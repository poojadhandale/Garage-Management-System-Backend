package com.garage.management.Controller;


import com.garage.management.Service.AuthService;
import com.garage.management.Security.AuthRequest;
import com.garage.management.Security.AuthResponse;
import com.garage.management.Entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        return authService.register(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            String token = authService.login(request.getUsername(), request.getPassword());
            AuthResponse response = new AuthResponse(
                    HttpStatus.OK.value(),
                    "Login successful",
                    token
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            AuthResponse response = new AuthResponse(
                    HttpStatus.UNAUTHORIZED.value(),
                    "Invalid credentials",
                    null
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
