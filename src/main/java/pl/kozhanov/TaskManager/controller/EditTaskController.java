package pl.kozhanov.TaskManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.kozhanov.TaskManager.repos.TaskRepo;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

@Controller
@RequestMapping("/editTask")
@PreAuthorize("hasAuthority('ADMIN')")
public class EditTaskController {

    @Autowired
    private TaskRepo taskRepo;

    @GetMapping("delete/{taskId}")
    public String deleteUser(@PathVariable String taskId, Model model) {
        taskRepo.delete(taskRepo.findById(Integer.parseInt(taskId)));
        return "redirect:/";
    }

    @PostMapping("deleteAll")
    public String deleteAll(Map<String, Object> model) throws IOException, GeneralSecurityException {
        taskRepo.deleteAll();
        return "redirect:/";
    }

}
