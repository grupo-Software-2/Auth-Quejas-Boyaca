package com.uptc.authservice.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.uptc.authservice.dto.LoginRequest;
import com.uptc.authservice.dto.LoginResponse;
import com.uptc.authservice.entities.Session;
import com.uptc.authservice.entities.User;
import com.uptc.authservice.repositories.SessionRepository;
import com.uptc.authservice.repositories.UserRepository;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no existe"));

        boolean passwordValid = BCrypt.checkpw(request.getPassword(), user.getPassword());
        if (!passwordValid) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        sessionRepository.findByUserAndActiveTrue(user).forEach(s -> {
            s.setActive(false);
            sessionRepository.save(s);
        });

        String token = UUID.randomUUID().toString();

        Session session = new Session();
        session.setUser(user);
        session.setToken(token);
        session.setActive(true);
        session.setExpiresAt(LocalDateTime.now().plusHours(2));
        sessionRepository.save(session);

        return new LoginResponse(token, user.getUsername(), "Login exitoso");
    }

    @Override
    public boolean validateSession(String token) {
        Session session = sessionRepository.findByTokenAndActiveTrue(token)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido"));

        if (!session.getActive()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sesión inactiva");
        }

        if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sesión expirada");
        }

        return true;
    }

    @Override
    public boolean logout(String token) {
        Optional<Session> sessionOpt = sessionRepository.findByTokenAndActiveTrue(token);

        if (sessionOpt.isEmpty()) {
            return false;
        }

        Session session = sessionOpt.get();
        session.setActive(false);
        sessionRepository.save(session);

        return true;
    }
}
