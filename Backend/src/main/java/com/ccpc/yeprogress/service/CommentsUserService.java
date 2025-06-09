package com.ccpc.yeprogress.service;

import com.ccpc.yeprogress.dto.CommentsUserDTO;
import com.ccpc.yeprogress.exception.UserNotFoundException;
import com.ccpc.yeprogress.exception.UserValidationException;
import com.ccpc.yeprogress.mapper.CommentsUserMapper;
import com.ccpc.yeprogress.model.CommentsUser;
import com.ccpc.yeprogress.model.User;
import com.ccpc.yeprogress.repository.CommentsUserRepository;
import com.ccpc.yeprogress.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentsUserService {

    private static final Logger logger = LoggerFactory.getLogger(CommentsUserService.class);

    private final CommentsUserRepository commentsUserRepository;
    private final UserRepository userRepository;
    private final CommentsUserMapper commentsUserMapper;

    private static final int MAX_CONTENT_LENGTH = 500;

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
        logger.info("Attempting to create comment for user ID: {} by author ID: {}",
                commentsUserDTO.getUserId(), commentsUserDTO.getUserAuthorId());

        try {
            validateCommentDTO(commentsUserDTO);

            User user = userRepository.findById(commentsUserDTO.getUserId())
                    .orElseThrow(() -> {
                        logger.warn("User with ID {} not found", commentsUserDTO.getUserId());
                        return new UserNotFoundException("User with ID " + commentsUserDTO.getUserId() + " not found");
                    });

            User userAuthor = userRepository.findById(commentsUserDTO.getUserAuthorId())
                    .orElseThrow(() -> {
                        logger.warn("User author with ID {} not found", commentsUserDTO.getUserAuthorId());
                        return new UserNotFoundException("User author with ID " + commentsUserDTO.getUserAuthorId() + " not found");
                    });

            CommentsUser commentsUser = commentsUserMapper.toEntity(commentsUserDTO);
            commentsUser.setUser(user);
            commentsUser.setUserAuthor(userAuthor);
            CommentsUser savedComment = commentsUserRepository.save(commentsUser);
            logger.info("Successfully created comment with ID: {}", savedComment.getCommentId());

            return commentsUserMapper.toDto(savedComment);

        } catch (UserValidationException | UserNotFoundException e) {
            logger.error("Error creating comment: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during comment creation: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create comment due to unexpected error", e);
        }
    }

    public CommentsUserDTO getCommentById(Long id) {
        logger.info("Retrieving comment with ID: {}", id);

        try {
            CommentsUser commentsUser = commentsUserRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Comment with ID {} not found", id);
                        return new UserNotFoundException("Comment with ID " + id + " not found");
                    });

            logger.debug("Successfully retrieved comment with ID: {}", id);
            return commentsUserMapper.toDto(commentsUser);

        } catch (UserNotFoundException e) {
            logger.error("Error retrieving comment: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error retrieving comment: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve comment due to unexpected error", e);
        }
    }

    public List<CommentsUserDTO> getAllComments() {
        logger.info("Retrieving all comments");

        try {
            List<CommentsUser> comments = commentsUserRepository.findAll();
            logger.debug("Retrieved {} comments", comments.size());

            return comments.stream()
                    .map(commentsUserMapper::toDto)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logger.error("Unexpected error retrieving all comments: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve comments due to unexpected error", e);
        }
    }

    @Transactional
    public CommentsUserDTO updateComment(Long id, CommentsUserDTO commentsUserDTO) {
        logger.info("Attempting to update comment with ID: {}", id);

        try {
            validateCommentDTO(commentsUserDTO);

            CommentsUser commentsUser = commentsUserRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Comment with ID {} not found", id);
                        return new UserNotFoundException("Comment with ID " + id + " not found");
                    });

            User user = userRepository.findById(commentsUserDTO.getUserId())
                    .orElseThrow(() -> {
                        logger.warn("User with ID {} not found", commentsUserDTO.getUserId());
                        return new UserNotFoundException("User with ID " + commentsUserDTO.getUserId() + " not found");
                    });

            User userAuthor = userRepository.findById(commentsUserDTO.getUserAuthorId())
                    .orElseThrow(() -> {
                        logger.warn("User author with ID {} not found", commentsUserDTO.getUserAuthorId());
                        return new UserNotFoundException("User author with ID " + commentsUserDTO.getUserAuthorId() + " not found");
                    });

            commentsUserMapper.updateEntityFromDto(commentsUserDTO, commentsUser);
            commentsUser.setUser(user);
            commentsUser.setUserAuthor(userAuthor);
            CommentsUser savedComment = commentsUserRepository.save(commentsUser);
            logger.info("Successfully updated comment with ID: {}", id);

            return commentsUserMapper.toDto(savedComment);

        } catch (UserValidationException | UserNotFoundException e) {
            logger.error("Error updating comment: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during comment update: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update comment due to unexpected error", e);
        }
    }

    @Transactional
    public void deleteComment(Long id) {
        logger.info("Attempting to delete comment with ID: {}", id);

        try {
            if (!commentsUserRepository.existsById(id)) {
                logger.warn("Delete failed: Comment with ID {} not found", id);
                throw new UserNotFoundException("Comment with ID " + id + " not found");
            }

            commentsUserRepository.deleteById(id);
            logger.info("Successfully deleted comment with ID: {}", id);

        } catch (UserNotFoundException e) {
            logger.error("Error deleting comment: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during comment deletion: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete comment due to unexpected error", e);
        }
    }

    private void validateCommentDTO(CommentsUserDTO commentsUserDTO) {
        logger.debug("Validating comment DTO");

        if (commentsUserDTO.getUserId() == null) {
            logger.warn("Validation failed: User ID is required");
            throw new UserValidationException("User ID is required");
        }

        if (commentsUserDTO.getUserAuthorId() == null) {
            logger.warn("Validation failed: User author ID is required");
            throw new UserValidationException("User author ID is required");
        }

        if (!StringUtils.hasText(commentsUserDTO.getContent())) {
            logger.warn("Validation failed: Comment content is required");
            throw new UserValidationException("Comment content is required");
        }

        if (commentsUserDTO.getContent().length() > MAX_CONTENT_LENGTH) {
            logger.warn("Validation failed: Comment content exceeds {} characters", MAX_CONTENT_LENGTH);
            throw new UserValidationException(
                    "Comment content must not exceed " + MAX_CONTENT_LENGTH + " characters");
        }
    }
}