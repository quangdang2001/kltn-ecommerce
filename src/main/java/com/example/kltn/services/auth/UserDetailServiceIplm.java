package com.example.kltn.services.auth;

import com.example.kltn.models.User;
import com.example.kltn.repos.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserDetailServiceIplm implements UserDetailsService {
    private final UserRepo usersRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = usersRepo.findUserByEmail(username);
        if (user!=null)
            return new UserDetailIplm(user);
        else{
            user = usersRepo.findUserByPhone(username);
            if (user!=null)
                return new UserDetailIplm(user);
            else{
                throw new BadCredentialsException(null);
            }
        }

    }

}
