package pl.oskarpolak.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.oskarpolak.UserRepository;
import pl.oskarpolak.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

/**
 * Created by OskarPraca on 2017-01-22.
 */

@Controller
@SessionAttributes("name")
public class UserController {

    @Autowired
    UserRepository userRepository;



    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String indexGet() {
        return "dashboard";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String indexPost(@ModelAttribute User user) {

        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>)    SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for(SimpleGrantedAuthority s : authorities){
            System.out.println("Autoryzacja: " + s.getAuthority());
        }


        return "dashboard";
    }


    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerGet() {
        return "dashboard";
    }

    // TODO Walidacja
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerPost(@ModelAttribute("user") User user){
        user.setRole("USER");
        if(userRepository.findByUsername(user.getUsername()) == null){
            user.setPassword(new ShaPasswordEncoder().encodePassword(user.getPassword(), null));
            userRepository.save(user);
        }
        return "register";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginGET(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        model.addAttribute("user", new User());
        return "login";
    }





     @RequestMapping("/all")
     @ResponseBody
    public Iterable<User> allUsers(){
        return userRepository.findAll();
     }

     @RequestMapping(value = "/rest/user/{username}", method = RequestMethod.GET)
     public ResponseEntity restUser(@PathVariable("username") String id) {
        return new ResponseEntity(userRepository.findByUsername(id), HttpStatus.OK);
     }


    @RequestMapping(value = "/rest/adduser", method = RequestMethod.POST)
    public ResponseEntity restUser(@RequestBody(required = false) User auth) {
        userRepository.save(auth);
        System.out.println("Wykonano!");
        return new ResponseEntity(auth, HttpStatus.OK);
    }






}
