package com.example.platstyle.web;

import com.example.platstyle.entities.*;
import com.example.platstyle.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@RestController
@RequestMapping("api/ajax")
@AllArgsConstructor
public class AjaxController {
    private  ServiceRepository serviceRepository;
    private UserRepository userRepository;
    private StylistRepository stylistRepository;
    private TimeslotRepository timeslotRepository;
    private SupportRepository supportRepository;
    SupportMessageRepository supportMessageRepository;

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
    @RequestMapping(value = "getServicesBySidAndGender/{sid}/{gender}", method = RequestMethod.GET, produces = {MimeTypeUtils.TEXT_HTML_VALUE})
    public ResponseEntity<String> getServicesByGender(@PathVariable("sid") Long sid,@PathVariable("gender") String gender, Principal principal) {
        Long uid= stylistRepository.findUidBySid(sid);
        List<Service> services = serviceRepository.findAllByUidANDGender(uid, gender);
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
        Service service = new Service(null ,gender,serviceName,minPrice,maxPrice,user,null);
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

    @RequestMapping(value = "getTimeslots", method = RequestMethod.GET, produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> getTimeslotsByUid(Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).orElse(null);
        if(user == null) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        List<Timeslot> timeslots = timeslotRepository.findAllByUid(user.getUid());
        if(timeslots == null) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        else {
            StringBuffer timeSlotJson = new StringBuffer();
            boolean first = true;
            timeSlotJson.append("[");
            for (Timeslot timeslot: timeslots) {
                if (!first) {
                    timeSlotJson.append(",");
                }
                timeSlotJson.append("{");
                timeSlotJson.append("\"week\": \""+timeslot.getWeek()+"\", ");
                timeSlotJson.append("\"startTime\": \""+timeslot.getStartTime()+"\", ");
                timeSlotJson.append("\"endTime\": \""+timeslot.getEndTime()+"\" ");
                timeSlotJson.append("}");
                first = false;
            }
            timeSlotJson.append("]");
            try {
                ResponseEntity<String> responseEntity = new ResponseEntity<String>(timeSlotJson.toString(), HttpStatus.OK);
                return responseEntity;
            } catch (Exception e) {
                return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
            }
        }
    }

    @RequestMapping(value = "setProfrile", method = RequestMethod.POST, produces = {MimeTypeUtils.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> setProfrile(@RequestParam("name") String name,
                                              @RequestParam("city") String city,
                                              @RequestParam("phone") String phone,
                                              @RequestParam("email") String email,
                                              @RequestParam("week") String[] weeks,
                                              @RequestParam Map<String,String> searchParams,
                                              Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).orElse(null);
        if(user == null) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        Stylist stylist = stylistRepository.findAllByUid(user).orElse(null);
        if(!name.equals(""))  stylist.setName(name);
        stylist.setCity(city);
        stylist.setPhone(phone);
        stylist.setEmail(email);
        stylistRepository.save(stylist);
        timeslotRepository.deleteByUser(user);
        for(String week: weeks) {
            String startTime = week+"Start";
            String endTime = week+"End";
            Timeslot timeslot = new Timeslot(null, week, searchParams.get(startTime), searchParams.get(endTime), user);
            timeslotRepository.save(timeslot);
        }
        String response="success";
        try {
            ResponseEntity<String> responseEntity = new ResponseEntity<String>(response, HttpStatus.OK);
            return responseEntity;
        } catch (Exception e) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "getMessages/{sid}", method = RequestMethod.GET, produces = {MimeTypeUtils.TEXT_HTML_VALUE})
    public ResponseEntity<String> getMessages(@PathVariable("sid") Long sid, Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).orElse(null);
        Support support = supportRepository.findAllBySid(sid).orElse(null);
        List<Support_message> messages = support.getMessages();
        if(messages == null) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        else {
            String supportJson ="";
            supportJson = "<p>\n" +
                    "                        Order: "+support.getOrderNum()+"<br>\n" +
                    "                        Name: "+support.getUserName()+"<br>\n" +
                    "                        Date: "+support.getFormatCreateDate()+"<br>\n" +
                    "                        Category: "+support.getCategory()+"<br>\n" +
                    "                        Subject: "+support.getSubject()+"<br>\n" +
                    "                    </p>\n" +
                    "                    <div class=\"mt-4 text-start\" style = \"width: 100%; height: 250px; line-height: 3em; overflow:scroll;border: thin #000 solid; padding: 5px;\">\n" +
                    "                        <blockquote id=\"messages\" class=\"clearfix\" style=\"display: block !important\">";
            for (Support_message message: messages) {
                String messageFormatDate = new SimpleDateFormat("MM/dd").format(message.getCreateDate());
                if(message.getSender() != user) {
                    supportJson += "<div class=\"stranger text \" mid=\"1\">\n" +
                            "                                <span class=\"hidden_text\">"+message.getSenderName()+":</span>"+message.getMessage()+"\n" +
                            "                                <div class=\"stranger comment\">\n" +
                            "                                    <span class=\"hidden_text\"> (</span>\n" +
                            "                                    <time class=\"timeago\" datetime=\""+message.getCreateDate()+"\">"+messageFormatDate+"</time>\n" +
                            "                                    <span class=\"hidden_text\">)<br></span>\n" +
                            "                                </div>\n" +
                            "                            </div>";
                } else {
                    supportJson += "<div class=\"me text\" id=\"msgcqltdjvyj\" mid=\"0\">\n" +
                            "                                <span class=\"hidden_text\">Me: </span>"+message.getMessage()+"\n" +
                            "                                <div class=\"me comment\">\n" +
                            "                                    <span class=\"hidden_text\">(</span>\n" +
                            "                                    <time class=\"timeago\" datetime=\""+message.getCreateDate()+"\">"+messageFormatDate+"</time>\n" +
                            "                                    <span class=\"hidden_text\">)<br></span>\n" +
                            "                                </div>\n" +
                            "                            </div>";
                }
            }
            supportJson = supportJson + "</blockquote> </div>";
            try {
                ResponseEntity<String> responseEntity = new ResponseEntity<String>(supportJson, HttpStatus.OK);
                return responseEntity;
            } catch (Exception e) {
                return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
            }
        }
    }

    @RequestMapping(value = "addMessage", method = RequestMethod.POST, produces = {MimeTypeUtils.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> addMessage(@RequestParam("message") String message,
                                              @RequestParam("sid") Long sid,
                                              Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).orElse(null);
        Support support = supportRepository.findAllBySid(sid).orElse(null);
        if(user == null) return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        Date in = new Date();
        LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
        ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
        Date date = Date.from(zdt.toInstant());
        Support_message support_message = new Support_message(null, message, date, user, support);
        supportMessageRepository.save(support_message);
        String response="success";
        try {
            ResponseEntity<String> responseEntity = new ResponseEntity<String>(response, HttpStatus.OK);
            return responseEntity;
        } catch (Exception e) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }
}
