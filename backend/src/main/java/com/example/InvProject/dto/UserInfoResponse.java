package com.example.InvProject.dto;

import java.util.Set;

public record UserInfoResponse(
    Long id,
    String username,
    Set<String> roles
) {}