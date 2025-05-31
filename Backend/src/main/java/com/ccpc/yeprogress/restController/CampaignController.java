package com.ccpc.yeprogress.restController;

import com.ccpc.yeprogress.dto.CampaignDTO;
import com.ccpc.yeprogress.model.User;
import com.ccpc.yeprogress.repository.UserRepository;
import com.ccpc.yeprogress.service.CampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {
    private final CampaignService campaignService;
    private final UserRepository userRepository;

    @Autowired
    public CampaignController(CampaignService campaignService, UserRepository userRepository) {
        this.campaignService = campaignService;
        this.userRepository = userRepository;
    }

    @PostMapping("/{uid}")
    public ResponseEntity<CampaignDTO> createCampaign(@PathVariable String uid, @RequestBody CampaignDTO campaignDTO) {
        User user = userRepository.findByFirebaseId(uid)
                .orElseThrow(() -> new RuntimeException("User not found"));
        CampaignDTO createdCampaign = campaignService.createCampaign(user, campaignDTO);
        return ResponseEntity.ok(createdCampaign);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CampaignDTO> getCampaignById(@PathVariable Long id) {
        CampaignDTO campaignDTO = campaignService.getCampaignById(id);
        return ResponseEntity.ok(campaignDTO);
    }

    @GetMapping("/user/{uid}")
    public ResponseEntity<List<CampaignDTO>> getUserCampaigns(@PathVariable String uid) {
        User user = userRepository.findByFirebaseId(uid)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<CampaignDTO> campaigns = campaignService.getCampaignsByUserId(user.getUserId());
        return ResponseEntity.ok(campaigns);
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

    @PutMapping("/{id}/refresh")
    public ResponseEntity<CampaignDTO> refreshCampaignData(@PathVariable Long id) {
        try {
            CampaignDTO refreshedCampaign = campaignService.refreshCampaignData(id);
            return ResponseEntity.ok(refreshedCampaign);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Ендпоінт для отримання статусу оновлення
    @GetMapping("/{id}/refresh-status")
    public ResponseEntity<Map<String, Object>> getRefreshStatus(@PathVariable Long id) {
        try {
            CampaignDTO campaign = campaignService.getCampaignById(id);
            Map<String, Object> status = Map.of(
                    "canRefresh", campaign.getBankaUrl() != null && !campaign.getBankaUrl().isEmpty(),
                    "bankaUrl", campaign.getBankaUrl() != null ? campaign.getBankaUrl() : "",
                    "currentAmount", campaign.getCurrentAmount() != null ? campaign.getCurrentAmount() : 0,
                    "goalAmount", campaign.getGoalAmount() != null ? campaign.getGoalAmount() : 0
            );
            return ResponseEntity.ok(status);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}