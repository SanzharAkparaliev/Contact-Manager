package com.contact.manager.contrller;

import com.contact.manager.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Random;

@Controller
public class ForgetController {
    Random random = new Random(1000);
    @Autowired
    private EmailService emailService;


    @GetMapping("/forgot")
    public String openEmail(){
        return "forgot_email_form";
    }

    @PostMapping("/send-otp")
    public String sendOTP(@RequestParam("email") String email, HttpSession session){

        System.out.println("EMAIL " + email);
        int otp = random.nextInt(999999);
        System.out.println("OTP  " + otp);

        //write code for send otp to email...
        String subject = "OTP from SCM";
        String message = " OTP = " + otp +" ";
        String to = email;
        boolean flag =  emailService.sendEmail(subject,message,to);
        if(flag){
            session.setAttribute("otp",otp);
            return "verify_otp";
        }else {
            session.setAttribute("message","Check your email id !!");
            return "forgot_email_form";
        }
    }
}
