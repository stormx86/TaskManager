package pl.kozhanov.taskmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.kozhanov.taskmanager.repos.TaskRepo;
import pl.kozhanov.taskmanager.service.TaskParserService;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * @author Anton Kozhanov
 * Delete tasks functionality
 */
@Controller
@RequestMapping("/editTask")
@PreAuthorize("hasAuthority('ADMIN')")
public class EditTaskController {

    private TaskRepo taskRepo;
    private TaskParserService taskParserService;

    @Autowired
    public EditTaskController(TaskRepo taskRepo, TaskParserService taskParserService) {
        this.taskRepo = taskRepo;
        this.taskParserService = taskParserService;
    }

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

    @GetMapping("getAttachment/{messageId}")
    @ResponseBody
    public ResponseEntity<Resource> getAttachment(@PathVariable String messageId) throws GeneralSecurityException,
            IOException {
        HttpHeaders headers = new HttpHeaders();
        String fName = taskParserService.getZipAttachment(messageId).getFileName();
        String fNameHeaderValue = "attachment;" + " " + "filename=" + fName;
        headers.add(HttpHeaders.CONTENT_DISPOSITION, fNameHeaderValue);
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(taskParserService.getZipAttachment(messageId).getBody().length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new ByteArrayResource(taskParserService.getZipAttachment(messageId).getBody()));
    }
}
