package com.example.platstyle.web;

import com.example.platstyle.entities.Service;
import com.example.platstyle.entities.User;
import com.example.platstyle.repositories.CustomerRepository;
import com.example.platstyle.repositories.ServiceRepository;
import com.example.platstyle.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;


@Controller
@AllArgsConstructor
public class StylistController {

    private ServiceRepository serviceRepository;
    private UserRepository userRepository;
    @GetMapping(path = "/stylist/profile")
    public String profile(){ return "stylist/profile";}

    @GetMapping(path = "/stylist/paymentRelease")
    public String paymentRelease(){ return "stylist/paymentRelease";}

    @GetMapping(path = "/user/store")
    public String store(){ return "stylist/store";}

    @GetMapping(path = "/stylist/portfolio")
    public String portfolio(Model model){
        model.addAttribute("newService", new Service());
        return "stylist/portfolio";
    }

    @PostMapping(path = "/stylist/addService")
    public String addService(Service newService, BindingResult bindingResult, Principal principal){
        if (bindingResult.hasErrors()) {
            return "stylist/portfolio?error";
        } else {
            User user = userRepository.findByEmail(principal.getName()).orElse(null);
            newService.setUser(user);
            serviceRepository.save(newService);
            return "redirect:/stylist/portfolio";
        }
    }

}
