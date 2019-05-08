package com.example.chat.security;


import org.springframework.security.core.authority.AuthorityUtils;

import com.example.chat.db.entity.User;


public class LoginUserDetails extends org.springframework.security.core.userdetails.User{

	private Long userId;
	private String userName;

	public LoginUserDetails(User user) {
		super(user.getName(), user.getPassword(),AuthorityUtils.createAuthorityList("ROLE_USER"));
		this.userId = user.getId();
		this.userName = user.getName();
	}
	
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
		this.userId = userId;
	}
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
		this.userName = userName;
	}
}
