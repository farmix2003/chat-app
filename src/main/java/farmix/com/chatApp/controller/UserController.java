package farmix.com.chatApp.controller;

import farmix.com.chatApp.dto.UserDTO;
import farmix.com.chatApp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers(@RequestHeader("Authorization") String token) {
       return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

}
