package com.example.platstyle.web;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class AdminController {
    @GetMapping(path = "/admin/supportManagement")
    public String supportManagement(){ return "admin/supportManagement";}

    @GetMapping(path = "/admin/userManagement")
    public String userManagement(){ return "admin/userManagement";}
}
