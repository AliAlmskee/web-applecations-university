package library.seeder;


import library.user.Role;
import library.user.User;
import library.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
@RequiredArgsConstructor
public class UserSeeder {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void seed() {
        if (userRepository.count() == 0) {
            List<User> users = new ArrayList<>();

            User admin1 = new User();
            admin1.setFirstname("John");
            admin1.setLastname("Doe");
            admin1.setEmail("johndoe@example.com");
            admin1.setPhone("0987651321");
            admin1.setPassword(passwordEncoder.encode("securePassword123"));
            admin1.setRole(Role.ADMIN);
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