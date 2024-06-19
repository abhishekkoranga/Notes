package com.speer.assessmet.notes.repository;

import com.speer.assessmet.notes.entity.Note;
import com.speer.assessmet.notes.entity.ShareNoteId;
import com.speer.assessmet.notes.entity.SharedNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IShareNotesRepository extends JpaRepository<SharedNote, ShareNoteId> {

    @Query("SELECT e.note from share_note e WHERE e.user.id=:userId")
    List<Note> findAllNotesByUserId(Long userId);

    @Query("SELECT e.note from share_note e WHERE e.user.id=:userId AND e.note.id=:id")
    Note findNoteByUserIdAndNoteId(Long userId, Long id);
}
