package com.example.InvProject;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtService {
  // TODO: move to env/config later
  private static final String SECRET = "change-this-to-a-very-long-random-secret-change-this-please-1234567890";
  private static final Key KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

  public static String generate(String subject, Map<String,Object> claims, long minutes) {
    Instant now = Instant.now();
    return Jwts.builder()
      .setSubject(subject)
      .addClaims(claims)
      .setIssuedAt(Date.from(now))
      .setExpiration(Date.from(now.plusSeconds(minutes * 60)))
      .signWith(KEY, SignatureAlgorithm.HS256)
      .compact();
  }

  public static Jws<Claims> parse(String token) {
    return Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token);
  }
}
