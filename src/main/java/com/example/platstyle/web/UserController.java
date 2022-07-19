package com.example.platstyle.web;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class UserController {
    //private UserRepository userRepository;

    @GetMapping(path = "/")
    public String students() {
        return "users";
    }

}
