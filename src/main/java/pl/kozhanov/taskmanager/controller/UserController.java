package pl.kozhanov.taskmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.kozhanov.taskmanager.service.UserService;

/**
 * @author Anton Kozhanov
 * User profile functionality
 */
@Controller
@PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
public class UserController {

    private static final String LOGGED_USER = "loggedUser";
    private static final String USER_PROFILE = "userProfile";
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("user/{username}")
    public String userProfile(@PathVariable String username, Model model) {
        if (userService.getCurrentLoggedInUsername().equals(username)) {
            model.addAttribute(LOGGED_USER, userService.getCurrentLoggedInUsername());
            return USER_PROFILE;
        } else {
            model.addAttribute("enteredUsername", username);
            return "user403";
        }
    }

    @PostMapping("/changeUserPassword")
    public String changeUserPassword(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("passwordConfirm") String passwordConfirm, Model model) {
        boolean hasErrors = false;
        if (StringUtils.isEmpty(password)) {
            model.addAttribute("passwordError", "Password can't be empty");
            hasErrors = true;
        }
        if (StringUtils.isEmpty(passwordConfirm)) {
            model.addAttribute("password2Error", "Password confirmation can't be empty");
            hasErrors = true;
        }
        if (password != null && !password.equals(passwordConfirm)) {
            model.addAttribute("passwordDifferentError", "Passwords are different");
            hasErrors = true;
        }

        if (hasErrors) {
            model.addAttribute(LOGGED_USER, userService.getCurrentLoggedInUsername());
            return USER_PROFILE;
        } else {
            userService.changeUserPassword(username, password);
            model.addAttribute(LOGGED_USER, userService.getCurrentLoggedInUsername());
            model.addAttribute("responseMessage", "success");
            return USER_PROFILE;
        }
    }
}
