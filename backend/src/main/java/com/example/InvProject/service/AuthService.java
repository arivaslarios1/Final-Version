package com.example.InvProject.service;

import com.example.InvProject.dto.LoginRequest;
import com.example.InvProject.dto.RegisterRequest;
import com.example.InvProject.entity.Role;
import com.example.InvProject.entity.User;
import com.example.InvProject.repository.RoleRepository;
import com.example.InvProject.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  public AuthService(UserRepository userRepository,
                     RoleRepository roleRepository,
                     PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
  }

  // CREATE ACCOUNT
  public User register(RegisterRequest req) {
    // make sure email is not already used
    userRepository.findByEmail(req.getEmail())
        .ifPresent(u -> { throw new IllegalArgumentException("Email already in use"); });

    // find or create STAFF role
    Role role = roleRepository.findByName("STAFF")
        .orElseGet(() -> {
          Role r = new Role();
          r.setName("STAFF");
          return roleRepository.save(r);
        });

    // build the user entity
    User u = new User();
    u.setName(req.getName());
    u.setEmail(req.getEmail());
    u.setPasswordHash(passwordEncoder.encode(req.getPassword()));
    u.setRole(role);

    return userRepository.save(u);
  }

  // LOGIN
  public User authenticate(LoginRequest req) {
    User u = userRepository.findByEmail(req.getEmail())
        .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

    if (!passwordEncoder.matches(req.getPassword(), u.getPasswordHash())) {
      throw new IllegalArgumentException("Invalid credentials");
    }

    return u;
  }
}