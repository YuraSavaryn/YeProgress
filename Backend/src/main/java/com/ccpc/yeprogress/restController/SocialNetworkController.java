package com.ccpc.yeprogress.restController;

import com.ccpc.yeprogress.dto.SocialNetworkDTO;
import com.ccpc.yeprogress.service.SocialNetworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/social-networks")
public class SocialNetworkController {

    private final SocialNetworkService socialNetworkService;

    @Autowired
    public SocialNetworkController(SocialNetworkService socialNetworkService) {
        this.socialNetworkService = socialNetworkService;
    }

    @PostMapping
    public ResponseEntity<SocialNetworkDTO> createSocialNetwork(@RequestBody SocialNetworkDTO socialNetworkDTO) {
        SocialNetworkDTO createdSocialNetwork = socialNetworkService.createSocialNetwork(socialNetworkDTO);
        return ResponseEntity.ok(createdSocialNetwork);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SocialNetworkDTO> getSocialNetworkById(@PathVariable Long id) {
        SocialNetworkDTO socialNetworkDTO = socialNetworkService.getSocialNetworkById(id);
        return ResponseEntity.ok(socialNetworkDTO);
    }

    @GetMapping
    public ResponseEntity<List<SocialNetworkDTO>> getAllSocialNetworks() {
        List<SocialNetworkDTO> socialNetworks = socialNetworkService.getAllSocialNetworks();
        return ResponseEntity.ok(socialNetworks);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SocialNetworkDTO> updateSocialNetwork(@PathVariable Long id, @RequestBody SocialNetworkDTO socialNetworkDTO) {
        SocialNetworkDTO updatedSocialNetwork = socialNetworkService.updateSocialNetwork(id, socialNetworkDTO);
        return ResponseEntity.ok(updatedSocialNetwork);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSocialNetwork(@PathVariable Long id) {
        socialNetworkService.deleteSocialNetwork(id);
        return ResponseEntity.noContent().build();
    }
}