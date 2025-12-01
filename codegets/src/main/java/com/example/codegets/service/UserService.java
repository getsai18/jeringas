package com.example.codegets.service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.example.codegets.dto.CreateUserDTO;
import com.example.codegets.model.User;

@Service
public class UserService {

    private final Map<Long, User> storage = new LinkedHashMap<>();
    private final AtomicLong idGen = new AtomicLong(1);

    // Constructor para poblar datos iniciales
    public UserService() {
        save(new CreateUserDTO("Ana Perez", "ana@example.com"));
        save(new CreateUserDTO("Carlos Lopez", "carlos@example.com"));
        save(new CreateUserDTO("Daniela Ruiz", "daniela@example.com"));
    }

    public User save(CreateUserDTO dto) {
        Long id = idGen.getAndIncrement();
        User u = new User(id, dto.getName(), dto.getEmail());
        storage.put(id, u);
        return u;
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<User> findAll(int page, int size, String search) {
        List<User> all = new ArrayList<>(storage.values());
        if (search != null && !search.isBlank()) {
            String s = search.toLowerCase();
            all = all.stream()
                .filter( User u -> u.getName().toLowerCase().contains(s) || u.getEmail().toLowerCase().contains(s))
                .collect(Collectors.toList());
        }
        int from = page * size;
        if (from >= all.size()) return Collections.emptyList();
        int to = Math.min(from + size, all.size());
        return all.subList(from, to);
    }




    public Optional<User> update(Long id, CreateUserDTO dto) {
        User existing = storage.get(id);
        if (existing == null) return Optional.empty();
        existing.setName(dto.getName());
        existing.setEmail(dto.getEmail());
        return Optional.of(existing);
    }
}