package com.jkngil.pos.users.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.jkngil.JwtAuthorities.JwtClaimsParser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthorizationFilter extends BasicAuthenticationFilter {

	private Environment env;
	
	public AuthorizationFilter(
			AuthenticationManager authenticationManager,
			Environment env) {
		super(authenticationManager);
		this.env = env;
	}

	@Override
	protected void doFilterInternal(
			HttpServletRequest req, 
			HttpServletResponse res, 
			FilterChain chain) throws IOException, ServletException {
		String authorizationHeader = req.getHeader(env.getProperty("authorization.token.header.name"));
		
		if(authorizationHeader==null || !authorizationHeader.startsWith(env.getProperty("authorization.token.header.prefix"))) {
			chain.doFilter(req, res);
			return;
		}
		
		UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(req, res);
	}
	
	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req) {
		String authorizationHeader = req.getHeader(env.getProperty("authorization.token.header.name"));
		
		if(authorizationHeader==null) {
			return null;
		}
		
		String token = authorizationHeader.replace(env.getProperty("authorization.token.header.prefix"), "");
		String tokenSecret = env.getProperty("token.secret");
		
		if(tokenSecret == null) return null;
		
//		byte[] secretKeyBytes = Base64.getEncoder().encode(tokenSecret.getBytes());
//		SecretKey secretKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());
//		
//		JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
//		
//		Jwt<Header, Claims> jwt = jwtParser.parse(token);
//		String userId = jwt.getBody().getSubject();
//		
//		if(userId == null) {
//			return null;
//		}
//		
//		return new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>());
		JwtClaimsParser jwtClaimsParser = new JwtClaimsParser(token, tokenSecret);
		
		String userId = jwtClaimsParser.getJwtSubject();
		
		if(userId == null) {
			return null;
		}
		
		return new UsernamePasswordAuthenticationToken(userId, null, jwtClaimsParser.getUserAuthorities());
	}
}
