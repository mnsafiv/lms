package ru.safonoviv.lms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.safonoviv.lms.entities.Role;
import ru.safonoviv.lms.entities.RoleType;
import ru.safonoviv.lms.repository.RoleRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role getUserRole() {
        return roleRepository.findByName(RoleType.USER.name()).get();
    }
}
