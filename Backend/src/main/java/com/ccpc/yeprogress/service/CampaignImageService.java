package com.ccpc.yeprogress.service;

import com.ccpc.yeprogress.dto.CampaignImagesDTO;
import com.ccpc.yeprogress.exception.UserNotFoundException;
import com.ccpc.yeprogress.exception.UserValidationException;
import com.ccpc.yeprogress.mapper.CampaignImageMapper;
import com.ccpc.yeprogress.model.CampaignImage;
import com.ccpc.yeprogress.repository.CampaignImagesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CampaignImageService {

    private static final Logger logger = LoggerFactory.getLogger(CampaignImageService.class);

    private final CampaignImagesRepository campaignImagesRepository;
    private final CampaignImageMapper campaignImageMapper;

    @Autowired
    public CampaignImageService(CampaignImagesRepository campaignImagesRepository,
                                CampaignImageMapper campaignImageMapper) {
        this.campaignImagesRepository = campaignImagesRepository;
        this.campaignImageMapper = campaignImageMapper;
    }

    public CampaignImagesDTO createCampaignImage(CampaignImagesDTO campaignImagesDTO) {
        logger.info("Attempting to create campaign image for campaign ID: {}",
                campaignImagesDTO.getCampaignId());

        try {
            validateCampaignImageDTO(campaignImagesDTO);

            CampaignImage campaignImage = campaignImageMapper.toEntity(campaignImagesDTO);
            CampaignImage savedCampaignImage = campaignImagesRepository.save(campaignImage);
            logger.info("Successfully created campaign image with ID: {} for campaign ID: {}",
                    savedCampaignImage.getCampaignImageId(), campaignImagesDTO.getCampaignId());

            return campaignImageMapper.toDto(savedCampaignImage);

        } catch (UserValidationException e) {
            logger.error("Validation failed for campaign image creation: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during campaign image creation: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create campaign image due to unexpected error", e);
        }
    }

    public CampaignImagesDTO getCampaignImageById(Long id) {
        logger.info("Retrieving campaign image for campaign ID: {}", id);

        try {
            CampaignImage campaignImage = campaignImagesRepository.findByCampaign_CampaignId(id);
            if (campaignImage == null) {
                logger.warn("Campaign image for campaign ID {} not found", id);
                throw new UserNotFoundException("Campaign image for campaign ID " + id + " not found");
            }

            logger.debug("Successfully retrieved campaign image for campaign ID: {}", id);
            return campaignImageMapper.toDto(campaignImage);

        } catch (UserNotFoundException e) {
            logger.error("Error retrieving campaign image: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error retrieving campaign image: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve campaign image due to unexpected error", e);
        }
    }

    public List<CampaignImagesDTO> getAllCampaignImages() {
        logger.info("Retrieving all campaign images");

        try {
            List<CampaignImage> campaignImages = campaignImagesRepository.findAll();
            logger.debug("Retrieved {} campaign images", campaignImages.size());

            return campaignImages.stream()
                    .map(campaignImageMapper::toDto)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logger.error("Unexpected error retrieving all campaign images: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve campaign images due to unexpected error", e);
        }
    }

    public CampaignImagesDTO updateCampaignImage(Long id, CampaignImagesDTO campaignImagesDTO) {
        logger.info("Attempting to update campaign image with ID: {}", id);

        try {
            validateCampaignImageDTO(campaignImagesDTO);

            CampaignImage campaignImage = campaignImagesRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Campaign image with ID {} not found", id);
                        return new UserNotFoundException("Campaign image with ID " + id + " not found");
                    });

            campaignImageMapper.updateEntityFromDto(campaignImagesDTO, campaignImage);
            CampaignImage savedCampaignImage = campaignImagesRepository.save(campaignImage);
            logger.info("Successfully updated campaign image with ID: {}", id);

            return campaignImageMapper.toDto(savedCampaignImage);

        } catch (UserValidationException | UserNotFoundException e) {
            logger.error("Error updating campaign image: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during campaign image update: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update campaign image due to unexpected error", e);
        }
    }

    public void deleteCampaignImage(Long id) {
        logger.info("Attempting to delete campaign image with ID: {}", id);

        try {
            if (!campaignImagesRepository.existsById(id)) {
                logger.warn("Campaign image deletion failed: ID {} not found", id);
                throw new UserNotFoundException("Campaign image with ID " + id + " not found");
            }

            campaignImagesRepository.deleteById(id);
            logger.info("Successfully deleted campaign image with ID: {}", id);

        } catch (UserNotFoundException e) {
            logger.error("Error deleting campaign image: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during campaign image deletion: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete campaign image due to unexpected error", e);
        }
    }

    private void validateCampaignImageDTO(CampaignImagesDTO campaignImagesDTO) {
        logger.debug("Validating campaign image DTO");

        if (campaignImagesDTO.getCampaignId() == null) {
            logger.warn("Validation failed: Campaign ID is required");
            throw new UserValidationException("Campaign ID is required");
        }

        if (!StringUtils.hasText(campaignImagesDTO.getImgUrl())) {
            logger.warn("Validation failed: Image URL is required");
            throw new UserValidationException("Image URL is required");
        }

        if (!isValidUrl(campaignImagesDTO.getImgUrl())) {
            logger.warn("Validation failed: Invalid image URL format: {}",
                    campaignImagesDTO.getImgUrl());
            throw new UserValidationException("Invalid image URL format");
        }
    }


    private boolean isValidUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}