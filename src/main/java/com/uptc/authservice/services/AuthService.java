package com.uptc.authservice.services;

import com.uptc.authservice.dto.LoginRequest;
import com.uptc.authservice.dto.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
}
