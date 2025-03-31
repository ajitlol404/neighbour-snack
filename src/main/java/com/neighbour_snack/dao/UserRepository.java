package com.neighbour_snack.dao;

import com.neighbour_snack.entity.Role;
import com.neighbour_snack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByRole(Role role);

    boolean existsByEmail(String email);

    boolean existsByUuid(UUID uuid);

    Optional<User> findByEmail(String email);

    Optional<User> findByUuid(UUID uuid);

    default User findUserByEmail(String email) {
        return findByEmail(email).orElseThrow(() -> new NoSuchElementException("User not found."));
    }

    default User findUserByUuid(UUID uuid) {
        return findByUuid(uuid).orElseThrow(() -> new NoSuchElementException("User not found."));
    }

    default User findUserById(Integer id) {
        return findById(id).orElseThrow(() -> new NoSuchElementException("User not found."));
    }
}

