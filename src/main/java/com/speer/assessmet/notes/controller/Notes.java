package com.speer.assessmet.notes.controller;

import com.speer.assessmet.notes.dto.NotesDto;
import com.speer.assessmet.notes.dto.NotesShareDto;
import com.speer.assessmet.notes.entity.Note;
import com.speer.assessmet.notes.service.NotesService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
@RateLimiter(name = "rtr")
public class Notes {

    @Autowired
    NotesService notesService;


    @GetMapping("/notes")
    public List<Note> getNotes(Authentication authentication) {
        return notesService.getNotes((String) authentication.getPrincipal());
    }

    @GetMapping("/notes/{id}")
    public Note getNote(@PathVariable Long id, Authentication authentication) {
        return notesService.getNote(id, (String) authentication.getPrincipal());
    }

    @PutMapping("/notes/{id}")
    public Note putNote(@PathVariable Long id, @RequestBody NotesDto dto, Authentication authentication) throws Exception {
        return notesService.putNote(id, dto, (String) authentication.getPrincipal());
    }

    @PostMapping("/notes")
    public Note createNote(@RequestBody NotesDto dto, Authentication authentication) {
        return notesService.createNote(dto, (String) authentication.getPrincipal());
    }

    @PostMapping("/notes/{id}/share")
    public void shareNotes(@PathVariable Long id, @RequestBody NotesShareDto dto, Authentication authentication) throws Exception {
        notesService.shareNotes(id, dto, (String) authentication.getPrincipal());
    }

    @GetMapping("/search")
    public List<Note> searchNotes(@RequestParam String q, Authentication authentication) {
        return notesService.searchNotes(q, (String) authentication.getPrincipal());
    }

    @DeleteMapping("/notes/{id}")
    public void deleteNote(@PathVariable Long id, Authentication authentication) throws Exception {
        notesService.deleteNote(id, (String) authentication.getPrincipal());
    }

}
