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

    public UserDTO createUser(UserDTO UserDTO) {
        User user = userMapper.toEntity(UserDTO);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toDto(user);
    }

    public UserDTO getUserByFirebaseId(String firebaseId) {
        User user = userRepository.findByFirebaseId(firebaseId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toDto(user);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserDTO updateUser(Long id, UserDTO UserDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userMapper.updateEntityFromDto(UserDTO, user); // Використовуємо MapStruct для оновлення
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    public UserDTO updateUser(String firebaseId, UserDTO UserDTO) {
        User user = userRepository.findByFirebaseId(firebaseId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userMapper.updateEntityFromDto(UserDTO, user); // Використовуємо MapStruct для оновлення
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void deleteUser(String firebaseId) {
        userRepository.deleteByFirebaseId(firebaseId);
    }
}
