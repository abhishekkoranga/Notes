package com.speer.assessmet.notes.repository;

import com.speer.assessmet.notes.entity.Note;
import com.speer.assessmet.notes.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface INotesRepository extends JpaRepository<Note, Long> {

    List<Note> findAllByUserId(Long userId);

    Note findByIdAndUserId(Long id, Long userId);

    @Query("SELECT e FROM note e WHERE e.userId=:userId AND LOWER(e.content) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Note> findByQuery(String query, Long userId);

}