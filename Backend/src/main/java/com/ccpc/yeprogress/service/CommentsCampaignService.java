package com.ccpc.yeprogress.service;

import com.ccpc.yeprogress.dto.CommentsCampaignDTO;
import com.ccpc.yeprogress.mapper.CommentsCampaignMapper;
import com.ccpc.yeprogress.model.CommentsCampaign;
import com.ccpc.yeprogress.model.User;
import com.ccpc.yeprogress.model.Campaign;
import com.ccpc.yeprogress.repository.CommentsCampaignRepository;
import com.ccpc.yeprogress.repository.UserRepository;
import com.ccpc.yeprogress.repository.CampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentsCampaignService {
    private final CommentsCampaignRepository commentsCampaignRepository;
    private final UserRepository userRepository;
    private final CampaignRepository campaignRepository;
    private final CommentsCampaignMapper commentsCampaignMapper;

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

    public CommentsCampaignDTO createComment(CommentsCampaignDTO commentsCampaignDTO) {
        User user = userRepository.findById(commentsCampaignDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + commentsCampaignDTO.getUserId()));
        Campaign campaign = campaignRepository.findById(commentsCampaignDTO.getCampaignId())
                .orElseThrow(() -> new RuntimeException("Campaign not found with id: " + commentsCampaignDTO.getCampaignId()));

        CommentsCampaign commentsCampaign = commentsCampaignMapper.toEntity(commentsCampaignDTO);
        commentsCampaign.setUser(user);
        commentsCampaign.setCampaign(campaign);
        CommentsCampaign savedComment = commentsCampaignRepository.save(commentsCampaign);
        return commentsCampaignMapper.toDto(savedComment);
    }

    public CommentsCampaignDTO getCommentById(Long id) {
        CommentsCampaign commentsCampaign = commentsCampaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));
        return commentsCampaignMapper.toDto(commentsCampaign);
    }

    public List<CommentsCampaignDTO> getAllComments() {
        return commentsCampaignRepository.findAll().stream()
                .map(commentsCampaignMapper::toDto)
                .collect(Collectors.toList());
    }

    public CommentsCampaignDTO updateComment(Long id, CommentsCampaignDTO commentsCampaignDTO) {
        CommentsCampaign commentsCampaign = commentsCampaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));

        User user = userRepository.findById(commentsCampaignDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + commentsCampaignDTO.getUserId()));
        Campaign campaign = campaignRepository.findById(commentsCampaignDTO.getCampaignId())
                .orElseThrow(() -> new RuntimeException("Campaign not found with id: " + commentsCampaignDTO.getCampaignId()));

        commentsCampaignMapper.updateEntityFromDto(commentsCampaignDTO, commentsCampaign);
        commentsCampaign.setUser(user);
        commentsCampaign.setCampaign(campaign);
        CommentsCampaign savedComment = commentsCampaignRepository.save(commentsCampaign);
        return commentsCampaignMapper.toDto(savedComment);
    }

    @Transactional
    public void deleteComment(Long id) {
        commentsCampaignRepository.deleteById(id);
    }
}