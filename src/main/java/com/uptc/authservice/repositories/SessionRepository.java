package com.uptc.authservice.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uptc.authservice.entities.Session;

public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> findByTokenAndActiveTrue(String token);
}
