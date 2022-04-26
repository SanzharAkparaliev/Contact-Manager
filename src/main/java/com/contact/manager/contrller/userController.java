package com.contact.manager.contrller;

import com.contact.manager.entity.Contact;
import com.contact.manager.entity.User;
import com.contact.manager.helper.Message;
import com.contact.manager.repository.ContactRepository;
import com.contact.manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class userController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContactRepository contactRepository;
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
                contact.setImage("contact.png");
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


    @GetMapping("/show-contacts/{page}")
    public String showContact(@PathVariable("page") Integer page,Model model,Principal principal){
        model.addAttribute("title","Show User Contacts");
        String username = principal.getName();
        User user = userRepository.getUserByUserName(username);
        Pageable pageable = PageRequest.of(page,8);
        Page<Contact> contactList = contactRepository.findContactByUser(user.getUserId().intValue(),pageable);
        model.addAttribute("contacts",contactList);
        model.addAttribute("currentPage",page);
        model.addAttribute("totalPages",contactList.getTotalPages());
        return "normal/show_contacts";
    }

   @GetMapping("/{cId}/contact")
    public String showContactDetail(@PathVariable("cId") Integer cId,Model model,Principal principal){
       Optional<Contact> contactOptional = contactRepository.findById(cId);
       Contact contact = contactOptional.get();

       String userName = principal.getName();
       User user = userRepository.getUserByUserName(userName);

       if(user.getUserId() == contact.getUser().getUserId()) {
        model.addAttribute("title",contact.getName());
           model.addAttribute("contact", contact);
       }

       return "normal/contact_detail";
   }

    @GetMapping("/delete/{cId}")
    public String deleteContact(@PathVariable("cId") Integer cId,Model model,HttpSession session){
        Optional<Contact> optionalContact = contactRepository.findById(cId);
        Contact contact = optionalContact.get();
        contact.setUser(null);
        contactRepository.delete(contact);
        session.setAttribute("message",new Message("Contact deleted succesfully...","success"));

        return "redirect:/user/show-contacts/0";
    }
}
