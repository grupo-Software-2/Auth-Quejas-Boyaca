package com.uptc.authservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uptc.authservice.dto.LoginRequest;
import com.uptc.authservice.dto.LoginResponse;
import com.uptc.authservice.services.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    in = SecuritySchemeIn.HEADER
)

@RestController
@RequestMapping("/auth")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Autenticación", description = "Endpoints para login, logout y validación de sesión")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "Iniciar sesión", description = "Autentica al usuario y devuelve un token de sesión")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login exitoso"),
        @ApiResponse(responseCode = "401", description = "Credenciales inválidas"),
        @ApiResponse(responseCode = "503", description = "Servicio Auth no disponible")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Validar sesión", description = "Verifica si el token de sesión es válido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sesión válida"),
        @ApiResponse(responseCode = "401", description = "Token inválido o sesión no encontrada"),
        @ApiResponse(responseCode = "503", description = "Servicio Auth no disponible")
    })
    @GetMapping("/validate-session")
    public ResponseEntity<String> validateSession(@RequestHeader("Authorization") String authorizationHeader) {

        if (!authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        }

        String token = authorizationHeader.substring(7);
        boolean isValid = authService.validateSession(token);

        if (!isValid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sesión no válida o expirada");
        }

        return ResponseEntity.ok("Sesión válida");
    }
    
    @Operation(summary = "Cerrar sesión", description = "Marca la sesión como inactiva o la elimina")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sesión cerrada correctamente"),
        @ApiResponse(responseCode = "401", description = "Token inválido o sesión no encontrada"),
        @ApiResponse(responseCode = "503", description = "Servicio Auth no disponible")
    })
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authorizationHeader) {
        if (!authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        }

        String token = authorizationHeader.substring(7);
        boolean success = authService.logout(token);

        if (success) {
            return ResponseEntity.ok("Sesión cerrada correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body("Token inválido o sesión no encontrada");
        }
    }
}
