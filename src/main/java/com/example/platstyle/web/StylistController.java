package com.example.platstyle.web;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@AllArgsConstructor
public class StylistController {
    @GetMapping(path = "/stylist/profile")
    public String profile(){ return "/stylist/profile";}

    @GetMapping(path = "/stylist/paymentRelease")
    public String paymentRelease(){ return "/stylist/paymentRelease";}

    @GetMapping(path = "/stylist/store")
    public String store(){ return "/stylist/store";}

}
