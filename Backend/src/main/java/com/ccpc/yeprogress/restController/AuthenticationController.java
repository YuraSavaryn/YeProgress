package com.ccpc.yeprogress.restController;

import com.ccpc.yeprogress.dto.AuthenticationDTO;
import com.ccpc.yeprogress.model.User;
import com.ccpc.yeprogress.repository.UserRepository;
import com.ccpc.yeprogress.service.AuthenticationService;
import com.ccpc.yeprogress.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authentications")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<AuthenticationDTO> createAuthentication(@RequestBody AuthenticationDTO authenticationDTO) {
        AuthenticationDTO createdAuthentication = authenticationService.createAuthentication(authenticationDTO);
        return ResponseEntity.ok(createdAuthentication);
    }

    @GetMapping("/{uid}")
    public ResponseEntity<AuthenticationDTO> getAuthenticationByUserId(@PathVariable String uid) {
        User user = userService.getUserByFirebaseId(uid);
        AuthenticationDTO authenticationDTO = authenticationService.getAuthenticationByUserId(user.getUserId());
        return ResponseEntity.ok(authenticationDTO);
    }

    @GetMapping
    public ResponseEntity<List<AuthenticationDTO>> getAllAuthentications() {
        List<AuthenticationDTO> authentications = authenticationService.getAllAuthentications();
        return ResponseEntity.ok(authentications);
    }

    @PutMapping("/{uid}")
    public ResponseEntity<AuthenticationDTO> updateAuthentication(@PathVariable String uid,
                                                                  @RequestBody AuthenticationDTO authenticationDTO) {
        User user = userService.getUserByFirebaseId(uid);
        Long userId = user.getUserId();
        AuthenticationDTO updatedAuthentication = authenticationService.updateAuthentication(userId, authenticationDTO);
        return ResponseEntity.ok(updatedAuthentication);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthentication(@PathVariable Long id) {
        authenticationService.deleteAuthentication(id);
        return ResponseEntity.noContent().build();
    }
}