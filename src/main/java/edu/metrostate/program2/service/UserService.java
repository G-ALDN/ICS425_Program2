package edu.metrostate.program2.service;

import edu.metrostate.program2.model.AppUser;
import edu.metrostate.program2.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepo userRepo;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> user = userRepo.findByUsername(username);

        if (user.isPresent()) {
            var appUser = user.get();
            return User.builder()
                    .username(appUser.getUsername())
                    .password(appUser.getPassword())
                    .roles(getRoles(appUser))
                    .build();
        }else{
            throw new UsernameNotFoundException(username);
        }
    }

    private String[] getRoles(AppUser user) {
        return user.getRoles().split(",");
    }
}
