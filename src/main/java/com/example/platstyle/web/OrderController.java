package com.example.platstyle.web;

import com.example.platstyle.entities.Order;
import com.example.platstyle.entities.Order_service;
import com.example.platstyle.entities.Service;
import com.example.platstyle.entities.User;
import com.example.platstyle.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@AllArgsConstructor
public class OrderController {

    private UserRepository userRepository;
    private StylistRepository stylistRepository;
    private OrderRepository orderRepository;
    private OrderServiceRepository orderServiceRepository;

    @GetMapping(path = "/user/orders")
    public String orders(Model model, Principal principal){
        User user = userRepository.findByEmail(principal.getName()).orElse(null);
        List<Order> orderList = orderRepository.findAllByUid(user.getUid());
        model.addAttribute("orderList", orderList);
        return "user/orders";
    }

    @GetMapping(path = "/user/orderDetail")
    public String orderDetail(){ return "user/orderDetail";}

    @GetMapping(path = "/stylist/appointments")
    public String appointments(Model model, Principal principal){
        User user = userRepository.findByEmail(principal.getName()).orElse(null);
        List<Order> appointmentList = orderRepository.findAllBySid(user.getStylistId());
        model.addAttribute("appointmentList", appointmentList);
        return "stylist/appointments";
    }

    @GetMapping(path = "/stylist/ongoingAppointments")
    public String ongoingAppointments(){ return "stylist/ongoingAppointments";}

    @GetMapping(path = "/stylist/manageAppointment")
    public String manageAppointment(Model model, @RequestParam(name="order",defaultValue = "0") long oid){
        if(oid == 0) return "redirect:/user/shop";
        Order order = orderRepository.findById(oid).orElse(null);
        if(order == null) throw new RuntimeException("Can not find appointment!");
        List<Order_service> services = order.getServices();
        model.addAttribute("order", order);
        model.addAttribute("services", services);
        return "stylist/manageAppointment";
    }

}
