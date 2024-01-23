package ru.safonoviv.lms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.safonoviv.lms.dto.RegistrationUserDto;
import ru.safonoviv.lms.entities.User;
import ru.safonoviv.lms.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
	private final UserRepository userRepository;
    private final RoleService roleService;
    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @CacheEvict(cacheNames="userCache",key = "#email")
    public Optional<User> loadByUsername(String email) {
        return userRepository.findByEmail(email);
    }

    @Cacheable(cacheNames = {"userCache"},key = "#name")
    public Optional<User> findByUsername(String name) {
        return userRepository.findByName(name);
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = loadByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
        return new org.springframework.security.core.userdetails.User(
                user.getName(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
        );
    }
    
    @Transactional
    public User createNewUser(RegistrationUserDto registrationUserDto) {
        User user = User.builder()
        		.email(registrationUserDto.getEmail())
        		.name(registrationUserDto.getName())
        		.password(passwordEncoder.encode(registrationUserDto.getPassword()))
        		.build();
        user.setRoles(List.of(roleService.getUserRole()));
        return userRepository.save(user);
    }



}
