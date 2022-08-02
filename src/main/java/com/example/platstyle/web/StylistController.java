package com.example.platstyle.web;

import com.example.platstyle.entities.*;
import com.example.platstyle.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import static com.example.platstyle.web.UserController.copyNonNullProperties;


@Controller
@AllArgsConstructor
public class StylistController {

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
    public String paymentRelease(){ return "stylist/paymentRelease";}

    @GetMapping(path = "/user/store")
    public String store(@RequestParam(name="stylist",defaultValue = "") long sid, Model model, Order_service order_service, Service service){
        Object stylist = stylistRepository.findBySid(sid).orElse(null);
        //取得order 狀態為0的order_service數量
        model.addAttribute("stylist", stylist);
        model.addAttribute("orderService", new Order_service());
        // addAttribute(名稱,數量)

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
    public String addCart(Order_service order_service, @RequestParam("serviceSelect") long sid, BindingResult bindingResult, Principal principal){

        if (bindingResult.hasErrors()) {
            return "user/store";
        } else {
            User user = userRepository.findByEmail(principal.getName()).orElse(null);
            Order order = orderRepository.findAllByUserAndStatus(user, 0).orElse(null);
            if(order == null) {
                Customer customer = customerRepository.findById(user.getUid()).orElse(null);
                Date in = new Date();
                LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
                ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
                Date date = Date.from(zdt.toInstant());
                order = new Order(null, date,"",customer.getAddress(),null,null,null,0,0,user,null,null,null);
                orderRepository.save(order);
                orderRepository.flush();
            }
            Service service = serviceRepository.findById(sid).orElse(null);
            if(service == null) throw new RuntimeException("can not find service");
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
            System.out.println(service.getUser().getFirstName());
            //取得數量 存入localstorage or session
            return  "redirect:/user/store?stylist="+service.getStylistId();
        }


    }
}
