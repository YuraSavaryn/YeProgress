package com.ccpc.yeprogress.restController;

import com.ccpc.yeprogress.dto.CampaignImagesDTO;
import com.ccpc.yeprogress.service.CampaignImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campaign-images")
public class CampaignImageController {

    @Autowired
    private CampaignImageService campaignImageService;

    @PostMapping
    public ResponseEntity<CampaignImagesDTO> createCampaignImage(@RequestBody CampaignImagesDTO campaignImagesDTO) {
        CampaignImagesDTO createdCampaignImage = campaignImageService.createCampaignImage(campaignImagesDTO);
        return ResponseEntity.ok(createdCampaignImage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CampaignImagesDTO> getCampaignImageById(@PathVariable Long id) {
        CampaignImagesDTO campaignImagesDTO = campaignImageService.getCampaignImageById(id);
        return ResponseEntity.ok(campaignImagesDTO);
    }

    @GetMapping
    public ResponseEntity<List<CampaignImagesDTO>> getAllCampaignImages() {
        List<CampaignImagesDTO> campaignImages = campaignImageService.getAllCampaignImages();
        return ResponseEntity.ok(campaignImages);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CampaignImagesDTO> updateCampaignImage(@PathVariable Long id, @RequestBody CampaignImagesDTO campaignImagesDTO) {
        CampaignImagesDTO updatedCampaignImage = campaignImageService.updateCampaignImage(id, campaignImagesDTO);
        return ResponseEntity.ok(updatedCampaignImage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCampaignImage(@PathVariable Long id) {
        campaignImageService.deleteCampaignImage(id);
        return ResponseEntity.noContent().build();
    }
}