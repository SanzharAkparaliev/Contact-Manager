package com.contact.manager.contrller;

import com.contact.manager.entity.Contact;
import com.contact.manager.entity.User;
import com.contact.manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class HomeController {
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/test")
    public String test(){
        User user = new User();
        user.setName("Sanzhar");
        user.setEmail("sanzhar@gmail.com");
        Contact contact = new Contact();
        user.getContactList().add(contact);

        userRepository.save(user);
        return "Working";
    }
}
