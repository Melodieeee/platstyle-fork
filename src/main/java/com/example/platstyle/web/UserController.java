package com.example.platstyle.web;

import com.example.platstyle.entities.User;
import com.example.platstyle.respositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Controller
@AllArgsConstructor
public class UserController {
    private UserRepository userRepository;

    @GetMapping(path = "/")
    public String students() {
        return "index";
    }

    @GetMapping(path = "/login")
    public String login(){ return "login";}

    @GetMapping(path = "/signup")
    public String signup(WebRequest request, Model model){
        model.addAttribute("user", new User());
        return "signup";
    }

    @GetMapping(path = "/forgotpassword")
    public String forgotpassword(){ return "forgotpassword";}

    @PostMapping(path = "/register")
    public String register(Model model, User user, BindingResult
            bindingResult, ModelMap mm, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "signup";
        } else {
            ZoneId defaultZoneId = ZoneId.systemDefault();
            LocalDate localDate = LocalDate.now();
            Date date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            user.setRegisterDate(date);
            user.setRoles("ROLE_ADMIN");
            userRepository.save(user);
            return "redirect:/";
        }
    }
    @GetMapping(path = "/user/account")
    public String account(){ return "user/account";}

    @GetMapping(path = "/user/support")
    public String support(){ return "user/support";}

    @GetMapping(path = "/user/upload")
    public String upload(){ return "user/upload";}

    @GetMapping(path = "/user/cart")
    public String order(){ return "user/cart";}

    @GetMapping(path = "/user/checkout")
    public String checkout(){ return "user/checkout";}
}
