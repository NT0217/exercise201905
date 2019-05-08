package com.example.chat.controller;

import java.security.Principal;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.example.chat.entity.Message;
import com.example.chat.entity.SocketMessage;
import com.example.chat.form.ChatForm;
import com.example.chat.security.LoginUserDetails;
import com.example.chat.service.impl.MessageServiceImpl;

@Controller
public class ChatController {
	
    @Autowired
    private MessageServiceImpl messageService;
    
    @GetMapping("/")
    public ModelAndView index(@PageableDefault(
            page = 0,
            size = 100,
            direction = Sort.Direction.ASC,
            sort = {"createdAt"}
                    )Pageable pageable, ModelAndView mav) {
        Page<Message> messages = messageService.findAll(pageable);
        mav.addObject("messages", messages);    	
    	mav.setViewName("index");
    	return mav;
    }
    
	@MessageMapping(value = "/message")
	@SendTo(value = "/topic/greetings")
	public SocketMessage greet(@PathVariable ChatForm form, Principal principal) {
		Authentication  auth = (Authentication)principal;
		LoginUserDetails userDetails = (LoginUserDetails)auth.getPrincipal();
		Message message = new Message();
		BeanUtils.copyProperties(form, message);
		messageService.save(message, userDetails.getUserId());
		return new SocketMessage(message.getBody(), userDetails.getUserName(), message.getCreatedAt().toString(), userDetails.getUserId());
	}
}
