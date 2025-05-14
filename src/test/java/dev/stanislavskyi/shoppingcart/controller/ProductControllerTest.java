package dev.stanislavskyi.shoppingcart.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.stanislavskyi.shoppingcart.model.Category;
import dev.stanislavskyi.shoppingcart.request.AddProductRequest;
import dev.stanislavskyi.shoppingcart.response.ApiResponse;
import dev.stanislavskyi.shoppingcart.security.jwt.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllProducts() {
    }

    @Test
    void getProductById() {
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addProduct() throws Exception {
        AddProductRequest addProductRequest = new AddProductRequest();
        addProductRequest.setName("TEST NAME1");
        addProductRequest.setBrand("TEST BRAND1");
        addProductRequest.setPrice(BigDecimal.TEN);
        addProductRequest.setInventory(10);
        addProductRequest.setDescription("This is a test product");
        Category category = new Category("TEST CATEGORY");
        addProductRequest.setCategory(category);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addProductRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        ApiResponse apiResponse = objectMapper.readValue(response, ApiResponse.class);


        assertEquals("success", apiResponse.getMessage());
        assertNotNull(apiResponse.getData());
    }
}