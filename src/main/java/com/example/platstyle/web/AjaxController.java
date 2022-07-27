package com.example.platstyle.web;

import com.example.platstyle.entities.Service;
import com.example.platstyle.entities.User;
import com.example.platstyle.repositories.ServiceRepository;
import com.example.platstyle.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/ajax")
@AllArgsConstructor
public class AjaxController {
    private  ServiceRepository serviceRepository;
    private UserRepository userRepository;

    @RequestMapping(value = "getServiceFee/{serviceID}", method = RequestMethod.GET, produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> getServiceFee(@PathVariable("serviceID") Long sid) {
        Service service = serviceRepository.findById(sid).orElse(null);
        if(service == null) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        else {
            try {
                String output="{\"minPrice\":"+ service.getMinPrice()+ ", \"maxPrice\":"+service.getMaxPrice()+"}";
                ResponseEntity<String> responseEntity = new ResponseEntity<String>(output, HttpStatus.OK);
                return responseEntity;
            } catch (Exception e) {
                return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
            }
        }

    }

    @RequestMapping(value = "getServicesByGender/{gender}", method = RequestMethod.GET, produces = {MimeTypeUtils.TEXT_HTML_VALUE})
    public ResponseEntity<String> getServicesByGender(@PathVariable("gender") String gender, Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).orElse(null);
        List<Service> services = serviceRepository.findAllByUidANDGender(user.getUid(), gender);
        if(services == null) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        else {

            String serviceJson ="";
            for (Service service: services) {
                    serviceJson = serviceJson + "<option value='"+service.getSid()+"'>"+service.getServiceName()+"</option>";
            }
            try {
                ResponseEntity<String> responseEntity = new ResponseEntity<String>(serviceJson, HttpStatus.OK);
                return responseEntity;
            } catch (Exception e) {
                return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
            }
        }
    }

    @RequestMapping(value = "addService", method = RequestMethod.POST, produces = {MimeTypeUtils.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> addService(@RequestParam("gender") String gender,
                                             @RequestParam("serviceName") String serviceName,
                                             @RequestParam("minPrice") double minPrice,
                                             @RequestParam("maxPrice") double maxPrice, Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).orElse(null);
        Service service = new Service(null ,gender,serviceName,minPrice,maxPrice,user);
        serviceRepository.save(service);
        List<Service> services = serviceRepository.findAllByUidANDGender(user.getUid(), gender);
        String response ="success";
        try {
            ResponseEntity<String> responseEntity = new ResponseEntity<String>(response, HttpStatus.OK);
            return responseEntity;
        } catch (Exception e) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "setService", method = RequestMethod.POST, produces = {MimeTypeUtils.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> addService(@RequestParam("serviceID") Long sid,
                                             @RequestParam("minPrice") double minPrice,
                                             @RequestParam("maxPrice") double maxPrice, Principal principal) {
        String response ="";
        User user = userRepository.findByEmail(principal.getName()).orElse(null);
        Service service = serviceRepository.findById(sid).orElse(null);
        if(service == null) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        service.setMinPrice(minPrice);
        service.setMaxPrice(maxPrice);
        serviceRepository.save(service);
        response = "success";
        try {
            ResponseEntity<String> responseEntity = new ResponseEntity<String>(response, HttpStatus.OK);
            return responseEntity;
        } catch (Exception e) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }
}
