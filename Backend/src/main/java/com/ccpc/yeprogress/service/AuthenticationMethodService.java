package com.ccpc.yeprogress.service;

import com.ccpc.yeprogress.dto.AuthenticationMethodDTO;
import com.ccpc.yeprogress.mapper.AuthenticationMethodMapper;
import com.ccpc.yeprogress.model.AuthenticationMethod;
import com.ccpc.yeprogress.repository.AuthenticationMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthenticationMethodService {
    private final AuthenticationMethodRepository authenticationMethodRepository;
    private final AuthenticationMethodMapper authenticationMethodMapper;

    @Autowired
    public AuthenticationMethodService(AuthenticationMethodRepository authenticationMethodRepository, AuthenticationMethodMapper authenticationMethodMapper) {
        this.authenticationMethodRepository = authenticationMethodRepository;
        this.authenticationMethodMapper = authenticationMethodMapper;
    }

    public AuthenticationMethodDTO createAuthenticationMethod(AuthenticationMethodDTO authenticationMethodDTO) {
        AuthenticationMethod authenticationMethod = authenticationMethodMapper.toEntity(authenticationMethodDTO);
        AuthenticationMethod savedAuthenticationMethod = authenticationMethodRepository.save(authenticationMethod);
        return authenticationMethodMapper.toDto(savedAuthenticationMethod);
    }

    public AuthenticationMethodDTO getAuthenticationMethodById(Long id) {
        AuthenticationMethod authenticationMethod = authenticationMethodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AuthenticationMethod not found"));
        return authenticationMethodMapper.toDto(authenticationMethod);
    }

    public List<AuthenticationMethodDTO> getAllAuthenticationMethods() {
        return authenticationMethodRepository.findAll().stream()
                .map(authenticationMethodMapper::toDto)
                .collect(Collectors.toList());
    }

    public AuthenticationMethodDTO updateAuthenticationMethod(Long id, AuthenticationMethodDTO authenticationMethodDTO) {
        AuthenticationMethod authenticationMethod = authenticationMethodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AuthenticationMethod not found"));
        authenticationMethodMapper.updateEntityFromDto(authenticationMethodDTO, authenticationMethod);
        AuthenticationMethod savedAuthenticationMethod = authenticationMethodRepository.save(authenticationMethod);
        return authenticationMethodMapper.toDto(savedAuthenticationMethod);
    }

    public void deleteAuthenticationMethod(Long id) {
        authenticationMethodRepository.deleteById(id);
    }
}