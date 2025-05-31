package com.ccpc.yeprogress.repository;

import com.ccpc.yeprogress.model.Authentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticationRepository extends JpaRepository<Authentication, Long> {
    Authentication findByUser_UserId(Long userId);
}