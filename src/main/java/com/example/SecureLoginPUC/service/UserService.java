package com.example.SecureLoginPUC.service;

import com.example.SecureLoginPUC.model.User;
import com.example.SecureLoginPUC.repository.UserJsonRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    private final UserJsonRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserJsonRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(String username, String email, String password, String confirmPassword) {
        if (isBlank(username) || isBlank(email) || isBlank(password) || isBlank(confirmPassword)) {
            throw new IllegalArgumentException("Preencha todos os campos.");
        }
        if (!email.matches(EMAIL_REGEX)) {
            throw new IllegalArgumentException("Informe um email válido.");
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException("A senha deve ter no mínimo 6 caracteres.");
        }
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("As senhas não conferem.");
        }
        if (repository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Este usuário já está cadastrado.");
        }
        if (repository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Este email já está cadastrado.");
        }
        repository.save(new User(username, email, passwordEncoder.encode(password), "USER"));
    }

    public void updatePassword(String email, String password, String confirmPassword) {
        if (isBlank(password) || isBlank(confirmPassword)) {
            throw new IllegalArgumentException("Preencha todos os campos.");
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException("A senha deve ter no mínimo 6 caracteres.");
        }
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("As senhas não conferem.");
        }
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
        user.setPassword(passwordEncoder.encode(password));
        repository.save(user);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
