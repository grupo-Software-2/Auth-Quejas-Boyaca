package com.uptc.authservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthClientService {

    @Autowired
    private RestTemplate restTemplate;

    public boolean validateAuthToken(String token) {
        String url = "https://auth-quejas-boyaca.onrender.com";
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setBearerAuth(token);
        org.springframework.http.HttpEntity<Void> request = new org.springframework.http.HttpEntity<>(headers);

        try {
            org.springframework.http.ResponseEntity<String> response = restTemplate
                    .exchange(url, org.springframework.http.HttpMethod.GET, request, String.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (HttpClientErrorException e) {
            System.out.println("Token inválido: " + e.getStatusCode());
            return false;
        } catch (ResourceAccessException e) {
            System.out.println("Auth Service no disponible: " + e.getMessage());
            return false;
        } catch (HttpServerErrorException e) {
            System.out.println("Error en Auth Service: " + e.getStatusCode());
            return false;
        }
    }
}