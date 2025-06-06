package com.ccpc.yeprogress.repository;

import com.ccpc.yeprogress.model.CommentsCampaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentsCampaignRepository extends JpaRepository<CommentsCampaign, Long> {
}
