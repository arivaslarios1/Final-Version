package com.example.InvProject.controllers;

import com.example.InvProject.JwtService;
import com.example.InvProject.dto.LoginRequest;
import com.example.InvProject.dto.RegisterRequest;
import com.example.InvProject.dto.UserInfoResponse;
import com.example.InvProject.entity.User;
import com.example.InvProject.service.AuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthService authService;
  public AuthController(AuthService authService) { this.authService = authService; }

  public record AuthResponse(String token){}

  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest req) {
    User user = authService.register(req);  // make sure your AuthService has this signature
    String token = JwtService.generate(
      user.getEmail(),
      Map.of("uid", user.getId(), "name", user.getName(), "role",
             user.getRole() != null ? user.getRole().getName() : "STAFF"),
      60
    );
    return ResponseEntity.ok(new AuthResponse(token));
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest req) {
    User user = authService.authenticate(req); // ensure this signature exists
    String token = JwtService.generate(
      user.getEmail(),
      Map.of("uid", user.getId(), "name", user.getName(), "role",
             user.getRole() != null ? user.getRole().getName() : "STAFF"),
      60
    );
    return ResponseEntity.ok(new AuthResponse(token));
  }

  @GetMapping("/me")
  public ResponseEntity<UserInfoResponse> me(@RequestHeader(value="Authorization", required=false) String authHeader) {
    if (authHeader == null || !authHeader.startsWith("Bearer ")) return ResponseEntity.status(401).build();
    String token = authHeader.substring(7);
    try {
      Jws<Claims> jws = JwtService.parse(token);
      var c = jws.getBody();
      Long id = c.get("uid", Number.class).longValue();
      String name = c.get("name", String.class);
      String role = c.get("role", String.class);
      // Your UserInfoResponse is (Long id, String name, Set<String> roles)
      return ResponseEntity.ok(new UserInfoResponse(id, name, Set.of(role)));
    } catch (JwtException ex) {
      return ResponseEntity.status(401).build();
    }
  }
}
