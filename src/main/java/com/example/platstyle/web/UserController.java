package com.example.platstyle.web;

import com.example.platstyle.entities.Customer;
import com.example.platstyle.entities.User;
import com.example.platstyle.respositories.CustomerRepository;
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
import java.security.Principal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class UserController {
    private UserRepository userRepository;
    private CustomerRepository customerRepository;

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
    @GetMapping(path = "/user/")
    public String home() {
        return "user/home";
    }
    @GetMapping(path = "/user/account")
    public String account(Model model, Principal principal){
        User user = userRepository.findByEmail(principal.getName()).orElse(null);
        Customer customer = customerRepository.findById(user.getUid()).orElse(null);
        if(user==null) throw new RuntimeException("User does not exist");
        if(customer==null) customer = new Customer();
        model.addAttribute("user", user);
        model.addAttribute("customer", customer);
        return "user/account";
    }

    @PostMapping(path="/save")
    public String save(Model model, User user, Customer customer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/account";
        } else {
            Optional<User> opor_user = userRepository.findByEmail(user.getEmail());
            User or_user = opor_user.get();
            user.setGender(or_user.getGender());
            user.setPassword(or_user.getPassword());
            user.setRegisterDate(or_user.getRegisterDate());
            user.setRoles(or_user.getRoles());
            System.out.println(user.getUid());
            userRepository.save(user);
            customer.setUid(user.getUid());
            customerRepository.save(customer);
            return "redirect:/";
        }

    }

    @GetMapping(path = "/user/support")
    public String support(){ return "user/support";}

    @GetMapping(path = "/user/upload")
    public String upload(){ return "user/upload";}

    @GetMapping(path = "/user/cart")
    public String order(){ return "user/cart";}

    @GetMapping(path = "/user/checkout")
    public String checkout(){ return "user/checkout";}


}


