package pl.kozhanov.TaskManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.kozhanov.TaskManager.domain.Role;
import pl.kozhanov.TaskManager.domain.User;
import pl.kozhanov.TaskManager.repos.UserRepo;
import pl.kozhanov.TaskManager.service.UserService;

import javax.validation.Valid;
import java.util.Map;

/**
 * @author Anton Kozhanov
 * Class provides couple of controller methods to manage Admin Panel
 */

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {


    private static final String LOGGED_USER = "loggedUser";
    private static final String ROLES = "roles";
    private static final String USER = "user";
    private static final String USER_EDIT = "userEdit";
    private static final String USERS = "users";

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;

    @GetMapping
    public String userList(Model model){
        model.addAttribute(USERS, userService.findAll());
        model.addAttribute(LOGGED_USER, userService.getCurrentLoggedInUsername());
        return "adminPanel";
    }

    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model){
        model.addAttribute(USER, user);
        model.addAttribute(ROLES, Role.values());
        model.addAttribute(LOGGED_USER, userService.getCurrentLoggedInUsername());
        return USER_EDIT;
    }

    @PostMapping("/addUser")
    public String addUser(@Valid User user, BindingResult result, Model model)
    {
        if(result.hasErrors())
        {
            Map<String, String> errorsMap = ControllerUtils.getErrors(result);
            model.mergeAttributes(errorsMap);
        }
        else {
            userService.addUser(user);
            model.addAttribute("responseMessage", "success");
        }
        model.addAttribute(USERS, userService.findAll());
        model.addAttribute(LOGGED_USER, userService.getCurrentLoggedInUsername());
        return "adminPanel";
    }

    @GetMapping("delete/{user}")
    public String deleteUser(@PathVariable User user, Model model){
        userRepo.delete(user);
        model.addAttribute(USERS, userService.findAll());

        return "redirect:/admin";
    }


    @PostMapping("/save")
    public String saveUser(
            @RequestParam String newUsername,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user,
            Model model){
        if(newUsername.equals(""))
            {
                model.addAttribute("usernameError", "Username field can't be empty");
            }
        else if(userService.findAll().contains(userService.findByUsername(newUsername)))
            {
                model.addAttribute("usernameError", "Username already exists");
            }
        else
            {
                userService.saveUser(user, newUsername, form);
                model.addAttribute("responseMessage", "success");
            }

        model.addAttribute(USER, user);
        model.addAttribute(ROLES, Role.values());
        model.addAttribute(LOGGED_USER, userService.getCurrentLoggedInUsername());
        return USER_EDIT;
    }

    @GetMapping("resetUserPassword/{user}")
    public String resetUserPassword(@PathVariable User user, Model model){
        userService.resetUserPassword(user);
        model.addAttribute(USER, user);
        model.addAttribute(ROLES, Role.values());
        model.addAttribute(LOGGED_USER, userService.getCurrentLoggedInUsername());
        model.addAttribute("resetResponseMessage", "success");
        return USER_EDIT;
    }

}
