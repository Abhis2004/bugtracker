package com.example.bugtracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bugs")
@CrossOrigin("*")
public class BugController {

    @Autowired
    private BugRepository repo;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // CREATE
    @PostMapping
    public Bug createBug(@RequestBody Bug bug) {
        Bug saved = repo.save(bug);
        messagingTemplate.convertAndSend("/topic/bugs", repo.findAll());
        return saved;
    }

    // READ ALL
    @GetMapping
    public List<Bug> getAllBugs() {
        return repo.findAll();
    }

    // UPDATE STATUS
    @PutMapping("/{id}")
    public Bug updateBug(@PathVariable int id, @RequestBody Bug updatedBug) {
        Bug bug = repo.findById(id).orElseThrow();
        bug.setStatus(updatedBug.getStatus());
        Bug saved = repo.save(bug);
        messagingTemplate.convertAndSend("/topic/bugs", repo.findAll());
        return saved;
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void deleteBug(@PathVariable int id) {
        repo.deleteById(id);
        messagingTemplate.convertAndSend("/topic/bugs", repo.findAll());
    }
}