package dev.stanislavskyi.shoppingcart.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.stanislavskyi.shoppingcart.request.LoginRequest;
import dev.stanislavskyi.shoppingcart.response.ApiResponse;
import dev.stanislavskyi.shoppingcart.response.JwtResponse;
import dev.stanislavskyi.shoppingcart.security.jwt.JwtUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    //@Sql(scripts = {"/data/insertData.sql"}) //"/data/cleanUp.sql",
    void login() throws Exception {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("admin1@email.com");
        loginRequest.setPassword("123456");

        String userJson = objectMapper.writeValueAsString(loginRequest);

        String tokenJson = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ApiResponse apiResponse = objectMapper.readValue(tokenJson, ApiResponse.class);

        Map<String, Object> dataMap = (Map<String, Object>) apiResponse.getData();

        String token = (String) dataMap.get("token");

        System.out.println("Токен: " + token);

        System.out.println("Login Request: " + loginRequest.getEmail());
        System.out.println("JWT UTILS " + jwtUtils.getUsernameFromToken(token));
        Assertions.assertEquals(loginRequest.getEmail(), jwtUtils.getUsernameFromToken(token));
    }
}
