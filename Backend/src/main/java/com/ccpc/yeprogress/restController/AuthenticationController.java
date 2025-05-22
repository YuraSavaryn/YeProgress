package com.ccpc.yeprogress.restController;

import com.ccpc.yeprogress.dto.AuthenticationDTO;
import com.ccpc.yeprogress.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authentications")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<AuthenticationDTO> createAuthentication(@RequestBody AuthenticationDTO authenticationDTO) {
        AuthenticationDTO createdAuthentication = authenticationService.createAuthentication(authenticationDTO);
        return ResponseEntity.ok(createdAuthentication);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthenticationDTO> getAuthenticationById(@PathVariable Long id) {
        AuthenticationDTO authenticationDTO = authenticationService.getAuthenticationById(id);
        return ResponseEntity.ok(authenticationDTO);
    }

    @GetMapping
    public ResponseEntity<List<AuthenticationDTO>> getAllAuthentications() {
        List<AuthenticationDTO> authentications = authenticationService.getAllAuthentications();
        return ResponseEntity.ok(authentications);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthenticationDTO> updateAuthentication(@PathVariable Long id, @RequestBody AuthenticationDTO authenticationDTO) {
        AuthenticationDTO updatedAuthentication = authenticationService.updateAuthentication(id, authenticationDTO);
        return ResponseEntity.ok(updatedAuthentication);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthentication(@PathVariable Long id) {
        authenticationService.deleteAuthentication(id);
        return ResponseEntity.noContent().build();
    }
}