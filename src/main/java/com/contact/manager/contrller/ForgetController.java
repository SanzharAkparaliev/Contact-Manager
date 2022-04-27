package com.contact.manager.contrller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Random;

@Controller
public class ForgetController {
    Random random = new Random(1000);


    @GetMapping("/forgot")
    public String openEmail(){
        return "forgot_email_form";
    }

    @PostMapping("/send-otp")
    public String sendOTP(@RequestParam("email") String email){

        System.out.println("EMAIL " + email);
        int otp = random.nextInt(999999);
        System.out.println("OTP  " + otp);
        return "verify_otp";
    }
}
