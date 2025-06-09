package com.ccpc.yeprogress.service;

import com.ccpc.yeprogress.dto.UserDTO;
import com.ccpc.yeprogress.exception.UserAlreadyExistsException;
import com.ccpc.yeprogress.exception.UserNotFoundException;
import com.ccpc.yeprogress.exception.UserValidationException;
import com.ccpc.yeprogress.mapper.UserMapper;
import com.ccpc.yeprogress.model.User;
import com.ccpc.yeprogress.repository.UserRepository;
import com.ccpc.yeprogress.validation.ValidationService;
import com.ccpc.yeprogress.logger.LoggerService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger logger = LoggerService.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ValidationService validationService;

    @Autowired
    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       ValidationService validationService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.validationService = validationService;
    }

    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        LoggerService.logCreateAttempt(logger, "user", userDTO.getFirebaseId());

        try {
            validationService.validateUserDTO(userDTO, true);

            if (userRepository.existsByFirebaseId(userDTO.getFirebaseId())) {
                LoggerService.logWarn(logger, "User creation failed: Firebase ID {} already exists", userDTO.getFirebaseId());
                throw new UserAlreadyExistsException("User with Firebase ID " + userDTO.getFirebaseId() + " already exists");
            }

            User user = userMapper.toEntity(userDTO);
            User savedUser = userRepository.save(user);
            LoggerService.logCreateSuccess(logger, "user", savedUser.getUserId());

            return userMapper.toDto(savedUser);

        } catch (UserValidationException | UserAlreadyExistsException e) {
            LoggerService.logError(logger, "Error creating user: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "user creation", e.getMessage(), e);
            throw new RuntimeException("Failed to create user due to unexpected error", e);
        }
    }

    public User getUserFromDTO(UserDTO userDTO) {
        LoggerService.logDebug(logger, "Converting UserDTO to User entity for Firebase ID: {}", userDTO.getFirebaseId());

        try {
            validationService.validateUserDTO(userDTO, false);
            return userMapper.toEntity(userDTO);

        } catch (UserValidationException e) {
            LoggerService.logError(logger, "Error converting UserDTO to entity: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "UserDTO to entity conversion", e.getMessage(), e);
            throw new RuntimeException("Failed to convert UserDTO to entity due to unexpected error", e);
        }
    }

    public User getUserById(Long id) {
        LoggerService.logRetrieveAttempt(logger, "user", id);

        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> {
                        LoggerService.logEntityNotFound(logger, "User", id);
                        return new UserNotFoundException("User with ID " + id + " not found");
                    });

            LoggerService.logRetrieveSuccess(logger, "user", id);
            return user;

        } catch (UserNotFoundException e) {
            LoggerService.logError(logger, "Error retrieving user: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "user retrieval", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve user due to unexpected error", e);
        }
    }

    public User getUserByFirebaseId(String firebaseId) {
        LoggerService.logRetrieveAttempt(logger, "user", firebaseId);

        try {
            validationService.validateFirebaseId(firebaseId);

            User user = userRepository.findByFirebaseId(firebaseId)
                    .orElseThrow(() -> {
                        LoggerService.logEntityNotFound(logger, "User", firebaseId);
                        return new UserNotFoundException("User with Firebase ID " + firebaseId + " not found");
                    });

            LoggerService.logRetrieveSuccess(logger, "user", firebaseId);
            return user;

        } catch (UserNotFoundException | UserValidationException e) {
            LoggerService.logError(logger, "Error retrieving user: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "user retrieval", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve user due to unexpected error", e);
        }
    }

    public UserDTO getUserDTOByFirebaseId(String firebaseId) {
        LoggerService.logRetrieveAttempt(logger, "user DTO", firebaseId);

        try {
            validationService.validateFirebaseId(firebaseId);

            User user = userRepository.findByFirebaseId(firebaseId)
                    .orElseThrow(() -> {
                        LoggerService.logEntityNotFound(logger, "User", firebaseId);
                        return new UserNotFoundException("User with Firebase ID " + firebaseId + " not found");
                    });

            LoggerService.logRetrieveSuccess(logger, "user DTO", firebaseId);
            return userMapper.toDto(user);

        } catch (UserNotFoundException | UserValidationException e) {
            LoggerService.logError(logger, "Error retrieving user DTO: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "user DTO retrieval", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve user DTO due to unexpected error", e);
        }
    }

    public List<UserDTO> getAllUsers() {
        LoggerService.logInfo(logger, "Retrieving all users");

        try {
            List<User> users = userRepository.findAll();
            LoggerService.logDebug(logger, "Retrieved {} users", users.size());

            return users.stream()
                    .map(userMapper::toDto)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "all users retrieval", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve users due to unexpected error", e);
        }
    }

    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        LoggerService.logUpdateAttempt(logger, "user", id);

        try {
            validationService.validateUserDTO(userDTO, false);

            User user = userRepository.findById(id)
                    .orElseThrow(() -> {
                        LoggerService.logEntityNotFound(logger, "User", id);
                        return new UserNotFoundException("User with ID " + id + " not found");
                    });

            userMapper.updateEntityFromDto(userDTO, user);
            User savedUser = userRepository.save(user);
            LoggerService.logUpdateSuccess(logger, "user", id);

            return userMapper.toDto(savedUser);

        } catch (UserValidationException | UserNotFoundException e) {
            LoggerService.logError(logger, "Error updating user: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "user update", e.getMessage(), e);
            throw new RuntimeException("Failed to update user due to unexpected error", e);
        }
    }

    @Transactional
    public UserDTO updateUser(String firebaseId, UserDTO userDTO) {
        LoggerService.logUpdateAttempt(logger, "user", firebaseId);

        try {
            validationService.validateUserDTO(userDTO, false);
            validationService.validateFirebaseId(firebaseId);

            User user = userRepository.findByFirebaseId(firebaseId)
                    .orElseThrow(() -> {
                        LoggerService.logEntityNotFound(logger, "User", firebaseId);
                        return new UserNotFoundException("User with Firebase ID " + firebaseId + " not found");
                    });

            userMapper.updateEntityFromDto(userDTO, user);
            User savedUser = userRepository.save(user);
            LoggerService.logUpdateSuccess(logger, "user", firebaseId);

            return userMapper.toDto(savedUser);

        } catch (UserValidationException | UserNotFoundException e) {
            LoggerService.logError(logger, "Error updating user: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "user update", e.getMessage(), e);
            throw new RuntimeException("Failed to update user due to unexpected error", e);
        }
    }

    @Transactional
    public UserDTO updateVerificationUser(String firebaseId) {
        LoggerService.logInfo(logger, "Attempting to verify user with Firebase ID: {}", firebaseId);

        try {
            validationService.validateFirebaseId(firebaseId);

            User user = userRepository.findByFirebaseId(firebaseId)
                    .orElseThrow(() -> {
                        LoggerService.logEntityNotFound(logger, "User", firebaseId);
                        return new UserNotFoundException("User with Firebase ID " + firebaseId + " not found");
                    });

            user.setIsVerified(true);
            User savedUser = userRepository.save(user);
            LoggerService.logInfo(logger, "Successfully verified user with Firebase ID: {}", firebaseId);

            return userMapper.toDto(savedUser);

        } catch (UserValidationException | UserNotFoundException e) {
            LoggerService.logError(logger, "Error verifying user: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "user verification", e.getMessage(), e);
            throw new RuntimeException("Failed to verify user due to unexpected error", e);
        }
    }

    @Transactional
    public void deleteUser(String firebaseId) {
        LoggerService.logDeleteAttempt(logger, "user", firebaseId);

        try {
            validationService.validateFirebaseId(firebaseId);

            if (!userRepository.existsByFirebaseId(firebaseId)) {
                LoggerService.logWarn(logger, "Delete failed: User with Firebase ID {} not found", firebaseId);
                throw new UserNotFoundException("User with Firebase ID " + firebaseId + " not found");
            }

            userRepository.deleteByFirebaseId(firebaseId);
            LoggerService.logDeleteSuccess(logger, "user", firebaseId);

        } catch (UserValidationException | UserNotFoundException e) {
            LoggerService.logError(logger, "Error deleting user: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "user deletion", e.getMessage(), e);
            throw new RuntimeException("Failed to delete user due to unexpected error", e);
        }
    }
}