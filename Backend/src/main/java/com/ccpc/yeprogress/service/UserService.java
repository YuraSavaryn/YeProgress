package com.ccpc.yeprogress.service;

import com.ccpc.yeprogress.dto.UserDTO;
import com.ccpc.yeprogress.mapper.UserMapper;
import com.ccpc.yeprogress.model.User;
import com.ccpc.yeprogress.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDTO createUser(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    public User getUserFromDTO(UserDTO userDTO) {
        return userMapper.toEntity(userDTO);
    }

    public User getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user;
    }

    public User getUserByFirebaseId(String firebaseId) {
        return userRepository.findByFirebaseId(firebaseId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserDTO getUserDTOByFirebaseId(String firebaseId) {
        User user = userRepository.findByFirebaseId(firebaseId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toDto(user);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userMapper.updateEntityFromDto(userDTO, user); // Використовуємо MapStruct для оновлення
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    public UserDTO updateUser(String firebaseId, UserDTO userDTO) {
        User user = userRepository.findByFirebaseId(firebaseId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userMapper.updateEntityFromDto(userDTO, user); // Використовуємо MapStruct для оновлення
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    public void deleteUser(String firebaseId) {
        userRepository.deleteByFirebaseId(firebaseId);
    }
}
