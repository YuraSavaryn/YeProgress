package com.ccpc.yeprogress.repository;

import com.ccpc.yeprogress.model.CampaignStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignStatusRepository extends JpaRepository<CampaignStatus, Long> {
}
