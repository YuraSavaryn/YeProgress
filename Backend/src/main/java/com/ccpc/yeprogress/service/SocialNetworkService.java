package com.ccpc.yeprogress.service;

import com.ccpc.yeprogress.dto.SocialNetworkDTO;
import com.ccpc.yeprogress.exception.UserNotFoundException;
import com.ccpc.yeprogress.exception.UserValidationException;
import com.ccpc.yeprogress.mapper.SocialNetworkMapper;
import com.ccpc.yeprogress.model.SocialNetwork;
import com.ccpc.yeprogress.model.User;
import com.ccpc.yeprogress.repository.SocialNetworkRepository;
import com.ccpc.yeprogress.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SocialNetworkService {

    private static final Logger logger = LoggerFactory.getLogger(SocialNetworkService.class);

    private final SocialNetworkRepository socialNetworkRepository;
    private final UserRepository userRepository;
    private final SocialNetworkMapper socialNetworkMapper;

    private static final int MAX_URL_LENGTH = 255;

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
        logger.info("Attempting to create social network for user ID: {}", socialNetworkDTO.getUserId());

        try {
            validateSocialNetworkDTO(socialNetworkDTO);

            User user = userRepository.findById(socialNetworkDTO.getUserId())
                    .orElseThrow(() -> {
                        logger.warn("User with ID {} not found", socialNetworkDTO.getUserId());
                        return new UserNotFoundException("User with ID " + socialNetworkDTO.getUserId() + " not found");
                    });

            SocialNetwork socialNetwork = socialNetworkMapper.toEntity(socialNetworkDTO);
            socialNetwork.setUser(user);
            SocialNetwork savedSocialNetwork = socialNetworkRepository.save(socialNetwork);
            logger.info("Successfully created social network with ID: {}", savedSocialNetwork.getContactId());

            return socialNetworkMapper.toDto(savedSocialNetwork);

        } catch (UserValidationException | UserNotFoundException e) {
            logger.error("Error creating social network: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during social network creation: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create social network due to unexpected error", e);
        }
    }

    public SocialNetworkDTO getSocialNetworkById(Long id) {
        logger.info("Retrieving social network with ID: {}", id);

        try {
            SocialNetwork socialNetwork = socialNetworkRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Social network with ID {} not found", id);
                        return new UserNotFoundException("Social network with ID " + id + " not found");
                    });

            logger.debug("Successfully retrieved social network with ID: {}", id);
            return socialNetworkMapper.toDto(socialNetwork);

        } catch (UserNotFoundException e) {
            logger.error("Error retrieving social network: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error retrieving social network: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve social network due to unexpected error", e);
        }
    }

    public List<SocialNetworkDTO> getAllSocialNetworks() {
        logger.info("Retrieving all social networks");

        try {
            List<SocialNetwork> socialNetworks = socialNetworkRepository.findAll();
            logger.debug("Retrieved {} social networks", socialNetworks.size());

            return socialNetworks.stream()
                    .map(socialNetworkMapper::toDto)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logger.error("Unexpected error retrieving all social networks: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve social networks due to unexpected error", e);
        }
    }

    @Transactional
    public SocialNetworkDTO updateSocialNetwork(Long id, SocialNetworkDTO socialNetworkDTO) {
        logger.info("Attempting to update social network with ID: {}", id);

        try {
            validateSocialNetworkDTO(socialNetworkDTO);

            SocialNetwork socialNetwork = socialNetworkRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Social network with ID {} not found", id);
                        return new UserNotFoundException("Social network with ID " + id + " not found");
                    });

            User user = userRepository.findById(socialNetworkDTO.getUserId())
                    .orElseThrow(() -> {
                        logger.warn("User with ID {} not found", socialNetworkDTO.getUserId());
                        return new UserNotFoundException("User with ID " + socialNetworkDTO.getUserId() + " not found");
                    });

            socialNetworkMapper.updateEntityFromDto(socialNetworkDTO, socialNetwork);
            socialNetwork.setUser(user);
            SocialNetwork savedSocialNetwork = socialNetworkRepository.save(socialNetwork);
            logger.info("Successfully updated social network with ID: {}", id);

            return socialNetworkMapper.toDto(savedSocialNetwork);

        } catch (UserValidationException | UserNotFoundException e) {
            logger.error("Error updating social network: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during social network update: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update social network due to unexpected error", e);
        }
    }

    @Transactional
    public void deleteSocialNetwork(Long id) {
        logger.info("Attempting to delete social network with ID: {}", id);

        try {
            if (!socialNetworkRepository.existsById(id)) {
                logger.warn("Delete failed: Social network with ID {} not found", id);
                throw new UserNotFoundException("Social network with ID " + id + " not found");
            }

            socialNetworkRepository.deleteById(id);
            logger.info("Successfully deleted social network with ID: {}", id);

        } catch (UserNotFoundException e) {
            logger.error("Error deleting social network: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during social network deletion: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete social network due to unexpected error", e);
        }
    }

    private void validateSocialNetworkDTO(SocialNetworkDTO socialNetworkDTO) {
        logger.debug("Validating social network DTO");

        if (socialNetworkDTO.getUserId() == null) {
            logger.warn("Validation failed: User ID is required");
            throw new UserValidationException("User ID is required");
        }

        if (!StringUtils.hasText(socialNetworkDTO.getSocialNetworkURL())) {
            logger.warn("Validation failed: Social network URL is required");
            throw new UserValidationException("Social network URL is required");
        }

        if (socialNetworkDTO.getSocialNetworkURL().length() > MAX_URL_LENGTH) {
            logger.warn("Validation failed: Social network URL exceeds {} characters", MAX_URL_LENGTH);
            throw new UserValidationException(
                    "Social network URL must not exceed " + MAX_URL_LENGTH + " characters");
        }

        if (!isValidUrl(socialNetworkDTO.getSocialNetworkURL())) {
            logger.warn("Validation failed: Invalid social network URL format: {}",
                    socialNetworkDTO.getSocialNetworkURL());
            throw new UserValidationException("Invalid social network URL format");
        }
    }

    private boolean isValidUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}