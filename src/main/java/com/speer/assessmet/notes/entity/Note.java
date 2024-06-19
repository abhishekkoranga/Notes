package com.speer.assessmet.notes.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity(name = "note")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(
       indexes = {
               @Index(name = "idx_user_id_content", columnList = "user_id, content"),
               @Index(name = "idx_user_id", columnList = "user_id")
       }
)
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "NoteIdSeqGen", allocationSize = 1)
    private Long id;

    private Long userId;

    @Column(columnDefinition = "TEXT")
    private String content;

//    @OneToMany(mappedBy = "note", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
//    Set<SharedNote> sharedNotes;

}
