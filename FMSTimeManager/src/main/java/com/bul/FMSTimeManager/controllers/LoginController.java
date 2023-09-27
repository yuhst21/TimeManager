package com.bul.FMSTimeManager.controllers;

import com.bul.FMSTimeManager.daos.UserRepository;
import com.bul.FMSTimeManager.models.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class LoginController {
    @Autowired
    UserRepository userRepository;
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String postLogin(
            @RequestParam(value = "name", required = false) String user_name
            , @RequestParam(value = "password", required = false) String password
            , HttpSession session
    ) {
        User u = userRepository.check(user_name,password);
        session.setAttribute("user_object",u);

        return "redirect:/my_request";
    }
}
