package com.speer.assessmet.notes.service;

import com.speer.assessmet.notes.dto.NotesDto;
import com.speer.assessmet.notes.dto.NotesShareDto;
import com.speer.assessmet.notes.entity.Note;
import com.speer.assessmet.notes.entity.ShareNoteId;
import com.speer.assessmet.notes.entity.SharedNote;
import com.speer.assessmet.notes.entity.User;
import com.speer.assessmet.notes.repository.INotesRepository;
import com.speer.assessmet.notes.repository.IShareNotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class NotesService {

    @Autowired
    INotesRepository notesRepository;

    @Autowired
    IShareNotesRepository shareNotesRepository;

    @Autowired
    AuthnService authnService;

    public List<Note> getNotes(String email) {

        Long userId = authnService.getUserForEmail(email).getId();
        List<Note> notesOwned = notesRepository.findAllByUserId(userId);

        List<Note> sharedNotes = shareNotesRepository.findAllNotesByUserId(userId);

        notesOwned.addAll(sharedNotes);

         return notesOwned;
    }

    public Note getNote(Long id, String email) {
        Long userId = authnService.getUserForEmail(email).getId();
        Note note = notesRepository.findByIdAndUserId(id, userId);

        if (note != null) return note;

        return shareNotesRepository.findNoteByUserIdAndNoteId(userId, id);
    }

    public Note createNote(NotesDto dto, String email) {
        Long userId = authnService.getUserForEmail(email).getId();

        Note note = Note.builder()
                .userId(userId)
                .content(dto.getContent())
                .build();

        return notesRepository.save(note);

    }

    public Note putNote(Long id, NotesDto dto, String email) throws Exception {
        Note note = getNote(id, email);

        if (note == null) throw new Exception("Note with id "+id+" doesn't exist");
        if(!Objects.equals(note.getUserId(), authnService.getUserForEmail(email).getId())) throw new Exception("Forbidden Operation!");

        note.setContent(dto.getContent());
        return notesRepository.save(note);
    }

    public void deleteNote(Long id, String email) throws Exception {
        Long userId = authnService.getUserForEmail(email).getId();
        Note note = notesRepository.findByIdAndUserId(id, userId);

        if (note == null) throw new Exception("Note no longer exist to delete");
        if(!Objects.equals(note.getUserId(), userId)) throw new Exception("Forbidden Operation!");

         notesRepository.deleteById(id);
    }


    public List<Note> searchNotes(String query, String email) {
        Long userId = authnService.getUserForEmail(email).getId();
        return notesRepository.findByQuery(query, userId);
    }

    public void shareNotes(Long id, NotesShareDto dto, String email) throws Exception {
        User toShareUser = authnService.getUserForEmail(dto.getUserEmail());

        if (toShareUser == null) throw new Exception("Note can't be shared as User "+ dto.getUserEmail() + " Doesn't exit");

        User user = authnService.getUserForEmail(email);
        if (toShareUser.getEmail().equals(user.getEmail())) throw new Exception("Operation Forbidden");

        Long userId = user.getId();

        Note note = getNoteByOwnership(id, userId);

        ShareNoteId shareNoteId = ShareNoteId.builder()
                .noteId(note.getId())
                .userId(toShareUser.getId())
                .build();

        SharedNote sharedNote = SharedNote.builder()
                .shareNoteId(shareNoteId)
                .note(note)
                .user(toShareUser)
                .build();

        shareNotesRepository.save(sharedNote);

    }

    private Note getNoteByOwnership(Long id, Long userId) throws Exception {
        Note note = notesRepository.getReferenceById(id);

        if (note == null) throw new Exception("Note with id doesn't exist");

        if(!Objects.equals(note.getUserId(), userId)) throw new Exception("Forbidden Operation!");

        return note;

    }
}
