package com.ccpc.yeprogress.service;

import com.ccpc.yeprogress.dto.CampaignImagesDTO;
import com.ccpc.yeprogress.mapper.CampaignImageMapper;
import com.ccpc.yeprogress.model.CampaignImage;
import com.ccpc.yeprogress.repository.CampaignImagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CampaignImageService {
    private final CampaignImagesRepository campaignImagesRepository;
    private final CampaignImageMapper campaignImageMapper;

    @Autowired
    public CampaignImageService(CampaignImagesRepository campaignImagesRepository, CampaignImageMapper campaignImageMapper) {
        this.campaignImagesRepository = campaignImagesRepository;
        this.campaignImageMapper = campaignImageMapper;
    }

    public CampaignImagesDTO createCampaignImage(CampaignImagesDTO campaignImagesDTO) {
        CampaignImage campaignImage = campaignImageMapper.toEntity(campaignImagesDTO);
        CampaignImage savedCampaignImage = campaignImagesRepository.save(campaignImage);
        return campaignImageMapper.toDto(savedCampaignImage);
    }

    public CampaignImagesDTO getCampaignImageById(Long id) {
        CampaignImage campaignImage = campaignImagesRepository.findByCampaign_CampaignId(id);
        return campaignImageMapper.toDto(campaignImage);
    }

    public List<CampaignImagesDTO> getAllCampaignImages() {
        return campaignImagesRepository.findAll().stream()
                .map(campaignImageMapper::toDto)
                .collect(Collectors.toList());
    }

    public CampaignImagesDTO updateCampaignImage(Long id, CampaignImagesDTO campaignImagesDTO) {
        CampaignImage campaignImage = campaignImagesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CampaignImage not found"));
        campaignImageMapper.updateEntityFromDto(campaignImagesDTO, campaignImage);
        CampaignImage savedCampaignImage = campaignImagesRepository.save(campaignImage);
        return campaignImageMapper.toDto(savedCampaignImage);
    }

    public void deleteCampaignImage(Long id) {
        campaignImagesRepository.deleteById(id);
    }
}