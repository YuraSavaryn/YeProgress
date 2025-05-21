package com.ccpc.yeprogress.repository;

import com.ccpc.yeprogress.model.AuthenticationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticationStatusRepository extends JpaRepository<AuthenticationStatus, Long> {
}
