package com.example.event.Controller;

import com.example.event.Service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component
@RestController
@RequestMapping("/mail")
@CrossOrigin(origins = "http://localhost:5173")
public class MailController {

    @Autowired
    private MailService mailService;


    @PostMapping("/sendMail")
    public boolean mailSend(@RequestBody String email){
        mailService.sendMail(email,"you are successfully Register to OrnaVista","OrnaVista Registration Successful");
        return true;
    }
}
