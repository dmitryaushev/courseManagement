package com.courses.management.user.auth;

import com.courses.management.user.User;
import com.courses.management.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UsersDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public UsersDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getByEmail(username).orElseThrow(() ->
                new UsernameNotFoundException(String.format("User with username %s not exists", username)));
        return new UserPrincipal(user);
    }
}
