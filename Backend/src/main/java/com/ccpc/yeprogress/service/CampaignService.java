package com.ccpc.yeprogress.service;

import com.ccpc.yeprogress.dto.CampaignDTO;
import com.ccpc.yeprogress.mapper.CampaignMapper;
import com.ccpc.yeprogress.model.Campaign;
import com.ccpc.yeprogress.repository.CampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CampaignService {
    private final CampaignRepository campaignRepository;
    private final CampaignMapper campaignMapper;

    @Autowired
    public CampaignService(CampaignRepository campaignRepository, CampaignMapper campaignMapper) {
        this.campaignRepository = campaignRepository;
        this.campaignMapper = campaignMapper;
    }

    public CampaignDTO createCampaign(CampaignDTO campaignDTO) {
        Campaign campaign = campaignMapper.toEntity(campaignDTO);
        Campaign savedCampaign = campaignRepository.save(campaign);
        return campaignMapper.toDto(savedCampaign);
    }

    public CampaignDTO getCampaignById(Long id) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));
        return campaignMapper.toDto(campaign);
    }

    public List<CampaignDTO> getAllCampaigns() {
        return campaignRepository.findAll().stream()
                .map(campaignMapper::toDto)
                .collect(Collectors.toList());
    }

    public CampaignDTO updateCampaign(Long id, CampaignDTO campaignDTO) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));
        campaignMapper.updateEntityFromDto(campaignDTO, campaign);
        Campaign savedCampaign = campaignRepository.save(campaign);
        return campaignMapper.toDto(savedCampaign);
    }

    public void deleteCampaign(Long id) {
        campaignRepository.deleteById(id);
    }
}