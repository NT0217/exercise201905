package com.example.chat.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.chat.entity.Message;

public interface MessageService {
    void save(Message message, Long userId);

    Page<Message> findAll(Pageable pageable);
}
