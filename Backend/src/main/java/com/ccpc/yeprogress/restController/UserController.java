package com.ccpc.yeprogress.restController;

import com.ccpc.yeprogress.dto.UserDTO;
import com.ccpc.yeprogress.service.AuthenticationService;
import com.ccpc.yeprogress.service.UserService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    public UserController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        //authenticationService.createAuthentication(userService.getUserFromDTO(createdUser));
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping("/{uid}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String uid) {
        UserDTO UserDTO = userService.getUserDTOByFirebaseId(uid);
        return ResponseEntity.ok(UserDTO);
    }

    @GetMapping("/comments/{ids}")
    public ResponseEntity<List<UserDTO>> getUserIdByComments(@PathVariable List<Long> ids) {
        List<UserDTO> users = userService.getUsersDTOById(ids);
        return ResponseEntity.ok(users);
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

    @PutMapping("/verified/{uid}")
    public ResponseEntity<UserDTO> updateVerificationUser(@PathVariable String uid) {
        UserDTO updateUser = userService.updateVerificationUser(uid);
        return ResponseEntity.ok(updateUser);
    }

    @DeleteMapping("/{uid}")
    public ResponseEntity<Void> deleteUser(@PathVariable String uid) {
        userService.deleteUser(uid);
        return ResponseEntity.noContent().build();
    }
}
