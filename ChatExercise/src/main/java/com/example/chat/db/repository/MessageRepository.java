package com.example.chat.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.chat.db.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long>{

}
