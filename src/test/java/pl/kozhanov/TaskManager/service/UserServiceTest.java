package pl.kozhanov.TaskManager.service;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.kozhanov.TaskManager.domain.Role;
import pl.kozhanov.TaskManager.domain.User;
import pl.kozhanov.TaskManager.repos.UserRepo;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

class UserServiceTest {

    private UserRepo userRepo = mock(UserRepo.class);

    private PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

    private SecurityContextHolder securityContextHolder = spy(SecurityContextHolder.class);

    private UserService userService = new UserService(userRepo, passwordEncoder);


    @Test
    void addUserTest() {

        User user = new User();
        user.setUsername("Nick");
        userService.addUser(user);
        assertTrue(user.isActive());
        assertTrue(CoreMatchers.is(user.getRoles()).matches(Collections.singleton(Role.USER)));
        Mockito.verify(userRepo, Mockito.times(1)).save(user);
    }

    @Test
    void saveUserTest() {
        User user = new User();
        user.setRoles(new HashSet<>());
        String newUsername = "newName";
        Map<String, String> form = new HashMap<>();
        form.put("ADMIN", "ADMIN");
        userService.saveUser(user, newUsername, form);
        assertEquals("newName", user.getUsername());
        assertTrue(CoreMatchers.is(user.getRoles()).matches(Collections.singleton(Role.ADMIN)));
    }


    @Test
    void getCurrentLoggedInUsername_ifUserLoggedIn_shoudBeNotNull() {
        Mockito.doReturn(UserDetails.class).when(securityContextHolder).getContext().getAuthentication();
        assertNotNull(userService.getCurrentLoggedInUsername());
    }

    @Test
    void getCurrentLoggedInUsername_ifNotLoggedIn_shouldNoUserLoggedIn() {
        assertEquals("No user logged in", userService.getCurrentLoggedInUsername());
    }

    @Test
    void changeUserPassword_ifPasswordEncodeCorrect_shouldReturn123() {
        User user = new User();
        String username = "Nick";
        String password = "123";
        Mockito.when(userRepo.findByUsername(username)).thenReturn(user);
        Mockito.when(passwordEncoder.encode(password)).thenReturn("321");
        userService.changeUserPassword(username, password);
        assertEquals("321", user.getPassword());
    }

    @Test
    void resetUserPassword_ifUsernameIsNick_passwordShoulBeNick() {
        User user = new User();
        user.setUsername("Nick");
        Mockito.when(passwordEncoder.encode(user.getUsername())).thenReturn(user.getUsername());
        userService.resetUserPassword(user);
        assertEquals("Nick", user.getPassword());
    }
}