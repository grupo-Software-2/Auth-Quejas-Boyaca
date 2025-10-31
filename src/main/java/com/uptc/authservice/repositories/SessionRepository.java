package com.uptc.authservice.repositories;

import com.uptc.authservice.entities.Session;
import com.uptc.authservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> findByTokenAndActiveTrue(String token);

    List<Session> findByUserAndActiveTrue(User user);
}
