package com.ccpc.yeprogress.repository;

import com.ccpc.yeprogress.model.SocialNetwork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialNetworkRepository extends JpaRepository<SocialNetwork, Long> {
}
