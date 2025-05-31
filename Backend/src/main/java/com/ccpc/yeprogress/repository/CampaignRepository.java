package com.ccpc.yeprogress.repository;

import com.ccpc.yeprogress.model.Campaign;
import com.ccpc.yeprogress.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    List<Campaign> findByUser_UserId(Long userId);
}
