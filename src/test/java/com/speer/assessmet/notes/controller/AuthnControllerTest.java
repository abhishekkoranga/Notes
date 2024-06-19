package com.speer.assessmet.notes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.speer.assessmet.notes.NotesAppApplication;
import com.speer.assessmet.notes.dto.SignUpDto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
//@ContextConfiguration(classes = {NotesAppApplication.class})
//@WebMvcTest(Authn.class)
@ComponentScan(basePackages = "com.speer.assessmet.notes")
public class AuthnControllerTest {

    @Autowired
    private MockMvc mvc;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void givenUserCredentials_whenUserSignUp_thenStatus200() throws Exception {
        SignUpDto signUpDto = SignUpDto.builder()
                .emailId("abc@xyz.com")
                .password("root123")
                .build();

        ResultActions response = mvc
                .perform(MockMvcRequestBuilders.post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(signUpDto)));


        response.andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void givenExistingUserCredentials_whenUserSignUp_thenStatus400() throws Exception {
        SignUpDto signUpDto = SignUpDto.builder()
                .emailId("abc@xyz.com")
                .password("root123")
                .build();

        ResultActions response = mvc
                .perform(MockMvcRequestBuilders.post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(signUpDto)));


        response.andExpect(MockMvcResultMatchers.status().is4xxClientError());

    }

    @Test
    public void givenSignedUpUserCredentials_whenUserLogin_thenStatus200() throws Exception {
        SignUpDto signUpDto = SignUpDto.builder()
                .emailId("abc@xyz.com")
                .password("root123")
                .build();

        ResultActions response = mvc
                .perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(signUpDto)));


        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andExpect(MockMvcResultMatchers.header().exists("Authorization"));

    }

    @Test
    public void givenUnSignedUserCredentials_whenUserLogin_thenStatus400() throws Exception {
        SignUpDto signUpDto = SignUpDto.builder()
                .emailId("pqr@xyz.com")
                .password("root123")
                .build();

        ResultActions response = mvc
                .perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(signUpDto)));


        response.andExpect(MockMvcResultMatchers.status().is4xxClientError());

    }






}
