package com.example.chat.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.chat.db.repository.UserRepository;
import com.example.chat.db.entity.User;
import com.example.chat.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public User save(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user = userRepository.save(user);
		return user;
	}

	@Override
	public User findOne(Long userId) {
		return userRepository.findById(userId).get();
	}

}
