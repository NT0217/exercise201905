package com.example.chat.service.impl;


import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.chat.entity.User;
import com.example.chat.repository.UserRepository;
import com.example.chat.security.LoginUserDetails;

@Service
@Transactional
public class UserDetailsSerivceImpl implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userRepository.findByName(name);
        if (user == null) {
            throw new UsernameNotFoundException("No Found this User Name: " + name);
        }
        
        return new LoginUserDetails(user);
    }
}
