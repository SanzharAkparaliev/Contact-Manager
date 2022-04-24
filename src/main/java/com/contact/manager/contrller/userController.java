package com.contact.manager.contrller;

import com.contact.manager.entity.Contact;
import com.contact.manager.entity.User;
import com.contact.manager.helper.Message;
import com.contact.manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

@Controller
@RequestMapping("/user")
public class userController {
    @Autowired
    private UserRepository userRepository;
    @ModelAttribute
    public void addCommonData(Model model,Principal principal){
        String userName = principal.getName();
        User user = userRepository.getUserByUserName(userName);
        model.addAttribute("user",user);
    }

    @GetMapping("/index")
    public String dashboard(Model model, Principal principal) {
        model.addAttribute("title","User Dashboard");
        return "normal/user_dashboard";
    }

    @GetMapping("/add-contact")
    public String openAddContactForm(Model model){
        model.addAttribute("contact",new Contact());
        model.addAttribute("title","Add Contact");
        return "normal/add_contact_form";
    }

    @PostMapping("/process-contact")
    public String processContact(
            @ModelAttribute Contact contact,
            @RequestParam("profileImage") MultipartFile file,
            Principal principal,
            HttpSession session){
        try {
            String name = principal.getName();
            User user = userRepository.getUserByUserName(name);

            if(file.isEmpty()){

            }else {
                contact.setImage(file.getOriginalFilename());
                File saveFile  = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
                Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
            }
            contact.setUser(user);
            user.getContactList().add(contact);
            userRepository.save(user);
            //message success
            session.setAttribute("message",new Message("Your contact is added !! Add more ..","success"));
        }catch (Exception e){
            e.printStackTrace();
            session.setAttribute("message",new Message("Some went wrong !! Try again ..","danger"));

        }
        return "normal/add_contact_form";
    }
}
