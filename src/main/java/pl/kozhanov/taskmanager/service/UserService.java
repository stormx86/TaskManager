package pl.kozhanov.taskmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.kozhanov.taskmanager.domain.Role;
import pl.kozhanov.taskmanager.domain.User;
import pl.kozhanov.taskmanager.repos.UserRepo;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getUsername()));
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepo.save(user);
    }

    public void saveUser(User user, String newUsername, Map<String, String> form) {
        user.setUsername(newUsername);
        Set<String> roles = Arrays.stream(Role.values()).map(Role::name).collect(Collectors.toSet());
        if (user.getRoles() != null) {
            user.getRoles().clear();
        }
        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }
        userRepo.save(user);
    }

    public String getCurrentLoggedInUsername() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) return "No user logged in";
        else {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                return ((UserDetails) principal).getUsername();
            } else {
                return principal.toString();
            }
        }
    }

    public void changeUserPassword(String username, String password) {
        User user = userRepo.findByUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        userRepo.save(user);
    }

    public void resetUserPassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getUsername()));
        userRepo.save(user);
    }

}
