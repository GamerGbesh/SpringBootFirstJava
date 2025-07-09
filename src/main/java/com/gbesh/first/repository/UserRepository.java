package com.gbesh.first.repository;

import com.gbesh.first.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
