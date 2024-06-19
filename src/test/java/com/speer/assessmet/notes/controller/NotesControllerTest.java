package com.speer.assessmet.notes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.speer.assessmet.notes.dto.SignUpDto;
import com.speer.assessmet.notes.entity.Note;
import com.speer.assessmet.notes.entity.User;
import com.speer.assessmet.notes.repository.INotesRepository;
import com.speer.assessmet.notes.repository.IUsersRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
//@ContextConfiguration(classes = {NotesAppApplication.class})
//@WebMvcTest(Authn.class)
@ComponentScan(basePackages = "com.speer.assessmet.notes")
public class NotesControllerTest {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Random random = new Random();
    private static final String AUTHORIZATION_HEADER = "Authorization";
    @Autowired
    INotesRepository notesRepository;
    @Autowired
    IUsersRepository usersRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mvc;

    private static final List<EmailWithToken> users = new ArrayList<>();

    @Before
    public void InitUsersWithTokens() throws Exception {
        User user1 = User.builder()
                .email("someone@xyz.com")
                .password(passwordEncoder.encode("root123"))
                .build();

        User user2 = User.builder()
                .email("someemail@xyz.com")
                .password(passwordEncoder.encode("root123"))
                .build();

        usersRepository.saveAll(List.of(user1, user2));

        ResultActions response = mvc
                .perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(SignUpDto.builder()
                                .emailId(user1.getEmail())
                                .password("root123")
                                .build())));


        EmailWithToken emailWithToken = new EmailWithToken();
        emailWithToken.email = user1.getEmail();
        emailWithToken.token = "Basic " + response.andReturn().getResponse().getHeader(AUTHORIZATION_HEADER);

        users.add(emailWithToken);

        response = mvc
                .perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(SignUpDto.builder()
                                .emailId(user2.getEmail())
                                .password("root123")
                                .build())));

        emailWithToken = new EmailWithToken();
        emailWithToken.email = user2.getEmail();
        emailWithToken.token = "Basic " + response.andReturn().getResponse().getHeader(AUTHORIZATION_HEADER);
        users.add(emailWithToken);
    }

    @Test
    @WithMockUser(username = "someone@xyz.com")
    public void givenAuthenticatedUser_whenGetNotes_thenStatus200() throws Exception {

        Long userId = usersRepository.findByEmail(users.get(0).email).getId();

        Note note = Note.builder()
                .userId(userId)
                .content("Some Random Content for Notes")
                .build();

        note = notesRepository.save(note);

        ResultActions response = mvc
                .perform(MockMvcRequestBuilders.get("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER, users.get(0).token)

                );
        String strBodyResponseExpected = mapper.writeValueAsString(List.of(note));
        response.andExpect(MockMvcResultMatchers.status().isOk());
        MockMvcResultMatchers.content().json(strBodyResponseExpected);
//        Note noteResponse
//        response.andExpect(MockMvcResultMatchers.content().)

//        Assert.assertEquals(note.getUserId(), response.andReturn().getResponse().get);


    }

    private static class EmailWithToken {
        String email;
        String token;

        public EmailWithToken() {
        }
    }
}
