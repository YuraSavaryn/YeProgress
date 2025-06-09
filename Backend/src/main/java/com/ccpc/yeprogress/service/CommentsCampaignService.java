package com.ccpc.yeprogress.service;

import com.ccpc.yeprogress.dto.CommentsCampaignDTO;
import com.ccpc.yeprogress.exception.UserNotFoundException;
import com.ccpc.yeprogress.exception.UserValidationException;
import com.ccpc.yeprogress.logger.LoggerService;
import com.ccpc.yeprogress.mapper.CommentsCampaignMapper;
import com.ccpc.yeprogress.model.CommentsCampaign;
import com.ccpc.yeprogress.model.User;
import com.ccpc.yeprogress.model.Campaign;
import com.ccpc.yeprogress.repository.CommentsCampaignRepository;
import com.ccpc.yeprogress.repository.UserRepository;
import com.ccpc.yeprogress.repository.CampaignRepository;
import com.ccpc.yeprogress.validation.ValidationService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentsCampaignService {

    private static final Logger logger = LoggerService.getLogger(CommentsCampaignService.class);

    private final CommentsCampaignRepository commentsCampaignRepository;
    private final UserRepository userRepository;
    private final CampaignRepository campaignRepository;
    private final CommentsCampaignMapper commentsCampaignMapper;
    private final ValidationService validationService;

    @Autowired
    public CommentsCampaignService(CommentsCampaignRepository commentsCampaignRepository,
                                   UserRepository userRepository,
                                   CampaignRepository campaignRepository,
                                   CommentsCampaignMapper commentsCampaignMapper,
                                   ValidationService validationService) {
        this.commentsCampaignRepository = commentsCampaignRepository;
        this.userRepository = userRepository;
        this.campaignRepository = campaignRepository;
        this.commentsCampaignMapper = commentsCampaignMapper;
        this.validationService = validationService;
    }

    @Transactional
    public CommentsCampaignDTO createComment(CommentsCampaignDTO commentsCampaignDTO) {
        LoggerService.logCreateAttempt(logger, "CampaignComment", commentsCampaignDTO.getUserId());

        try {
            validationService.validateCommentDTO(commentsCampaignDTO);

            User user = userRepository.findById(commentsCampaignDTO.getUserId())
                    .orElseThrow(() -> {
                        LoggerService.logEntityNotFound(logger, "User", commentsCampaignDTO.getUserId());
                        return new UserNotFoundException("User with ID " + commentsCampaignDTO.getUserId() + " not found");
                    });

            Campaign campaign = campaignRepository.findById(commentsCampaignDTO.getCampaignId())
                    .orElseThrow(() -> {
                        LoggerService.logEntityNotFound(logger, "Campaign", commentsCampaignDTO.getCampaignId());
                        return new UserNotFoundException("Campaign with ID " + commentsCampaignDTO.getCampaignId() + " not found");
                    });

            CommentsCampaign commentsCampaign = commentsCampaignMapper.toEntity(commentsCampaignDTO);
            commentsCampaign.setUser(user);
            commentsCampaign.setCampaign(campaign);
            CommentsCampaign savedComment = commentsCampaignRepository.save(commentsCampaign);
            LoggerService.logCreateSuccess(logger, "CampaignComment", savedComment.getCommentId());

            return commentsCampaignMapper.toDto(savedComment);

        } catch (UserValidationException | UserNotFoundException e) {
            LoggerService.logError(logger, "Error creating campaign comment: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "campaign comment creation", e.getMessage(), e);
            throw new RuntimeException("Failed to create campaign comment due to unexpected error", e);
        }
    }

    public CommentsCampaignDTO getCommentById(Long id) {
        LoggerService.logRetrieveAttempt(logger, "CampaignComment", id);

        try {
            CommentsCampaign commentsCampaign = commentsCampaignRepository.findById(id)
                    .orElseThrow(() -> {
                        LoggerService.logEntityNotFound(logger, "CampaignComment", id);
                        return new UserNotFoundException("Comment with ID " + id + " not found");
                    });

            LoggerService.logRetrieveSuccess(logger, "CampaignComment", id);
            return commentsCampaignMapper.toDto(commentsCampaign);

        } catch (UserNotFoundException e) {
            LoggerService.logError(logger, "Error retrieving campaign comment: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "campaign comment retrieval", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve campaign comment due to unexpected error", e);
        }
    }

    public List<CommentsCampaignDTO> getAllComments() {
        LoggerService.logRetrieveAttempt(logger, "All CampaignComments", "all");

        try {
            List<CommentsCampaign> comments = commentsCampaignRepository.findAll();
            LoggerService.logRetrieveSuccess(logger, "All CampaignComments", comments.size());

            return comments.stream()
                    .map(commentsCampaignMapper::toDto)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "retrieval of all campaign comments", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve campaign comments due to unexpected error", e);
        }
    }

    @Transactional
    public CommentsCampaignDTO updateComment(Long id, CommentsCampaignDTO commentsCampaignDTO) {
        LoggerService.logUpdateAttempt(logger, "CampaignComment", id);

        try {
            validationService.validateCommentDTO(commentsCampaignDTO);

            CommentsCampaign commentsCampaign = commentsCampaignRepository.findById(id)
                    .orElseThrow(() -> {
                        LoggerService.logEntityNotFound(logger, "CampaignComment", id);
                        return new UserNotFoundException("Comment with ID " + id + " not found");
                    });

            User user = userRepository.findById(commentsCampaignDTO.getUserId())
                    .orElseThrow(() -> {
                        LoggerService.logEntityNotFound(logger, "User", commentsCampaignDTO.getUserId());
                        return new UserNotFoundException("User with ID " + commentsCampaignDTO.getUserId() + " not found");
                    });

            Campaign campaign = campaignRepository.findById(commentsCampaignDTO.getCampaignId())
                    .orElseThrow(() -> {
                        LoggerService.logEntityNotFound(logger, "Campaign", commentsCampaignDTO.getCampaignId());
                        return new UserNotFoundException("Campaign with ID " + commentsCampaignDTO.getCampaignId() + " not found");
                    });

            commentsCampaignMapper.updateEntityFromDto(commentsCampaignDTO, commentsCampaign);
            commentsCampaign.setUser(user);
            commentsCampaign.setCampaign(campaign);
            CommentsCampaign savedComment = commentsCampaignRepository.save(commentsCampaign);
            LoggerService.logUpdateSuccess(logger, "CampaignComment", id);

            return commentsCampaignMapper.toDto(savedComment);

        } catch (UserValidationException | UserNotFoundException e) {
            LoggerService.logError(logger, "Error updating campaign comment: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "campaign comment update", e.getMessage(), e);
            throw new RuntimeException("Failed to update campaign comment due to unexpected error", e);
        }
    }

    @Transactional
    public void deleteComment(Long id) {
        LoggerService.logDeleteAttempt(logger, "CampaignComment", id);

        try {
            if (!commentsCampaignRepository.existsById(id)) {
                LoggerService.logEntityNotFound(logger, "CampaignComment", id);
                throw new UserNotFoundException("Comment with ID " + id + " not found");
            }

            commentsCampaignRepository.deleteById(id);
            LoggerService.logDeleteSuccess(logger, "CampaignComment", id);

        } catch (UserNotFoundException e) {
            LoggerService.logError(logger, "Error deleting campaign comment: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "campaign comment deletion", e.getMessage(), e);
            throw new RuntimeException("Failed to delete campaign comment due to unexpected error", e);
        }
    }
}