package com.uptc.authservice.config;

import com.uptc.authservice.entities.User;
import com.uptc.authservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class InitialDataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@system.com");
            admin.setPassword(BCrypt.hashpw("admin123", BCrypt.gensalt()));
            admin.setActivo(true);
            userRepository.save(admin);
            System.out.println("Usuario admin creado con contraseña encriptada");
        } else {
            System.out.println("Usuario admin ya existe");
        }
    }
}
