package com.transtu.reclamationservice.security;

import com.transtu.reclamationservice.clients.UserRestClient;
import com.transtu.reclamationservice.models.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRestClient userRestClient;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = userRestClient.getUserByUsername(username);
        if(appUser==null) throw new UsernameNotFoundException(String.format("User %s not found",username));

        //String[] roles = appUser.getRoles().stream().map(u -> u.getRole()).toArray(String[]::new);
        List<SimpleGrantedAuthority> authorities = appUser.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getRole())).collect(Collectors.toList());
        UserDetails userDetails= User
                .withUsername(appUser.getUsername())
                .password(appUser.getPassword())
                .authorities(authorities).build();
                //.roles(roles).build();
        return userDetails;
    }
}
