package com.ccpc.yeprogress.service;

import com.ccpc.yeprogress.dto.SocialNetworkDTO;
import com.ccpc.yeprogress.mapper.SocialNetworkMapper;
import com.ccpc.yeprogress.model.SocialNetwork;
import com.ccpc.yeprogress.model.User;
import com.ccpc.yeprogress.repository.SocialNetworkRepository;
import com.ccpc.yeprogress.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SocialNetworkService {
    private final SocialNetworkRepository socialNetworkRepository;
    private final UserRepository userRepository;
    private final SocialNetworkMapper socialNetworkMapper;

    @Autowired
    public SocialNetworkService(SocialNetworkRepository socialNetworkRepository,
                                UserRepository userRepository,
                                SocialNetworkMapper socialNetworkMapper) {
        this.socialNetworkRepository = socialNetworkRepository;
        this.userRepository = userRepository;
        this.socialNetworkMapper = socialNetworkMapper;
    }

    @Transactional
    public SocialNetworkDTO createSocialNetwork(SocialNetworkDTO socialNetworkDTO) {
        User user = userRepository.findById(socialNetworkDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + socialNetworkDTO.getUserId()));

        SocialNetwork socialNetwork = socialNetworkMapper.toEntity(socialNetworkDTO);
        socialNetwork.setUser(user); // Оновлено з setUserId на setUser
        SocialNetwork savedSocialNetwork = socialNetworkRepository.save(socialNetwork);
        return socialNetworkMapper.toDto(savedSocialNetwork);
    }

    public SocialNetworkDTO getSocialNetworkById(Long id) {
        SocialNetwork socialNetwork = socialNetworkRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SocialNetwork not found with id: " + id));
        return socialNetworkMapper.toDto(socialNetwork);
    }

    public List<SocialNetworkDTO> getAllSocialNetworks() {
        return socialNetworkRepository.findAll().stream()
                .map(socialNetworkMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public SocialNetworkDTO updateSocialNetwork(Long id, SocialNetworkDTO socialNetworkDTO) {
        SocialNetwork socialNetwork = socialNetworkRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SocialNetwork not found with id: " + id));

        User user = userRepository.findById(socialNetworkDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + socialNetworkDTO.getUserId()));

        socialNetworkMapper.updateEntityFromDto(socialNetworkDTO, socialNetwork);
        socialNetwork.setUser(user); // Оновлено з setUserId на setUser
        SocialNetwork savedSocialNetwork = socialNetworkRepository.save(socialNetwork);
        return socialNetworkMapper.toDto(savedSocialNetwork);
    }

    @Transactional
    public void deleteSocialNetwork(Long id) {
        socialNetworkRepository.deleteById(id);
    }
}