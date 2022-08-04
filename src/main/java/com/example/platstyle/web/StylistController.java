package com.example.platstyle.web;

import com.example.platstyle.entities.*;
import com.example.platstyle.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.example.platstyle.web.UserController.copyNonNullProperties;


@Controller
@AllArgsConstructor
public class StylistController {

    private final String UPLOAD_PHOTO_DIR = "src/main/resources/static/img/";
    private ServiceRepository serviceRepository;
    private UserRepository userRepository;
    private StylistRepository stylistRepository;
    private TimeslotRepository timeslotRepository;
    private OrderRepository orderRepository;
    private OrderServiceRepository orderServiceRepository;

    private CustomerRepository customerRepository;


    @GetMapping(path = "/stylist/profile")
    public String profile(Model model, Principal principal){
        User user = userRepository.findByEmail(principal.getName()).orElse(null);
        Stylist stylist = stylistRepository.findAllByUid(user).orElse(null);
        if(stylist == null) return "error/403";
        model.addAttribute("stylist", stylist);
        return "stylist/profile";
    }

    @GetMapping(path = "/stylist/paymentRelease")
    public String paymentRelease(Model model, Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).orElse(null);
        Stylist stylist = stylistRepository.findAllByUid(user).orElse(null);
        model.addAttribute("balance", stylist.getBalance());
        return "stylist/paymentRelease";
    }

    @GetMapping(path = "/user/store")
    public String store(@RequestParam(name="stylist",defaultValue = "") long sid, Model model, Order_service order_service, Service service, Principal principal){
        int count;
        Stylist stylist = stylistRepository.findBySid(sid).orElse(null);
        User user = userRepository.findByEmail(principal.getName()).orElse(null);
        Order order = orderRepository.findAllByUserAndStatus(user, 0).orElse(null);
        List<Timeslot> timeslots = timeslotRepository.findAllByUid(stylist.getUid().getUid());
        if(order == null) {
            count = 0;
        } else {
            count = order.getServices().size();
        }
        model.addAttribute("stylist", stylist);
        model.addAttribute("orderService", new Order_service());
        model.addAttribute("count", count);
        model.addAttribute("timeslot", timeslots);
        System.out.println(stylist.getFeedbacks());
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

    @PostMapping(path = "/user/addService")
    public String addCart(Order_service order_service, @RequestParam("serviceSelect") long sid, BindingResult bindingResult, Principal principal, Model model){
        if(sid==0) return "redirect:/user/shop";
        if (bindingResult.hasErrors()) {
            return "redirect:/user/shop";
        } else {
            User user = userRepository.findByEmail(principal.getName()).orElse(null);
            Service service = serviceRepository.findById(sid).orElse(null);
            if(service == null) throw new RuntimeException("can not find service");
            Stylist stylist = stylistRepository.findAllByUid(service.getUser()).orElse(null);
            Order order = orderRepository.findAllByUserAndStatus(user, 0).orElse(null);
            if(order == null) {
                Customer customer = customerRepository.findById(user.getUid()).orElse(null);
                Date in = new Date();
                LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
                ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
                Date date = Date.from(zdt.toInstant());
                order = new Order(null, date,"",customer.getAddress(),null,null,null,0,0,0,user,stylist,null,null,null);
                orderRepository.save(order);
                orderRepository.flush();
            } else if(stylist.getSid() != order.getStylist().getSid()){
                orderServiceRepository.deleteByOid(order);
                order.setStylist(stylist);
                orderRepository.save(order);
            }
            order_service.setService(service);
            double minPrice = service.getMinPrice();
            double maxPrice = service.getMaxPrice();
            double midPrice = (minPrice+maxPrice)/2;
            switch (order_service.getLengthOfHair()) {
                case "short":
                    order_service.setPrice(minPrice);
                    break;
                case "medium":
                    order_service.setPrice(midPrice);
                    break;
                case "long":
                    order_service.setPrice(maxPrice);
                    break;
            }
            order_service.setOrder(order);
            orderServiceRepository.save(order_service);

            return  "redirect:/user/store?stylist="+service.getStylistId();
        }
    }


    @PostMapping("/stylist/profile")
    public String uploadPhoto(@RequestParam("photoFile") MultipartFile photoFile, Principal principal, Model model, RedirectAttributes attributes){

        String photoFileName = StringUtils.cleanPath(photoFile.getOriginalFilename());
        try {
            Path path = Paths.get(UPLOAD_PHOTO_DIR + photoFileName);
            Files.copy(photoFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        User user = userRepository.findByEmail(principal.getName()).orElse(null);
        Stylist stylist = stylistRepository.findBySid(user.getStylistId()).orElse(null);
        stylist.setPhoto("../img/"+photoFileName);
        stylistRepository.save(stylist);
        attributes.addFlashAttribute("message", "You successfully uploaded " + photoFileName + '!');

        return "redirect:/stylist/profile";
    }

}
