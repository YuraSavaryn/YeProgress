package com.ccpc.yeprogress.restController;

import com.ccpc.yeprogress.dto.AuthenticationMethodDTO;
import com.ccpc.yeprogress.service.AuthenticationMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authentication-methods")
public class AuthenticationMethodController {

    @Autowired
    private AuthenticationMethodService authenticationMethodService;

    @PostMapping
    public ResponseEntity<AuthenticationMethodDTO> createAuthenticationMethod(@RequestBody AuthenticationMethodDTO authenticationMethodDTO) {
        AuthenticationMethodDTO createdAuthenticationMethod = authenticationMethodService.createAuthenticationMethod(authenticationMethodDTO);
        return ResponseEntity.ok(createdAuthenticationMethod);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthenticationMethodDTO> getAuthenticationMethodById(@PathVariable Long id) {
        AuthenticationMethodDTO authenticationMethodDTO = authenticationMethodService.getAuthenticationMethodById(id);
        return ResponseEntity.ok(authenticationMethodDTO);
    }

    @GetMapping
    public ResponseEntity<List<AuthenticationMethodDTO>> getAllAuthenticationMethods() {
        List<AuthenticationMethodDTO> authenticationMethods = authenticationMethodService.getAllAuthenticationMethods();
        return ResponseEntity.ok(authenticationMethods);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthenticationMethodDTO> updateAuthenticationMethod(@PathVariable Long id, @RequestBody AuthenticationMethodDTO authenticationMethodDTO) {
        AuthenticationMethodDTO updatedAuthenticationMethod = authenticationMethodService.updateAuthenticationMethod(id, authenticationMethodDTO);
        return ResponseEntity.ok(updatedAuthenticationMethod);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthenticationMethod(@PathVariable Long id) {
        authenticationMethodService.deleteAuthenticationMethod(id);
        return ResponseEntity.noContent().build();
    }
}