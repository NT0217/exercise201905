package com.example.chat.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.chat.entity.User;
import com.example.chat.form.UserForm;
import com.example.chat.repository.UserRepository;
import com.example.chat.security.LoginUserDetails;
import com.example.chat.service.impl.UserServiceImpl;

@Controller
public class UserController {
	
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping(value = "/login")
	public String login(@AuthenticationPrincipal LoginUserDetails userDetails) {
		if(userDetails != null) {
			return "redirect:/";
		}
		return "login";
	}
	
	@RequestMapping(value = "/sign_up", method = RequestMethod.GET)
	public String signup(UserForm user) {
		return "signup";
	}
	
	@RequestMapping(value = "/sign_up", method = RequestMethod.POST)
	public String signup(@Validated UserForm form, BindingResult result) {
		if(!form.getPassword().equals(form.getConfirmPassword())) {
			result.rejectValue("password", "error.passwordConfirmation", "do not match.");
		}
		
		if(userRepository.existsByName(form.getName())) {
			result.rejectValue("name", "error.nameDuplication", "already exists.");
		}
		
		if(result.hasErrors()) {
			return "signup";
		}
		User user = new User();
		BeanUtils.copyProperties(form, user);
		userService.save(user);
		return "redirect:/";
	}
}
