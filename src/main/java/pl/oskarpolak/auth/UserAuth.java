package pl.oskarpolak.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.oskarpolak.UserRepository;
import pl.oskarpolak.model.User;

import java.util.Arrays;

/**
 * Created by OskarPraca on 2017-02-19.
 */

@Service
public class UserAuth implements UserDetailsService {

    @Autowired(required = true)
    UserRepository userRepository;


    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null ) throw new UsernameNotFoundException("Użytkownik " + username + " nie został odnaleziony");

        System.out.println("Znalazłem użytkownika : " + username);

        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());
        org.springframework.security.core.userdetails.User springUser = new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(), Arrays.asList(authority));
        UserDetails details = (UserDetails) springUser;


        return details;
    }




}
