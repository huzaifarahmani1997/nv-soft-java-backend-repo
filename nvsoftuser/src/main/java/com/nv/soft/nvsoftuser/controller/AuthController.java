package com.nv.soft.nvsoftuser.controller;


import com.nv.soft.nvsoftuser.modal.LoginRequest;
import com.nv.soft.nvsoftuser.modal.RegisterRequest;
import com.nv.soft.nvsoftuser.modal.UserResponse;
import com.nv.soft.nvsoftuser.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class AuthController {

    private final UserService userService;

    // SLF4J logger
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    // ✅ Constructor Injection
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // GET endpoint for testing
    @GetMapping("/users")
    public ResponseEntity<String> users() {
        log.info("GET /user/users called");
        log.info("GET /user/users called");
        return ResponseEntity.ok("Hello, User!");
    }


    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest request) {
        // Log safely: do not log raw passwords
        log.info("POST /user/register called for email={}", request.email());
        log.debug("RegisterRequest details: name={}, email={}", request.name(), request.email());
        UserResponse response = userService.register(request);
        log.info("User registered with id={}", response.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest request) {
        log.info("POST /user/login called for email={}", request.email());
        // don't log password
        UserResponse response = userService.login(request);
        log.info("User login successful for id={}", response.id());
        return ResponseEntity.ok(response); // 200 OK
    }
}
