package com.example.platstyle.web;

import com.example.platstyle.entities.Service;
import com.example.platstyle.entities.Stylist;
import com.example.platstyle.entities.Timeslot;
import com.example.platstyle.entities.User;
import com.example.platstyle.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    private StylistRepository stylistRepository;
    private TimeslotRepository timeslotRepository;

    @GetMapping(path = "/stylist/profile")
    public String profile(Model model, Principal principal){
        User user = userRepository.findByEmail(principal.getName()).orElse(null);
        Stylist stylist = stylistRepository.findById(user.getUid()).orElse(null);
        if(stylist == null) return "error/403";
        model.addAttribute("stylist", stylist);
        return "stylist/profile";
    }

    @GetMapping(path = "/stylist/paymentRelease")
    public String paymentRelease(){ return "stylist/paymentRelease";}

    @GetMapping(path = "/user/store")
    public String store(@RequestParam(name="stylist",defaultValue = "") String uid){

        return "stylist/store";
    }

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
