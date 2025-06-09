package com.ccpc.yeprogress.service;

import com.ccpc.yeprogress.dto.CommentsUserDTO;
import com.ccpc.yeprogress.exception.UserNotFoundException;
import com.ccpc.yeprogress.exception.UserValidationException;
import com.ccpc.yeprogress.logger.LoggerService;
import com.ccpc.yeprogress.mapper.CommentsUserMapper;
import com.ccpc.yeprogress.model.CommentsUser;
import com.ccpc.yeprogress.model.User;
import com.ccpc.yeprogress.repository.CommentsUserRepository;
import com.ccpc.yeprogress.repository.UserRepository;
import com.ccpc.yeprogress.validation.ValidationService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentsUserService {

    private static final Logger logger = LoggerService.getLogger(CommentsUserService.class);

    private final CommentsUserRepository commentsUserRepository;
    private final UserRepository userRepository;
    private final CommentsUserMapper commentsUserMapper;
    private final ValidationService validationService;

    @Autowired
    public CommentsUserService(CommentsUserRepository commentsUserRepository,
                               UserRepository userRepository,
                               CommentsUserMapper commentsUserMapper,
                               ValidationService validationService) {
        this.commentsUserRepository = commentsUserRepository;
        this.userRepository = userRepository;
        this.commentsUserMapper = commentsUserMapper;
        this.validationService = validationService;
    }

    @Transactional
    public CommentsUserDTO createComment(CommentsUserDTO commentsUserDTO) {
        LoggerService.logCreateAttempt(logger, "Comment", commentsUserDTO.getUserId());

        try {
            validationService.validateCommentDTO(commentsUserDTO);

            User user = userRepository.findById(commentsUserDTO.getUserId())
                    .orElseThrow(() -> {
                        LoggerService.logEntityNotFound(logger, "User", commentsUserDTO.getUserId());
                        return new UserNotFoundException("User with ID " + commentsUserDTO.getUserId() + " not found");
                    });

            User userAuthor = userRepository.findById(commentsUserDTO.getUserAuthorId())
                    .orElseThrow(() -> {
                        LoggerService.logEntityNotFound(logger, "User author", commentsUserDTO.getUserAuthorId());
                        return new UserNotFoundException("User author with ID " + commentsUserDTO.getUserAuthorId() + " not found");
                    });

            CommentsUser commentsUser = commentsUserMapper.toEntity(commentsUserDTO);
            commentsUser.setUser(user);
            commentsUser.setUserAuthor(userAuthor);
            CommentsUser savedComment = commentsUserRepository.save(commentsUser);
            LoggerService.logCreateSuccess(logger, "Comment", savedComment.getCommentId());

            return commentsUserMapper.toDto(savedComment);

        } catch (UserValidationException | UserNotFoundException e) {
            LoggerService.logError(logger, "Error creating comment: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "comment creation", e.getMessage(), e);
            throw new RuntimeException("Failed to create comment due to unexpected error", e);
        }
    }

    public CommentsUserDTO getCommentById(Long id) {
        LoggerService.logRetrieveAttempt(logger, "Comment", id);

        try {
            CommentsUser commentsUser = commentsUserRepository.findById(id)
                    .orElseThrow(() -> {
                        LoggerService.logEntityNotFound(logger, "Comment", id);
                        return new UserNotFoundException("Comment with ID " + id + " not found");
                    });

            LoggerService.logRetrieveSuccess(logger, "Comment", id);
            return commentsUserMapper.toDto(commentsUser);

        } catch (UserNotFoundException e) {
            LoggerService.logError(logger, "Error retrieving comment: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "comment retrieval", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve comment due to unexpected error", e);
        }
    }

    public List<CommentsUserDTO> getAllComments() {
        LoggerService.logRetrieveAttempt(logger, "All Comments", "all");

        try {
            List<CommentsUser> comments = commentsUserRepository.findAll();
            LoggerService.logRetrieveSuccess(logger, "All Comments", comments.size());

            return comments.stream()
                    .map(commentsUserMapper::toDto)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "retrieval of all comments", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve comments due to unexpected error", e);
        }
    }

    @Transactional
    public CommentsUserDTO updateComment(Long id, CommentsUserDTO commentsUserDTO) {
        LoggerService.logUpdateAttempt(logger, "Comment", id);

        try {
            validationService.validateCommentDTO(commentsUserDTO);

            CommentsUser commentsUser = commentsUserRepository.findById(id)
                    .orElseThrow(() -> {
                        LoggerService.logEntityNotFound(logger, "Comment", id);
                        return new UserNotFoundException("Comment with ID " + id + " not found");
                    });

            User user = userRepository.findById(commentsUserDTO.getUserId())
                    .orElseThrow(() -> {
                        LoggerService.logEntityNotFound(logger, "User", commentsUserDTO.getUserId());
                        return new UserNotFoundException("User with ID " + commentsUserDTO.getUserId() + " not found");
                    });

            User userAuthor = userRepository.findById(commentsUserDTO.getUserAuthorId())
                    .orElseThrow(() -> {
                        LoggerService.logEntityNotFound(logger, "User author", commentsUserDTO.getUserAuthorId());
                        return new UserNotFoundException("User author with ID " + commentsUserDTO.getUserAuthorId() + " not found");
                    });

            commentsUserMapper.updateEntityFromDto(commentsUserDTO, commentsUser);
            commentsUser.setUser(user);
            commentsUser.setUserAuthor(userAuthor);
            CommentsUser savedComment = commentsUserRepository.save(commentsUser);
            LoggerService.logUpdateSuccess(logger, "Comment", id);

            return commentsUserMapper.toDto(savedComment);

        } catch (UserValidationException | UserNotFoundException e) {
            LoggerService.logError(logger, "Error updating comment: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "comment update", e.getMessage(), e);
            throw new RuntimeException("Failed to update comment due to unexpected error", e);
        }
    }

    @Transactional
    public void deleteComment(Long id) {
        LoggerService.logDeleteAttempt(logger, "Comment", id);

        try {
            if (!commentsUserRepository.existsById(id)) {
                LoggerService.logEntityNotFound(logger, "Comment", id);
                throw new UserNotFoundException("Comment with ID " + id + " not found");
            }

            commentsUserRepository.deleteById(id);
            LoggerService.logDeleteSuccess(logger, "Comment", id);

        } catch (UserNotFoundException e) {
            LoggerService.logError(logger, "Error deleting comment: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "comment deletion", e.getMessage(), e);
            throw new RuntimeException("Failed to delete comment due to unexpected error", e);
        }
    }
}