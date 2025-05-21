package com.ccpc.yeprogress.repository;

import com.ccpc.yeprogress.model.CommentsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentsUserRepository extends JpaRepository<CommentsUser, Long> {
}
