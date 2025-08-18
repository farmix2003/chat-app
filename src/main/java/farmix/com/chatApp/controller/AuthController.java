package farmix.com.chatApp.controller;

import farmix.com.chatApp.config.JwtUtils;
import farmix.com.chatApp.entity.User;
import farmix.com.chatApp.repository.UserRepository;
import farmix.com.chatApp.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user){
        User isUserExist = userRepository.findByEmail(user.getEmail());

        if (isUserExist != null){
            return ResponseEntity.badRequest().body("User already exist");
        }

        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setRole(user.getRole());
        newUser.setCreatedAt(user.getCreatedAt());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setUsername(user.getUsername());
        userRepository.save(newUser);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody User user){
        User isUserExist = userRepository.findByEmail(user.getEmail());
        if (isUserExist == null){
            AuthResponse response = new AuthResponse();
            response.setToken("");
            response.setMessage("Email is wrong. Please check your email!");
            return ResponseEntity.badRequest().body(response);
        }
        if(!passwordEncoder.matches(user.getPassword(), isUserExist.getPassword())){
            AuthResponse response = new AuthResponse();
            response.setToken("");
            response.setMessage("Password is wrong. Please check your password!");
        }
        String jwt = jwtUtils.generateToken(user.getUsername());
        AuthResponse response = new AuthResponse();
        response.setToken(jwt);
        response.setMessage("Login successfully");
        return ResponseEntity.ok(response);
    }
}
