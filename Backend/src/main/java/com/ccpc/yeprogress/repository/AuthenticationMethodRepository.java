package com.ccpc.yeprogress.repository;

import com.ccpc.yeprogress.model.AuthenticationMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticationMethodRepository extends JpaRepository<AuthenticationMethod, Long> {
}
