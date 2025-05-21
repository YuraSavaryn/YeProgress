package com.ccpc.yeprogress.service;

import com.ccpc.yeprogress.dto.CampaignStatusDTO;
import com.ccpc.yeprogress.mapper.CampaignStatusMapper;
import com.ccpc.yeprogress.model.CampaignStatus;
import com.ccpc.yeprogress.repository.CampaignStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CampaignStatusService {
    private final CampaignStatusRepository campaignStatusRepository;
    private final CampaignStatusMapper campaignStatusMapper;

    @Autowired
    public CampaignStatusService(CampaignStatusRepository campaignStatusRepository, CampaignStatusMapper campaignStatusMapper) {
        this.campaignStatusRepository = campaignStatusRepository;
        this.campaignStatusMapper = campaignStatusMapper;
    }

    public CampaignStatusDTO createCampaignStatus(CampaignStatusDTO campaignStatusDTO) {
        CampaignStatus campaignStatus = campaignStatusMapper.toEntity(campaignStatusDTO);
        CampaignStatus savedCampaignStatus = campaignStatusRepository.save(campaignStatus);
        return campaignStatusMapper.toDto(savedCampaignStatus);
    }

    public CampaignStatusDTO getCampaignStatusById(Long id) {
        CampaignStatus campaignStatus = campaignStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CampaignStatus not found"));
        return campaignStatusMapper.toDto(campaignStatus);
    }

    public List<CampaignStatusDTO> getAllCampaignStatuses() {
        return campaignStatusRepository.findAll().stream()
                .map(campaignStatusMapper::toDto)
                .collect(Collectors.toList());
    }

    public CampaignStatusDTO updateCampaignStatus(Long id, CampaignStatusDTO campaignStatusDTO) {
        CampaignStatus campaignStatus = campaignStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CampaignStatus not found"));
        campaignStatusMapper.updateEntityFromDto(campaignStatusDTO, campaignStatus);
        CampaignStatus savedCampaignStatus = campaignStatusRepository.save(campaignStatus);
        return campaignStatusMapper.toDto(savedCampaignStatus);
    }

    public void deleteCampaignStatus(Long id) {
        campaignStatusRepository.deleteById(id);
    }
}