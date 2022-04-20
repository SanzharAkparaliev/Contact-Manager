package com.contact.manager.contrller;

import com.contact.manager.entity.Contact;
import com.contact.manager.entity.User;
import com.contact.manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
        return "signup";
    }
}
