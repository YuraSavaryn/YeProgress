package com.ccpc.yeprogress.restController;

import com.ccpc.yeprogress.dto.CampaignDTO;
import com.ccpc.yeprogress.service.CampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {

    @Autowired
    private CampaignService campaignService;

    @PostMapping
    public ResponseEntity<CampaignDTO> createCampaign(@RequestBody CampaignDTO campaignDTO) {
        CampaignDTO createdCampaign = campaignService.createCampaign(campaignDTO);
        return ResponseEntity.ok(createdCampaign);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CampaignDTO> getCampaignById(@PathVariable Long id) {
        CampaignDTO campaignDTO = campaignService.getCampaignById(id);
        return ResponseEntity.ok(campaignDTO);
    }

    @GetMapping
    public ResponseEntity<List<CampaignDTO>> getAllCampaigns() {
        List<CampaignDTO> campaigns = campaignService.getAllCampaigns();
        return ResponseEntity.ok(campaigns);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CampaignDTO> updateCampaign(@PathVariable Long id, @RequestBody CampaignDTO campaignDTO) {
        CampaignDTO updatedCampaign = campaignService.updateCampaign(id, campaignDTO);
        return ResponseEntity.ok(updatedCampaign);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCampaign(@PathVariable Long id) {
        campaignService.deleteCampaign(id);
        return ResponseEntity.noContent().build();
    }
}