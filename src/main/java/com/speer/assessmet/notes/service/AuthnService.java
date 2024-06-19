package com.speer.assessmet.notes.service;

import com.speer.assessmet.notes.dto.LoginDto;
import com.speer.assessmet.notes.dto.SignUpDto;
import com.speer.assessmet.notes.entity.User;
import com.speer.assessmet.notes.repository.IUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class AuthnService {

    @Autowired
    IUsersRepository repository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public void signup(SignUpDto dto) throws Exception {
        User user = repository.findByEmail(dto.getEmailId());

        if (user != null) throw new Exception("User Exists");

        user = User.builder()
                .email(dto.getEmailId())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();

        repository.save(user);

    }

    public boolean validCredential(String email, String hashedPassword) {

        User user = repository.findByEmail(email);
        return user.getEmail().equals(email) && user.getPassword().equals(hashedPassword);
    }

    public String login(LoginDto dto) throws Exception {
        User user = repository.findByEmail(dto.getEmailId());

        if (user == null) throw new Exception("User Doesn't Exists");

        String strUserToEncode = user.getEmail() + ":" + user.getPassword();

        return new String(Base64.getEncoder().encode(strUserToEncode.getBytes(StandardCharsets.UTF_8)));
    }

    public User getUserForEmail(String email) {
        return repository.findByEmail(email);
    }
}
