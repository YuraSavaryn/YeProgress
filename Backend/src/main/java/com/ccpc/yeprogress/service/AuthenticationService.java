package com.ccpc.yeprogress.service;

import com.ccpc.yeprogress.dto.AuthenticationDTO;
import com.ccpc.yeprogress.mapper.AuthenticationMapper;
import com.ccpc.yeprogress.model.Authentication;
import com.ccpc.yeprogress.model.User;
import com.ccpc.yeprogress.model.types.AuthenticationStatusType;
import com.ccpc.yeprogress.repository.AuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {
    private final AuthenticationRepository authenticationRepository;
    private final AuthenticationMapper authenticationMapper;

    @Autowired
    public AuthenticationService(AuthenticationRepository authenticationRepository, AuthenticationMapper authenticationMapper) {
        this.authenticationRepository = authenticationRepository;
        this.authenticationMapper = authenticationMapper;
    }

    public AuthenticationDTO createAuthentication(AuthenticationDTO authenticationDTO) {
        Authentication authentication = authenticationMapper.toEntity(authenticationDTO);
        Authentication savedAuthentication = authenticationRepository.save(authentication);
        return authenticationMapper.toDto(savedAuthentication);
    }

    public AuthenticationDTO createAuthentication(User user) {
        Authentication authentication = new Authentication();
        authentication.setUser(user);
        authentication.setStatus(AuthenticationStatusType.NOT_VERIFIED);
        Authentication savedAuthentication = authenticationRepository.save(authentication);
        return authenticationMapper.toDto(savedAuthentication);
    }

    public AuthenticationDTO getAuthenticationByUserId(Long id) {
        Authentication authentication = authenticationRepository.findByUser_UserId(id);
        return authenticationMapper.toDto(authentication);
    }

    public List<AuthenticationDTO> getAllAuthentications() {
        return authenticationRepository.findAll().stream()
                .map(authenticationMapper::toDto)
                .collect(Collectors.toList());
    }

    public AuthenticationDTO updateAuthentication(Long userId, AuthenticationDTO authenticationDTO) {
        Authentication authentication = authenticationRepository.findByUser_UserId(userId);
        authenticationMapper.updateEntityFromDto(authenticationDTO, authentication);
        Authentication savedAuthentication = authenticationRepository.save(authentication);
        return authenticationMapper.toDto(savedAuthentication);
    }

    public void deleteAuthentication(Long id) {
        authenticationRepository.deleteById(id);
    }
}