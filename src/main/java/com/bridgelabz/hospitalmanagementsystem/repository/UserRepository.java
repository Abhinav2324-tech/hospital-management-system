package com.bridgelabz.hospitalmanagementsystem.repository;

import com.bridgelabz.hospitalmanagementsystem.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Provides database operations for user accounts.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}