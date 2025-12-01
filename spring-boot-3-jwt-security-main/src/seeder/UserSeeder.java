package com.main.seeder;

import com.main.entity.Role;
import com.main.entity.User;
import com.main.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserSeeder {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void seed() {
        if (userRepository.count() == 0) {
            List<User> users = new ArrayList<>();

            User admin1 = User.builder()
                    .firstname("John")
                    .lastname("Doe")
                    .phone("0987651321")
                    .password(passwordEncoder.encode("securePassword123"))
                    .role(Role.ADMIN)
                    .build();
            users.add(admin1);

            userRepository.saveAll(users);
        }
    }

   // void makeToken(User user){
//        var jwtToken = jwtService.generateToken(user);
//        var refreshToken = jwtService.generateRefreshToken(user);
//        var token = Token.builder()
//                .user(user)
//                .token(jwtToken)
//                .tokenType(TokenType.BEARER)
//                .expired(false)
//                .revoked(false)
//                .build();
//        System.out.println(jwtToken);
//        tokenRepository.save(token);
//    }
}