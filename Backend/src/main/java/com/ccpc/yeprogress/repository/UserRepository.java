package com.ccpc.yeprogress.repository;

import com.ccpc.yeprogress.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByFirebaseId(String firebaseId);
    boolean existsByFirebaseId(String firebaseId);
    void deleteByFirebaseId(String firebaseId);
}