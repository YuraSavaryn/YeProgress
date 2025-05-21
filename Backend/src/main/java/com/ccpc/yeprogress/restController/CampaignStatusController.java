package com.ccpc.yeprogress.restController;

import com.ccpc.yeprogress.dto.CampaignStatusDTO;
import com.ccpc.yeprogress.service.CampaignStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campaign-statuses")
public class CampaignStatusController {

    @Autowired
    private CampaignStatusService campaignStatusService;

    @PostMapping
    public ResponseEntity<CampaignStatusDTO> createCampaignStatus(@RequestBody CampaignStatusDTO campaignStatusDTO) {
        CampaignStatusDTO createdCampaignStatus = campaignStatusService.createCampaignStatus(campaignStatusDTO);
        return ResponseEntity.ok(createdCampaignStatus);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CampaignStatusDTO> getCampaignStatusById(@PathVariable Long id) {
        CampaignStatusDTO campaignStatusDTO = campaignStatusService.getCampaignStatusById(id);
        return ResponseEntity.ok(campaignStatusDTO);
    }

    @GetMapping
    public ResponseEntity<List<CampaignStatusDTO>> getAllCampaignStatuses() {
        List<CampaignStatusDTO> campaignStatuses = campaignStatusService.getAllCampaignStatuses();
        return ResponseEntity.ok(campaignStatuses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CampaignStatusDTO> updateCampaignStatus(@PathVariable Long id, @RequestBody CampaignStatusDTO campaignStatusDTO) {
        CampaignStatusDTO updatedCampaignStatus = campaignStatusService.updateCampaignStatus(id, campaignStatusDTO);
        return ResponseEntity.ok(updatedCampaignStatus);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCampaignStatus(@PathVariable Long id) {
        campaignStatusService.deleteCampaignStatus(id);
        return ResponseEntity.noContent().build();
    }
}