package com.ccpc.yeprogress.service;

import com.ccpc.yeprogress.dto.CommentsUserDTO;
import com.ccpc.yeprogress.mapper.CommentsUserMapper;
import com.ccpc.yeprogress.model.CommentsUser;
import com.ccpc.yeprogress.model.User;
import com.ccpc.yeprogress.repository.CommentsUserRepository;
import com.ccpc.yeprogress.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentsUserService {
    private final CommentsUserRepository commentsUserRepository;
    private final UserRepository userRepository;
    private final CommentsUserMapper commentsUserMapper;

    @Autowired
    public CommentsUserService(CommentsUserRepository commentsUserRepository,
                               UserRepository userRepository,
                               CommentsUserMapper commentsUserMapper) {
        this.commentsUserRepository = commentsUserRepository;
        this.userRepository = userRepository;
        this.commentsUserMapper = commentsUserMapper;
    }

    @Transactional
    public CommentsUserDTO createComment(CommentsUserDTO commentsUserDTO) {
        User user = userRepository.findById(commentsUserDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + commentsUserDTO.getUserId()));
        User userAuthor = userRepository.findById(commentsUserDTO.getUserAuthorId())
                .orElseThrow(() -> new RuntimeException("User author not found with id: " + commentsUserDTO.getUserAuthorId()));

        CommentsUser commentsUser = commentsUserMapper.toEntity(commentsUserDTO);
        commentsUser.setUser(user);
        commentsUser.setUserAuthor(userAuthor);
        CommentsUser savedComment = commentsUserRepository.save(commentsUser);
        return commentsUserMapper.toDto(savedComment);
    }

    public CommentsUserDTO getCommentById(Long id) {
        CommentsUser commentsUser = commentsUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));
        return commentsUserMapper.toDto(commentsUser);
    }

    public List<CommentsUserDTO> getAllComments() {
        return commentsUserRepository.findAll().stream()
                .map(commentsUserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentsUserDTO updateComment(Long id, CommentsUserDTO commentsUserDTO) {
        CommentsUser commentsUser = commentsUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));

        User user = userRepository.findById(commentsUserDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + commentsUserDTO.getUserId()));
        User userAuthor = userRepository.findById(commentsUserDTO.getUserAuthorId())
                .orElseThrow(() -> new RuntimeException("User author not found with id: " + commentsUserDTO.getUserAuthorId()));

        commentsUserMapper.updateEntityFromDto(commentsUserDTO, commentsUser);
        commentsUser.setUser(user);
        commentsUser.setUserAuthor(userAuthor);
        CommentsUser savedComment = commentsUserRepository.save(commentsUser);
        return commentsUserMapper.toDto(savedComment);
    }

    @Transactional
    public void deleteComment(Long id) {
        commentsUserRepository.deleteById(id);
    }
}