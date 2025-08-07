package com.example.auth.repository;

import com.example.auth.entity.User;
import com.example.auth.entity.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findUserByUsername(String login);
    Optional<User> findUserByEmail(String email);
    @Query("SELECT u FROM User u WHERE u.username = :username AND u.role = :role")
    Optional<User> findUserByUsernameAndHasPremission(@Param("username") String username, @Param("role") UserType role);
    @Modifying
    @Transactional
    @Query("UPDATE User U SET U.email=:email, U.username=:username, U.password=:password, U.role=:role WHERE U.uuid=:uuid")
    int updateUserByUuid(@Param("email") String email, @Param("username") String username, @Param("password") String password, @Param("role") UserType userType, @Param("uuid") String uuid);
    @Transactional
    @Modifying
    @Query("DELETE FROM User u WHERE u.uuid=:uuid")
    int deleteUserByUuid(@Param("uuid") String uuid);

    Optional<User> findUserByUuid(String uuid);
    List<User> findAll();

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.role=:role WHERE u.uuid=:uuid")
    int changeUserRole(@Param("uuid") String uuid, @Param("role") UserType role);
}
