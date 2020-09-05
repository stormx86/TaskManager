package pl.kozhanov.TaskManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.kozhanov.TaskManager.domain.Task;
import pl.kozhanov.TaskManager.service.TaskParserService;
import pl.kozhanov.TaskManager.repos.TaskRepo;

import org.springframework.data.domain.Pageable;
import pl.kozhanov.TaskManager.service.TaskViewProjection;
import pl.kozhanov.TaskManager.service.UserService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

@Controller
public class MainController {

    @Autowired
    private TaskRepo taskRepo;

    @Autowired
    private TaskParserService taskParserService;

    @Autowired
    private UserService userService;


    @GetMapping("/")
    public String index(Model model, Pageable pageable) {

        Page<TaskViewProjection> page = taskRepo.findAllByOrderByReceivedAtDesc(pageable);
        model.addAttribute("page", page);
        model.addAttribute("url", "/");
        model.addAttribute("loggedUser", userService.getCurrentLoggedInUsername());
        return "main";
    }

    @PostMapping("/refresh")
    public String refresh(Map<String, Object> model) throws IOException, GeneralSecurityException {
        for (Task t : taskParserService.getTask()) {
            taskRepo.save(t);
            System.out.println("Task saved");
        }
        return "redirect:/";
    }


    @PostMapping(value = "changestatus")
    @ResponseBody
    public String[] changeStatus(@RequestParam("id") String id, Map<String, Object> model) {
        Task task = taskRepo.findById(Integer.parseInt(id));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (task.getStatus().equals("Waiting")) {
            task.setStatus("Processing");
            task.setEditBy(auth.getName());
            taskRepo.save(task);
        } else if (task.getStatus().equals("Processing") && task.getEditBy().equals(auth.getName())) {
            task.setStatus("Waiting");
            task.setEditBy("");
            taskRepo.save(task);
        }
        return new String[]{task.getStatus(), task.getEditBy()};
    }

    @PostMapping(value = "checktask")
    @ResponseBody
    public String checkTask() throws IOException, GeneralSecurityException {
        if (taskParserService.checkTask()) {
            return ("New Task available!");
        }
        return ("");
    }


}
