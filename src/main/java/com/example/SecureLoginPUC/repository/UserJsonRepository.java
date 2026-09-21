package com.example.SecureLoginPUC.repository;

import com.example.SecureLoginPUC.model.User;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Repository
public class UserJsonRepository {

    private final ObjectMapper mapper;
    private final File file;

    public UserJsonRepository(ObjectMapper mapper, @Value("${app.users.file}") String path) {
        this.mapper = mapper;
        this.file = new File(path);
    }

    public synchronized List<User> findAll() {
        if (!file.exists()) {
            return new ArrayList<>();
        }
        return mapper.readValue(file, new TypeReference<List<User>>() {});
    }

    public synchronized void saveAll(List<User> users) {
        File folder = file.getParentFile();
        if (folder != null) {
            folder.mkdirs();
        }
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, users);
    }

    public synchronized void save(User user) {
        List<User> users = findAll();
        users.removeIf(u -> u.getUsername().equalsIgnoreCase(user.getUsername()));
        users.add(user);
        saveAll(users);
    }

    public Optional<User> findByUsername(String username) {
        return findAll().stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst();
    }

    public Optional<User> findByEmail(String email) {
        return findAll().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }
}
