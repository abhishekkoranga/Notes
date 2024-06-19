package com.speer.assessmet.notes.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity(name = "share_note")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class SharedNote {

    @EmbeddedId
    private ShareNoteId shareNoteId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "noteId", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Note note;
}
