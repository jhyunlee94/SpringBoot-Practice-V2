package com.example.mvc.controller;

import com.example.mvc.entity.User;
import com.example.mvc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "account/login";
    }

    @GetMapping("/register")
    public String register() {
        return "account/register";
    }

    @PostMapping("/register")
    public String register(User user) {
        //user를 받아서 사용자를 저장해야하는데
        //패스워드 암호화해야하고 사용자 권한도 추가해줘야하기에 여기서는 서비스 클래스를 만들어서 처리해주겠습니다.
        userService.save(user);
        return "redirect:/";
    }
}
