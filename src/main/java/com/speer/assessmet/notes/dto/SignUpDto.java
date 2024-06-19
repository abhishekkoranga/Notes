package com.speer.assessmet.notes.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
public class SignUpDto extends LoginDto {

    @Builder
    public SignUpDto(String emailId, String password) {
        super(emailId, password);
    }
}
