package com.example.chat.service;

import com.example.chat.db.entity.User;

public interface UserService {
	
	User save (User user);
	
	User findOne(Long userId);
}
