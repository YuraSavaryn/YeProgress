package com.ccpc.yeprogress.service;

import com.ccpc.yeprogress.dto.CommentsCampaignDTO;
import com.ccpc.yeprogress.exception.UserNotFoundException;
import com.ccpc.yeprogress.exception.UserValidationException;
import com.ccpc.yeprogress.mapper.CommentsCampaignMapper;
import com.ccpc.yeprogress.model.CommentsCampaign;
import com.ccpc.yeprogress.model.User;
import com.ccpc.yeprogress.model.Campaign;
import com.ccpc.yeprogress.repository.CommentsCampaignRepository;
import com.ccpc.yeprogress.repository.UserRepository;
import com.ccpc.yeprogress.repository.CampaignRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentsCampaignService {

    private static final Logger logger = LoggerFactory.getLogger(CommentsCampaignService.class);

    private final CommentsCampaignRepository commentsCampaignRepository;
    private final UserRepository userRepository;
    private final CampaignRepository campaignRepository;
    private final CommentsCampaignMapper commentsCampaignMapper;

    private static final int MAX_CONTENT_LENGTH = 500;

    @Autowired
    public CommentsCampaignService(CommentsCampaignRepository commentsCampaignRepository,
                                   UserRepository userRepository,
                                   CampaignRepository campaignRepository,
                                   CommentsCampaignMapper commentsCampaignMapper) {
        this.commentsCampaignRepository = commentsCampaignRepository;
        this.userRepository = userRepository;
        this.campaignRepository = campaignRepository;
        this.commentsCampaignMapper = commentsCampaignMapper;
    }

    @Transactional
    public CommentsCampaignDTO createComment(CommentsCampaignDTO commentsCampaignDTO) {
        logger.info("Attempting to create comment for user ID: {} and campaign ID: {}",
                commentsCampaignDTO.getUserId(), commentsCampaignDTO.getCampaignId());

        try {
            validateCommentDTO(commentsCampaignDTO);

            User user = userRepository.findById(commentsCampaignDTO.getUserId())
                    .orElseThrow(() -> {
                        logger.warn("User with ID {} not found", commentsCampaignDTO.getUserId());
                        return new UserNotFoundException("User with ID " + commentsCampaignDTO.getUserId() + " not found");
                    });

            Campaign campaign = campaignRepository.findById(commentsCampaignDTO.getCampaignId())
                    .orElseThrow(() -> {
                        logger.warn("Campaign with ID {} not found", commentsCampaignDTO.getCampaignId());
                        return new UserNotFoundException("Campaign with ID " + commentsCampaignDTO.getCampaignId() + " not found");
                    });

            CommentsCampaign commentsCampaign = commentsCampaignMapper.toEntity(commentsCampaignDTO);
            commentsCampaign.setUser(user);
            commentsCampaign.setCampaign(campaign);
            CommentsCampaign savedComment = commentsCampaignRepository.save(commentsCampaign);
            logger.info("Successfully created comment with ID: {}", savedComment.getCommentId());

            return commentsCampaignMapper.toDto(savedComment);

        } catch (UserValidationException | UserNotFoundException e) {
            logger.error("Error creating comment: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during comment creation: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create comment due to unexpected error", e);
        }
    }

    public CommentsCampaignDTO getCommentById(Long id) {
        logger.info("Retrieving comment with ID: {}", id);

        try {
            CommentsCampaign commentsCampaign = commentsCampaignRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Comment with ID {} not found", id);
                        return new UserNotFoundException("Comment with ID " + id + " not found");
                    });

            logger.debug("Successfully retrieved comment with ID: {}", id);
            return commentsCampaignMapper.toDto(commentsCampaign);

        } catch (UserNotFoundException e) {
            logger.error("Error retrieving comment: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error retrieving comment: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve comment due to unexpected error", e);
        }
    }

    public List<CommentsCampaignDTO> getAllComments() {
        logger.info("Retrieving all comments");

        try {
            List<CommentsCampaign> comments = commentsCampaignRepository.findAll();
            logger.debug("Retrieved {} comments", comments.size());

            return comments.stream()
                    .map(commentsCampaignMapper::toDto)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logger.error("Unexpected error retrieving all comments: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve comments due to unexpected error", e);
        }
    }

    @Transactional
    public CommentsCampaignDTO updateComment(Long id, CommentsCampaignDTO commentsCampaignDTO) {
        logger.info("Attempting to update comment with ID: {}", id);

        try {
            validateCommentDTO(commentsCampaignDTO);

            CommentsCampaign commentsCampaign = commentsCampaignRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Comment with ID {} not found", id);
                        return new UserNotFoundException("Comment with ID " + id + " not found");
                    });

            User user = userRepository.findById(commentsCampaignDTO.getUserId())
                    .orElseThrow(() -> {
                        logger.warn("User with ID {} not found", commentsCampaignDTO.getUserId());
                        return new UserNotFoundException("User with ID " + commentsCampaignDTO.getUserId() + " not found");
                    });

            Campaign campaign = campaignRepository.findById(commentsCampaignDTO.getCampaignId())
                    .orElseThrow(() -> {
                        logger.warn("Campaign with ID {} not found", commentsCampaignDTO.getCampaignId());
                        return new UserNotFoundException("Campaign with ID " + commentsCampaignDTO.getCampaignId() + " not found");
                    });

            commentsCampaignMapper.updateEntityFromDto(commentsCampaignDTO, commentsCampaign);
            commentsCampaign.setUser(user);
            commentsCampaign.setCampaign(campaign);
            CommentsCampaign savedComment = commentsCampaignRepository.save(commentsCampaign);
            logger.info("Successfully updated comment with ID: {}", id);

            return commentsCampaignMapper.toDto(savedComment);

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
            if (!commentsCampaignRepository.existsById(id)) {
                logger.warn("Delete failed: Comment with ID {} not found", id);
                throw new UserNotFoundException("Comment with ID " + id + " not found");
            }

            commentsCampaignRepository.deleteById(id);
            logger.info("Successfully deleted comment with ID: {}", id);

        } catch (UserNotFoundException e) {
            logger.error("Error deleting comment: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during comment deletion: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete comment due to unexpected error", e);
        }
    }

    private void validateCommentDTO(CommentsCampaignDTO commentsCampaignDTO) {
        logger.debug("Validating comment DTO");

        if (commentsCampaignDTO.getUserId() == null) {
            logger.warn("Validation failed: User ID is required");
            throw new UserValidationException("User ID is required");
        }

        if (commentsCampaignDTO.getCampaignId() == null) {
            logger.warn("Validation failed: Campaign ID is required");
            throw new UserValidationException("Campaign ID is required");
        }

        if (!StringUtils.hasText(commentsCampaignDTO.getContent())) {
            logger.warn("Validation failed: Comment content is required");
            throw new UserValidationException("Comment content is required");
        }

        if (commentsCampaignDTO.getContent().length() > MAX_CONTENT_LENGTH) {
            logger.warn("Validation failed: Comment content exceeds {} characters", MAX_CONTENT_LENGTH);
            throw new UserValidationException(
                    "Comment content must not exceed " + MAX_CONTENT_LENGTH + " characters");
        }
    }
}