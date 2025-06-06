package com.ccpc.yeprogress.service;

import com.ccpc.yeprogress.dto.CampaignDTO;
import com.ccpc.yeprogress.mapper.CampaignMapper;
import com.ccpc.yeprogress.model.Campaign;
import com.ccpc.yeprogress.model.User;
import com.ccpc.yeprogress.repository.CampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CampaignService {
    private final CampaignRepository campaignRepository;
    private final CampaignMapper campaignMapper;
    private final CampaignScrapingService scrapingService;

    @Autowired
    public CampaignService(CampaignRepository campaignRepository,
                           CampaignMapper campaignMapper,
                           CampaignScrapingService scrapingService) {
        this.campaignRepository = campaignRepository;
        this.campaignMapper = campaignMapper;
        this.scrapingService = scrapingService;
    }

    public CampaignDTO createCampaign(User user, CampaignDTO campaignDTO) {
            if (campaignDTO.getBankaUrl() != null && !campaignDTO.getBankaUrl().isEmpty()) {
            boolean exists = campaignRepository.existsByBankaUrl(campaignDTO.getBankaUrl());
            if (exists) {
                throw new RuntimeException("Campaign with the same banka URL already exists");
            }
        }

        Campaign campaign = campaignMapper.toEntity(campaignDTO);
        campaign.setUser(user);
        Campaign savedCampaign = campaignRepository.save(campaign);

        // Якщо є bankaUrl, спробуємо відразу оновити дані
        if (savedCampaign.getBankaUrl() != null && !savedCampaign.getBankaUrl().isEmpty()) {
            scrapingService.updateCampaignAmounts(savedCampaign);
        }

        return campaignMapper.toDto(savedCampaign);
    }

    public CampaignDTO getCampaignById(Long id) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));
        return campaignMapper.toDto(campaign);
    }

    public List<CampaignDTO> getCampaignsByUserId(Long userId) {
        return campaignRepository.findByUser_UserId(userId).stream()
                .map(campaignMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<CampaignDTO> getAllCampaigns() {
        return campaignRepository.findAll().stream()
                .map(campaignMapper::toDto)
                .collect(Collectors.toList());
    }

    public CampaignDTO updateCampaign(Long id, CampaignDTO campaignDTO) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));

        String oldBankaUrl = campaign.getBankaUrl();
        campaignMapper.updateEntityFromDto(campaignDTO, campaign);
        Campaign savedCampaign = campaignRepository.save(campaign);

        // Якщо bankaUrl змінився або додався, оновлюємо дані
        if (savedCampaign.getBankaUrl() != null &&
                !savedCampaign.getBankaUrl().equals(oldBankaUrl)) {
            scrapingService.updateCampaignAmounts(savedCampaign);
        }

        return campaignMapper.toDto(savedCampaign);
    }

    public void deleteCampaign(Long id) {
        campaignRepository.deleteById(id);
    }

    // Новий метод для ручного оновлення даних з банки
    public CampaignDTO refreshCampaignData(Long id) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));

        if (campaign.getBankaUrl() == null || campaign.getBankaUrl().isEmpty()) {
            throw new RuntimeException("Campaign has no banka URL to refresh from");
        }

        boolean updated = scrapingService.updateCampaignAmounts(campaign);

        if (!updated) {
            throw new RuntimeException("Failed to refresh campaign data from banka URL");
        }

        return campaignMapper.toDto(campaign);
    }
}