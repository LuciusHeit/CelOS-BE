package com.badnarrators.celos.user.repository;

import com.badnarrators.celos.user.entity.User;
import com.badnarrators.celos.user.service.UserService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameAndPassword(String username, String password);
    Optional<User> findByIdAndPassword(Long id, String password);

    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameIgnoreCase(String username);

    boolean existsByUsername(String username);
    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByUsernameIgnoreCaseAndDomainIgnoreCase(String username, String domain);

    User findByUsernameIgnoreCaseAndDomainIgnoreCase(String username, String domain);

    int countByUsernameIgnoreCase(String username);

    List<User> findAllByUsernameIgnoreCase(String username);
}
