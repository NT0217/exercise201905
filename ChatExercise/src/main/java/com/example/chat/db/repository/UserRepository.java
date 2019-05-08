package com.example.chat.db.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.chat.db.entity.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByName(String name);
    
    boolean existsByName(String name);

}