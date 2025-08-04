package com.example.auth.repository;

import com.example.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findUserByUsername(String login);
    Optional<User> findUserByEmail(String email);
    @Query("SELECT u FROM User u WHERE u.username = :username AND u.role = com.example.auth.entity.UserRole.ADMIN")
    Optional<User> findUserByUsernameAndIsAdmin(@Param("username") String username);
//    @Query(nativeQuery = true, value = "SELECT * FROM users WHERE username = :username AND role = 'ADMIN'")
//    Optional<User> findUserByUsernameAndIsAdmin(@Param("username") String username);
}
