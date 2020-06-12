package pl.kozhanov.TaskManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.kozhanov.TaskManager.domain.Task;
import pl.kozhanov.TaskManager.domain.TaskParser;
import pl.kozhanov.TaskManager.repos.TaskRepo;

import org.springframework.data.domain.Pageable;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

@Controller
public class MainController {

    @Autowired
    private TaskRepo taskRepo;

    @Autowired
    private TaskParser taskParser;


    @GetMapping("/")
    public String index(Map<String, Object> model, Pageable pageable){

        Page<Task> page = taskRepo.findAllByOrderByReceivedAtDesc(pageable);
        model.put("page", page);
        model.put("url", "/");
        return "main";
    }

    @PostMapping("refresh")
    public String refresh(Map<String, Object> model) throws IOException, GeneralSecurityException {
        for (Task t : taskParser.getTask()) {
            taskRepo.save(t);
            System.out.println("Task saved");
        }
        /*Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth.getName());*/
        return "redirect:/";
    }


    @PostMapping(value="changestatus")
    @ResponseBody
    public String[] changeStatus(@RequestParam("id") String id, Map<String, Object> model) {
       Task task = taskRepo.findById(Integer.parseInt(id));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

       if(task.getStatus().equals("Waiting")) {
           task.setStatus("Processing");
           task.setEditBy(auth.getName());
           taskRepo.save(task);
       }
       else if(task.getStatus().equals("Processing") && task.getEditBy().equals(auth.getName()))  {
           task.setStatus("Waiting");
           task.setEditBy("");
           taskRepo.save(task);
       }
        return new String[]{task.getStatus(), task.getEditBy()};
    }

    @PostMapping(value = "checktask")
    @ResponseBody
    public String checkTask() throws IOException, GeneralSecurityException{
        if(taskParser.checkTask()){
            return ("New Task available!");
        }
            return ("");
    }



}
