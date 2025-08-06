package com.example.auth.repository;

import com.example.auth.entity.User;
import com.example.auth.entity.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findUserByUsername(String login);
    Optional<User> findUserByEmail(String email);
    @Query("SELECT u FROM User u WHERE u.username = :username AND u.role = :role")
    Optional<User> findUserByUsernameAndHasPremission(@Param("username") String username, @Param("role") UserType role);

}
