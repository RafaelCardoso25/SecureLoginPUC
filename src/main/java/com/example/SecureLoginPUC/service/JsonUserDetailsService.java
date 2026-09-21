package com.example.SecureLoginPUC.service;

import com.example.SecureLoginPUC.model.User;
import com.example.SecureLoginPUC.repository.UserJsonRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JsonUserDetailsService implements UserDetailsService {

    private final UserJsonRepository repository;

    public JsonUserDetailsService(UserJsonRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = repository.findByUsername(login)
                .or(() -> repository.findByEmail(login))
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + login));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
}
