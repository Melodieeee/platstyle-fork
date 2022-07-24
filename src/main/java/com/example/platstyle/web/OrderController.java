package com.example.platstyle.web;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class OrderController {

    @GetMapping(path = "/user/orders")
    public String orders(){ return "/user/orders";}

    @GetMapping(path = "/user/orderDetail")
    public String orderDetail(){ return "/user/orderDetail";}

    @GetMapping(path = "/stylist/appointments")
    public String appointments(){ return "/stylist/appointments";}

    @GetMapping(path = "/stylist/ongoingAppointments")
    public String ongoingAppointments(){ return "/stylist/ongoingAppointments";}

}
