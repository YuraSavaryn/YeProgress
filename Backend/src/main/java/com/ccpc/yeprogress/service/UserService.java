package com.ccpc.yeprogress.service;

import com.ccpc.yeprogress.dto.UserDTO;
import com.ccpc.yeprogress.exception.UserAlreadyExistsException;
import com.ccpc.yeprogress.exception.UserNotFoundException;
import com.ccpc.yeprogress.exception.UserValidationException;
import com.ccpc.yeprogress.mapper.UserMapper;
import com.ccpc.yeprogress.model.User;
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
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private static final int MAX_NAME_LENGTH = 50;
    private static final int MAX_SURNAME_LENGTH = 50;
    private static final int MAX_PHONE_LENGTH = 20;
    private static final int MAX_EMAIL_LENGTH = 100;
    private static final int MAX_LOCATION_LENGTH = 100;
    private static final int MAX_DESCRIPTION_LENGTH = 500;
    private static final int MAX_IMG_URL_LENGTH = 255;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        logger.info("Attempting to create user with Firebase ID: {}", userDTO.getFirebaseId());

        try {
            validateUserDTO(userDTO, true);

            if (userRepository.existsByFirebaseId(userDTO.getFirebaseId())) {
                logger.warn("User creation failed: Firebase ID {} already exists", userDTO.getFirebaseId());
                throw new UserAlreadyExistsException("User with Firebase ID " + userDTO.getFirebaseId() + " already exists");
            }

            User user = userMapper.toEntity(userDTO);
            User savedUser = userRepository.save(user);
            logger.info("Successfully created user with ID: {}", savedUser.getUserId());

            return userMapper.toDto(savedUser);

        } catch (UserValidationException | UserAlreadyExistsException e) {
            logger.error("Error creating user: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during user creation: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create user due to unexpected error", e);
        }
    }

    public User getUserFromDTO(UserDTO userDTO) {
        logger.debug("Converting UserDTO to User entity for Firebase ID: {}", userDTO.getFirebaseId());

        try {
            validateUserDTO(userDTO, false);
            return userMapper.toEntity(userDTO);

        } catch (UserValidationException e) {
            logger.error("Error converting UserDTO to entity: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error converting UserDTO to entity: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to convert UserDTO to entity due to unexpected error", e);
        }
    }

    public User getUserById(Long id) {
        logger.info("Retrieving user with ID: {}", id);

        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("User with ID {} not found", id);
                        return new UserNotFoundException("User with ID " + id + " not found");
                    });

            logger.debug("Successfully retrieved user with ID: {}", id);
            return user;

        } catch (UserNotFoundException e) {
            logger.error("Error retrieving user: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error retrieving user: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve user due to unexpected error", e);
        }
    }

    public User getUserByFirebaseId(String firebaseId) {
        logger.info("Retrieving user with Firebase ID: {}", firebaseId);

        try {
            if (!StringUtils.hasText(firebaseId)) {
                logger.warn("Validation failed: Firebase ID is required");
                throw new UserValidationException("Firebase ID is required");
            }

            User user = userRepository.findByFirebaseId(firebaseId)
                    .orElseThrow(() -> {
                        logger.warn("User with Firebase ID {} not found", firebaseId);
                        return new UserNotFoundException("User with Firebase ID " + firebaseId + " not found");
                    });

            logger.debug("Successfully retrieved user with Firebase ID: {}", firebaseId);
            return user;

        } catch (UserNotFoundException | UserValidationException e) {
            logger.error("Error retrieving user: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error retrieving user: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve user due to unexpected error", e);
        }
    }

    public UserDTO getUserDTOByFirebaseId(String firebaseId) {
        logger.info("Retrieving user DTO with Firebase ID: {}", firebaseId);

        try {
            if (!StringUtils.hasText(firebaseId)) {
                logger.warn("Validation failed: Firebase ID is required");
                throw new UserValidationException("Firebase ID is required");
            }

            User user = userRepository.findByFirebaseId(firebaseId)
                    .orElseThrow(() -> {
                        logger.warn("User with Firebase ID {} not found", firebaseId);
                        return new UserNotFoundException("User with Firebase ID " + firebaseId + " not found");
                    });

            logger.debug("Successfully retrieved user DTO with Firebase ID: {}", firebaseId);
            return userMapper.toDto(user);

        } catch (UserNotFoundException | UserValidationException e) {
            logger.error("Error retrieving user DTO: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error retrieving user DTO: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve user DTO due to unexpected error", e);
        }
    }

    public List<UserDTO> getAllUsers() {
        logger.info("Retrieving all users");

        try {
            List<User> users = userRepository.findAll();
            logger.debug("Retrieved {} users", users.size());

            return users.stream()
                    .map(userMapper::toDto)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logger.error("Unexpected error retrieving all users: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve users due to unexpected error", e);
        }
    }

    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        logger.info("Attempting to update user with ID: {}", id);

        try {
            validateUserDTO(userDTO, false);

            User user = userRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("User with ID {} not found", id);
                        return new UserNotFoundException("User with ID " + id + " not found");
                    });

            userMapper.updateEntityFromDto(userDTO, user);
            User savedUser = userRepository.save(user);
            logger.info("Successfully updated user with ID: {}", id);

            return userMapper.toDto(savedUser);

        } catch (UserValidationException | UserNotFoundException e) {
            logger.error("Error updating user: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during user update: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update user due to unexpected error", e);
        }
    }

    @Transactional
    public UserDTO updateUser(String firebaseId, UserDTO userDTO) {
        logger.info("Attempting to update user with Firebase ID: {}", firebaseId);

        try {
            validateUserDTO(userDTO, false);

            if (!StringUtils.hasText(firebaseId)) {
                logger.warn("Validation failed: Firebase ID is required");
                throw new UserValidationException("Firebase ID is required");
            }

            User user = userRepository.findByFirebaseId(firebaseId)
                    .orElseThrow(() -> {
                        logger.warn("User with Firebase ID {} not found", firebaseId);
                        return new UserNotFoundException("User with Firebase ID " + firebaseId + " not found");
                    });

            userMapper.updateEntityFromDto(userDTO, user);
            User savedUser = userRepository.save(user);
            logger.info("Successfully updated user with Firebase ID: {}", firebaseId);

            return userMapper.toDto(savedUser);

        } catch (UserValidationException | UserNotFoundException e) {
            logger.error("Error updating user: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during user update: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update user due to unexpected error", e);
        }
    }

    @Transactional
    public UserDTO updateVerificationUser(String firebaseId) {
        logger.info("Attempting to verify user with Firebase ID: {}", firebaseId);

        try {
            if (!StringUtils.hasText(firebaseId)) {
                logger.warn("Validation failed: Firebase ID is required");
                throw new UserValidationException("Firebase ID is required");
            }

            User user = userRepository.findByFirebaseId(firebaseId)
                    .orElseThrow(() -> {
                        logger.warn("User with Firebase ID {} not found", firebaseId);
                        return new UserNotFoundException("User with Firebase ID " + firebaseId + " not found");
                    });

            user.setIsVerified(true);
            User savedUser = userRepository.save(user);
            logger.info("Successfully verified user with Firebase ID: {}", firebaseId);

            return userMapper.toDto(savedUser);

        } catch (UserValidationException | UserNotFoundException e) {
            logger.error("Error verifying user: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during user verification: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to verify user due to unexpected error", e);
        }
    }

    @Transactional
    public void deleteUser(String firebaseId) {
        logger.info("Attempting to delete user with Firebase ID: {}", firebaseId);

        try {
            if (!StringUtils.hasText(firebaseId)) {
                logger.warn("Validation failed: Firebase ID is required");
                throw new UserValidationException("Firebase ID is required");
            }

            if (!userRepository.existsByFirebaseId(firebaseId)) {
                logger.warn("Delete failed: User with Firebase ID {} not found", firebaseId);
                throw new UserNotFoundException("User with Firebase ID " + firebaseId + " not found");
            }

            userRepository.deleteByFirebaseId(firebaseId);
            logger.info("Successfully deleted user with Firebase ID: {}", firebaseId);

        } catch (UserValidationException | UserNotFoundException e) {
            logger.error("Error deleting user: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during user deletion: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete user due to unexpected error", e);
        }
    }

    private void validateUserDTO(UserDTO userDTO, boolean isCreate) {
        logger.debug("Validating user DTO for create: {}", isCreate);

        if (isCreate && !StringUtils.hasText(userDTO.getFirebaseId())) {
            logger.warn("Validation failed: Firebase ID is required");
            throw new UserValidationException("Firebase ID is required");
        }

        if (StringUtils.hasText(userDTO.getName())) {
            if (userDTO.getName().length() > MAX_NAME_LENGTH) {
                logger.warn("Validation failed: Name exceeds {} characters", MAX_NAME_LENGTH);
                throw new UserValidationException("Name must not exceed " + MAX_NAME_LENGTH + " characters");
            }
            if (!userDTO.getName().matches("^[a-zA-Zа-яА-ЯіІїЇєЄ\\s-']+$")) {
                logger.warn("Validation failed: Name contains invalid characters");
                throw new UserValidationException("Name contains invalid characters");
            }
        }

        if (StringUtils.hasText(userDTO.getSurname())) {
            if (userDTO.getSurname().length() > MAX_SURNAME_LENGTH) {
                logger.warn("Validation failed: Surname exceeds {} characters", MAX_SURNAME_LENGTH);
                throw new UserValidationException("Surname must not exceed " + MAX_SURNAME_LENGTH + " characters");
            }
            if (!userDTO.getSurname().matches("^[a-zA-Zа-яА-ЯіІїЇєЄ\\s-']+$")) {
                logger.warn("Validation failed: Surname contains invalid characters");
                throw new UserValidationException("Surname contains invalid characters");
            }
        }

        if (StringUtils.hasText(userDTO.getPhone())) {
            if (userDTO.getPhone().length() > MAX_PHONE_LENGTH) {
                logger.warn("Validation failed: Phone exceeds {} characters", MAX_PHONE_LENGTH);
                throw new UserValidationException("Phone must not exceed " + MAX_PHONE_LENGTH + " characters");
            }
            if (!userDTO.getPhone().matches("^\\+?[0-9\\s-]+$")) {
                logger.warn("Validation failed: Invalid phone format");
                throw new UserValidationException("Invalid phone format");
            }
        }

        if (StringUtils.hasText(userDTO.getEmail())) {
            if (userDTO.getEmail().length() > MAX_EMAIL_LENGTH) {
                logger.warn("Validation failed: Email exceeds {} characters", MAX_EMAIL_LENGTH);
                throw new UserValidationException("Email must not exceed " + MAX_EMAIL_LENGTH + " characters");
            }
            if (!userDTO.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                logger.warn("Validation failed: Invalid email format");
                throw new UserValidationException("Invalid email format");
            }
        }

        if (StringUtils.hasText(userDTO.getLocation())) {
            if (userDTO.getLocation().length() > MAX_LOCATION_LENGTH) {
                logger.warn("Validation failed: Location exceeds {} characters", MAX_LOCATION_LENGTH);
                throw new UserValidationException("Location must not exceed " + MAX_LOCATION_LENGTH + " characters");
            }
        }

        if (StringUtils.hasText(userDTO.getDescription())) {
            if (userDTO.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
                logger.warn("Validation failed: Description exceeds {} characters", MAX_DESCRIPTION_LENGTH);
                throw new UserValidationException("Description must not exceed " + MAX_DESCRIPTION_LENGTH + " characters");
            }
        }

        if (StringUtils.hasText(userDTO.getImgUrl()) && !isValidUrl(userDTO.getImgUrl())) {
            logger.warn("Validation failed: Invalid image URL format: {}", userDTO.getImgUrl());
            throw new UserValidationException("Invalid image URL format");
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