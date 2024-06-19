package com.speer.assessmet.notes.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShareNoteId implements Serializable {

    private Long userId;
    private Long noteId;

}
