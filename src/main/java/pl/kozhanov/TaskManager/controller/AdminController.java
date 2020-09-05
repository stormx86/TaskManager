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

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;

    @GetMapping
    public String userList(Model model){
        model.addAttribute("users", userService.findAll());
        model.addAttribute("loggedUser", userService.getCurrentLoggedInUsername());
        return "adminPanel";
    }

    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model){
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        model.addAttribute("loggedUser", userService.getCurrentLoggedInUsername());
        return  "userEdit";
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
        model.addAttribute("users", userService.findAll());
        model.addAttribute("loggedUser", userService.getCurrentLoggedInUsername());
        return "adminPanel";
    }

    @GetMapping("delete/{user}")
    public String deleteUser(@PathVariable User user, Model model){
        userRepo.delete(user);
        model.addAttribute("users", userService.findAll());

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

        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        model.addAttribute("loggedUser", userService.getCurrentLoggedInUsername());
        return "userEdit";
    }

    @GetMapping("resetUserPassword/{user}")
    public String resetUserPassword(@PathVariable User user, Model model){
        userService.resetUserPassword(user);
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        model.addAttribute("loggedUser", userService.getCurrentLoggedInUsername());
        model.addAttribute("resetResponseMessage", "success");
        return "userEdit";
    }

}
