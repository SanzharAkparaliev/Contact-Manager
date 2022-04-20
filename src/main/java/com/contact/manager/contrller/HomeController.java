package com.contact.manager.contrller;

import com.contact.manager.entity.Contact;
import com.contact.manager.entity.User;
import com.contact.manager.helper.Message;
import com.contact.manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("title","Home - Smart Contact Manager");
        return "home";
    }

    @GetMapping("/signup")
    public String singup(Model model){
        model.addAttribute("title","Register - Smart Contact Manager");
        model.addAttribute("user",new User());
        return "signup";
    }

    @PostMapping("/do_register")
    public String registerUser(@ModelAttribute("user") User user, @RequestParam(value = "agreement",defaultValue = "false")
    Boolean agreement, Model model, HttpSession session){
       try {
           if(!agreement){
               System.out.println("You have not agreed the terms and conditions");
               throw new Exception("You have not agreed the terms and conditions");
           }
           user.setRole("ROLE_USER");
           user.setEnabled(true);
           user.setImageUrl("default.png");
           System.out.println("Agreement " + agreement);
           System.out.println("User " + user);
           model.addAttribute("user",new User());
           //userRepository.save(user);
           session.setAttribute("message",new Message("Successfully Register !! " ,"alert-success"));
           return "signup";
       }catch (Exception e){
           e.printStackTrace();
           model.addAttribute("user",user);
           session.setAttribute("message",new Message("Something Went wrong !! " + e.getMessage(),"alert-danger"));
           return "signup";
       }
    }
}
