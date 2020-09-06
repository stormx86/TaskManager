package pl.kozhanov.TaskManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.kozhanov.TaskManager.repos.TaskRepo;

/**
 * @author Anton Kozhanov
 * Delete tasks functionality
 */
@Controller
@RequestMapping("/editTask")
@PreAuthorize("hasAuthority('ADMIN')")
public class EditTaskController {

    @Autowired
    private TaskRepo taskRepo;

    @GetMapping("delete/{taskId}")
    public String deleteUser(@PathVariable String taskId) {
        taskRepo.delete(taskRepo.findById(Integer.parseInt(taskId)));
        return "redirect:/";
    }

    @PostMapping("deleteAll")
    public String deleteAll() {
        taskRepo.deleteAll();
        return "redirect:/";
    }

}
