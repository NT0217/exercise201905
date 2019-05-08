package com.example.chat.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.chat.db.repository.MessageRepository;
import com.example.chat.db.entity.Message;
import com.example.chat.service.MessageService;
import com.example.chat.service.UserService;

@Service
@Transactional
public class MessageServiceImpl implements MessageService{

	@Autowired
	private MessageRepository messageRepository;
	
	@Autowired
	private UserService userService;
	
	@Override
	public void save(Message message, Long userId) {
		message.setUser(userService.findOne(userId));
		
		messageRepository.save(message);
	}

	@Override
	public Page<Message> findAll(Pageable pageable) {
		return messageRepository.findAll(pageable);
	}

}
