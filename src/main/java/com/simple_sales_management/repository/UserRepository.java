package com.simple_sales_management.repository;

import com.simple_sales_management.enums.Roles;
import com.simple_sales_management.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailIgnoreCase(String username);
    boolean existsByEmail(String email);
    void delete(Optional<User> user);

    Page<User> findByRoles(Pageable pageable, Roles roles);

    Optional<User> findByIdAndRoles(Long userId, Roles role);

}
