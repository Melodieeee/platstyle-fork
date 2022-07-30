package com.example.platstyle.web;

import com.example.platstyle.entities.Stylist;
import com.example.platstyle.entities.User;
import com.example.platstyle.repositories.StylistRepository;
import com.example.platstyle.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class AdminController {

    UserRepository userRepository;
    StylistRepository stylistRepository;

    @GetMapping(path = "/admin/supportManagement")
    public String supportManagement(){

        return "admin/supportManagement";
    }

    @GetMapping(path = "/admin/userManagement")
    public String userManagement(Model model){
        List<User> usersList = userRepository.findAll();
        model.addAttribute("usersList", usersList);
        return "admin/userManagement";
    }

    @GetMapping("/admin/acceptStylistRequest")
    public String acceptStylistRequest(Long id) {
        User user = userRepository.findAllByUid(id).orElse(null);
        Stylist stylist = stylistRepository.findAllByUid(user).orElse(null);
        if(stylist != null && user != null) {
            stylist.setVerify(true);
            if (user.getRoles().equals("ROLE_ADMIN")){
                user.setRoles("ROLE_ADMIN");
            } else {
                user.setRoles("ROLE_STYLIST");
            }
            stylistRepository.save(stylist);
            userRepository.save(user);
        }
        return "redirect:/admin/userManagement";
    }


    @GetMapping("/admin/denyStylistRequest")
    public String denyStylistRequest(Long id) {
        User user = userRepository.findAllByUid(id).orElse(null);
        stylistRepository.deleteAllByUid(user);
        return "redirect:/admin/userManagement";
    }
}
