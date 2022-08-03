package com.example.platstyle.web;

import com.example.platstyle.entities.Stylist;
import com.example.platstyle.entities.Support;
import com.example.platstyle.entities.User;
import com.example.platstyle.repositories.StylistRepository;
import com.example.platstyle.repositories.SupportMessageRepository;
import com.example.platstyle.repositories.SupportRepository;
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
    SupportRepository supportRepository;
    SupportMessageRepository supportMessageRepository;

    @GetMapping(path = "/admin/supportManagement")
    public String supportManagement(Model model){
        List<Support> supportList = supportRepository.findAll();
        model.addAttribute("supportList", supportList);
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
            user.setRoles("ROLE_STYLIST");
            stylistRepository.save(stylist);
            userRepository.save(user);
        }
        return "redirect:/admin/userManagement";
    }


    @GetMapping("/admin/denyStylistRequest")
    public String denyStylistRequest(Long id) {
        User user = userRepository.findAllByUid(id).orElse(null);
        System.out.println(user.getEmail());
        stylistRepository.deleteByUid(user);
        return "redirect:/admin/userManagement";
    }

    @GetMapping("/admin/removeStylistRequest")
    public String removeStylistRequest(Long id) {
        User user = userRepository.findAllByUid(id).orElse(null);
        userRepository.delete(user);
        return "redirect:/admin/userManagement";
    }

    @GetMapping("/admin/finishSupport")
    public String finishSupport(Long id) {
        Support support = supportRepository.findAllBySid(id).orElse(null);
        if(support==null) throw new RuntimeException("support does not exist");
        support.setStatus(true);
        supportRepository.save(support);
        return "redirect:/admin/supportManagement";
    }
}
