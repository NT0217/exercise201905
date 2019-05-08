package com.example.chat.controller.advice;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.chat.security.LoginUserDetails;

@ControllerAdvice
public class CommonController {
    @ModelAttribute(name = "loginUser")
    private LoginUserDetails setupLoginUser(@AuthenticationPrincipal LoginUserDetails loginUserDetails) {
        return loginUserDetails;
    }
}
