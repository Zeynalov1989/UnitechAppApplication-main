package com.unibank.UnitechAppApplication.service;

import com.unibank.UnitechAppApplication.model.User;
import com.unibank.UnitechAppApplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String pin) throws UsernameNotFoundException {
        User user = userRepository.findByPin(pin)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with the provided PIN"));

        return new org.springframework.security.core.userdetails.User(
            user.getPin(),
            user.getPassword(),
            Collections.emptyList()
        );
    }
}
