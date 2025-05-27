package com.ccpc.yeprogress.restController;

import com.ccpc.yeprogress.dto.UserDTO;
import com.ccpc.yeprogress.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO UserDTO) {
        UserDTO createdUser = userService.createUser(UserDTO);
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping("/{uid}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String uid) {
        UserDTO UserDTO = userService.getUserByFirebaseId(uid);
        return ResponseEntity.ok(UserDTO);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{uid}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String uid, @RequestBody UserDTO UserDTO) {
        UserDTO updatedUser = userService.updateUser(uid, UserDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{uid}")
    public ResponseEntity<Void> deleteUser(@PathVariable String uid) {
        userService.deleteUser(uid);
        return ResponseEntity.noContent().build();
    }
}
