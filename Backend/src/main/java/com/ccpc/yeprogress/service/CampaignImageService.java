package com.ccpc.yeprogress.service;

import com.ccpc.yeprogress.dto.CampaignImagesDTO;
import com.ccpc.yeprogress.exception.UserNotFoundException;
import com.ccpc.yeprogress.exception.UserValidationException;
import com.ccpc.yeprogress.logger.LoggerService;
import com.ccpc.yeprogress.mapper.CampaignImageMapper;
import com.ccpc.yeprogress.model.CampaignImage;
import com.ccpc.yeprogress.repository.CampaignImagesRepository;
import com.ccpc.yeprogress.validation.ValidationService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CampaignImageService {

    private static final Logger logger = LoggerService.getLogger(CampaignImageService.class);

    private final CampaignImagesRepository campaignImagesRepository;
    private final CampaignImageMapper campaignImageMapper;
    private final ValidationService validationService;

    @Autowired
    public CampaignImageService(CampaignImagesRepository campaignImagesRepository,
                                CampaignImageMapper campaignImageMapper,
                                ValidationService validationService) {
        this.campaignImagesRepository = campaignImagesRepository;
        this.campaignImageMapper = campaignImageMapper;
        this.validationService = validationService;
    }

    @Transactional
    public CampaignImagesDTO createCampaignImage(CampaignImagesDTO campaignImagesDTO) {
        LoggerService.logCreateAttempt(logger, "CampaignImage", campaignImagesDTO.getCampaignId());

        try {
            validationService.validateCampaignImageDTO(campaignImagesDTO);

            CampaignImage campaignImage = campaignImageMapper.toEntity(campaignImagesDTO);
            CampaignImage savedCampaignImage = campaignImagesRepository.save(campaignImage);
            LoggerService.logCreateSuccess(logger, "CampaignImage", savedCampaignImage.getCampaignImageId());

            return campaignImageMapper.toDto(savedCampaignImage);

        } catch (UserValidationException e) {
            LoggerService.logError(logger, "Validation failed for campaign image creation: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "campaign image creation", e.getMessage(), e);
            throw new RuntimeException("Failed to create campaign image due to unexpected error", e);
        }
    }

    public CampaignImagesDTO getCampaignImageById(Long id) {
        LoggerService.logRetrieveAttempt(logger, "CampaignImage", id);

        try {
            CampaignImage campaignImage = campaignImagesRepository.findByCampaign_CampaignId(id);
            if (campaignImage == null) {
                LoggerService.logEntityNotFound(logger, "CampaignImage", id);
                throw new UserNotFoundException("Campaign image for campaign ID " + id + " not found");
            }

            LoggerService.logRetrieveSuccess(logger, "CampaignImage", id);
            return campaignImageMapper.toDto(campaignImage);

        } catch (UserNotFoundException e) {
            LoggerService.logError(logger, "Error retrieving campaign image: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "campaign image retrieval", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve campaign image due to unexpected error", e);
        }
    }

    public List<CampaignImagesDTO> getAllCampaignImages() {
        LoggerService.logRetrieveAttempt(logger, "All CampaignImages", "all");

        try {
            List<CampaignImage> campaignImages = campaignImagesRepository.findAll();
            LoggerService.logRetrieveSuccess(logger, "All CampaignImages", campaignImages.size());

            return campaignImages.stream()
                    .map(campaignImageMapper::toDto)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "retrieval of all campaign images", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve campaign images due to unexpected error", e);
        }
    }

    @Transactional
    public CampaignImagesDTO updateCampaignImage(Long id, CampaignImagesDTO campaignImagesDTO) {
        LoggerService.logUpdateAttempt(logger, "CampaignImage", id);

        try {
            validationService.validateCampaignImageDTO(campaignImagesDTO);

            CampaignImage campaignImage = campaignImagesRepository.findById(id)
                    .orElseThrow(() -> {
                        LoggerService.logEntityNotFound(logger, "CampaignImage", id);
                        return new UserNotFoundException("Campaign image with ID " + id + " not found");
                    });

            campaignImageMapper.updateEntityFromDto(campaignImagesDTO, campaignImage);
            CampaignImage savedCampaignImage = campaignImagesRepository.save(campaignImage);
            LoggerService.logUpdateSuccess(logger, "CampaignImage", id);

            return campaignImageMapper.toDto(savedCampaignImage);

        } catch (UserValidationException | UserNotFoundException e) {
            LoggerService.logError(logger, "Error updating campaign image: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "campaign image update", e.getMessage(), e);
            throw new RuntimeException("Failed to update campaign image due to unexpected error", e);
        }
    }

    @Transactional
    public void deleteCampaignImage(Long id) {
        LoggerService.logDeleteAttempt(logger, "CampaignImage", id);

        try {
            if (!campaignImagesRepository.existsById(id)) {
                LoggerService.logEntityNotFound(logger, "CampaignImage", id);
                throw new UserNotFoundException("Campaign image with ID " + id + " not found");
            }

            campaignImagesRepository.deleteById(id);
            LoggerService.logDeleteSuccess(logger, "CampaignImage", id);

        } catch (UserNotFoundException e) {
            LoggerService.logError(logger, "Error deleting campaign image: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "campaign image deletion", e.getMessage(), e);
            throw new RuntimeException("Failed to delete campaign image due to unexpected error", e);
        }
    }
}