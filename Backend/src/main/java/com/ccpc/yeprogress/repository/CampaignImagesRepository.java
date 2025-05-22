package com.ccpc.yeprogress.repository;

import com.ccpc.yeprogress.model.CampaignImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignImagesRepository extends JpaRepository<CampaignImage, Long> {
}