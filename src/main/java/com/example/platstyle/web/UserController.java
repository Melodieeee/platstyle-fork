package com.example.platstyle.web;

import com.example.platstyle.entities.Customer;
import com.example.platstyle.entities.Stylist;
import com.example.platstyle.entities.User;
import com.example.platstyle.repositories.CustomerRepository;
import com.example.platstyle.repositories.StylistRepository;
import com.example.platstyle.repositories.UserRepository;
import lombok.AllArgsConstructor;
import net.bytebuddy.description.field.FieldList;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Controller
@AllArgsConstructor
public class UserController {
    private final String UPLOAD_DIR = "./uploads/";
    private UserRepository userRepository;
    private CustomerRepository customerRepository;
    private StylistRepository stylistRepository;
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
    @GetMapping(path = "/user/shop")
    public String home(Model model, Stylist stylists) {
        List<Stylist> stylistList = stylistRepository.findAll();
        model.addAttribute("stylistList", stylistList);
        return "user/shop";
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
            User existing = opor_user.get();
            copyNonNullProperties(user, existing);
            userRepository.save(existing);
            customer.setUid(existing.getUid());
            customerRepository.save(customer);
            return "redirect:/user/account";
        }
    }

    @GetMapping(path = "/user/support")
    public String support(){ return "user/support";}

    @GetMapping(path = "/user/upload")
    public String upload(){
        return "user/upload";
    }

    @PostMapping(path = "/user/upload")
    public String uploadFiles(@RequestParam("idFile") MultipartFile idFile,
                              @RequestParam("workPermitFile") MultipartFile workPermitFile,
                              Principal principal,
                              RedirectAttributes attributes){
        if(idFile.isEmpty() || workPermitFile.isEmpty()) {
            attributes.addFlashAttribute("message", "Please select a file to upload.");
            return "redirect:/user/upload";
        }
        String idFileName = StringUtils.cleanPath(idFile.getOriginalFilename());
        String workPermitFileName = StringUtils.cleanPath(workPermitFile.getOriginalFilename());
        try {
            Path path = Paths.get(UPLOAD_DIR + idFileName);
            Files.copy(idFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            path = Paths.get(UPLOAD_DIR + workPermitFileName);
            Files.copy(workPermitFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        User user = userRepository.findByEmail(principal.getName()).orElse(null);
        Stylist stylist = new Stylist(null, user, user.getFirstName()+" "+user.getLastName(),user.getPhone(),user.getEmail(), "", idFileName, workPermitFileName, "", false,0,null);
        stylistRepository.save(stylist);
        attributes.addFlashAttribute("message", "You successfully uploaded " + idFileName + " and "+ workPermitFileName +'!');
        return "redirect:/user/upload";
    }

    @GetMapping(path = "/user/cart")
    public String order(){ return "user/cart";}

    @GetMapping(path = "/user/checkout")
    public String checkout(){ return "user/checkout";}

    public static void copyNonNullProperties(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

    public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }


}


