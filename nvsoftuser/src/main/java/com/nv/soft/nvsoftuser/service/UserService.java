package com.nv.soft.nvsoftuser.service;


import com.nv.soft.nvsoftuser.entity.User;
import com.nv.soft.nvsoftuser.modal.LoginRequest;
import com.nv.soft.nvsoftuser.modal.RegisterRequest;
import com.nv.soft.nvsoftuser.modal.UserResponse;
import com.nv.soft.nvsoftuser.repo.UserRepository;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    //private final PasswordEncoder passwordEncoder;

    // SLF4J logger
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserResponse register(RegisterRequest request) {

        log.info("Register called for email={}", request.email());
        log.debug("RegisterRequest details: name={}, email={}", request.name(), request.email());

        if (userRepository.existsByEmail(request.email())) {
           // throw new UserAlreadyExistsException("Email already registered");
           log.warn("Attempt to register already existing email={}", request.email());
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(request.password())
                .build();

        User savedUser = userRepository.save(user);

        log.info("User saved with id={}", savedUser.getId());

        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail()
        );
    }

    public UserResponse login(LoginRequest request) {

        log.info("Login called for email={}", request.email());

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> {
                    log.warn("Login failed for email={}", request.email());
                    return new RuntimeException("Invalid credentials");
                });

     /*   if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }*/

        log.info("Login successful for userId={}", user.getId());

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
