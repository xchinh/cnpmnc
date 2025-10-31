package com.example.cnpmnc.controller;

import com.example.cnpmnc.dto.CustomerRequest;
import com.example.cnpmnc.dto.CustomerResponse;
import com.example.cnpmnc.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;

    private CustomerResponse testResponse;
    private CustomerRequest testRequest;

    @BeforeEach
    void setUp() {
        testResponse = CustomerResponse.builder()
                .id(1L)
                .name("Test Customer")
                .email("test@example.com")
                .phone("0901234567")
                .company("Test Company")
                .notes("Test notes")
                .teamId(1L)
                .createdBy(1L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        testRequest = new CustomerRequest();
        testRequest.setName("New Customer");
        testRequest.setEmail("new@example.com");
        testRequest.setPhone("0909876543");
        testRequest.setCompany("New Company");
        testRequest.setTeamId(1L);
    }

    @Test
    @DisplayName("GET /api/customers - Lấy danh sách khách hàng")
    void testGetAllCustomers() throws Exception {
        // Given
        List<CustomerResponse> customers = Arrays.asList(testResponse);
        when(customerService.getAllCustomers()).thenReturn(customers);

        // When & Then
        mockMvc.perform(get("/api/customers"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Lấy danh sách khách hàng thành công")))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].name", is("Test Customer")))
                .andExpect(jsonPath("$.data[0].email", is("test@example.com")));

        verify(customerService, times(1)).getAllCustomers();
    }

    @Test
    @DisplayName("GET /api/customers/{id} - Lấy chi tiết khách hàng")
    void testGetCustomerById_Success() throws Exception {
        // Given
        when(customerService.getCustomerById(1L)).thenReturn(testResponse);

        // When & Then
        mockMvc.perform(get("/api/customers/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.name", is("Test Customer")));

        verify(customerService, times(1)).getCustomerById(1L);
    }

    @Test
    @DisplayName("GET /api/customers/{id} - Không tìm thấy khách hàng")
    void testGetCustomerById_NotFound() throws Exception {
        // Given
        when(customerService.getCustomerById(999L))
                .thenThrow(new RuntimeException("Không tìm thấy khách hàng với ID: 999"));

        // When & Then
        mockMvc.perform(get("/api/customers/999"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message", containsString("Không tìm thấy")));

        verify(customerService, times(1)).getCustomerById(999L);
    }

    @Test
    @DisplayName("POST /api/customers - Tạo khách hàng mới thành công")
    void testCreateCustomer_Success() throws Exception {
        // Given
        when(customerService.createCustomer(any(CustomerRequest.class), anyLong()))
                .thenReturn(testResponse);

        // When & Then
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Tạo khách hàng thành công")))
                .andExpect(jsonPath("$.data.id", is(1)));

        verify(customerService, times(1)).createCustomer(any(CustomerRequest.class), anyLong());
    }

    @Test
    @DisplayName("POST /api/customers - Validation error (tên trống)")
    void testCreateCustomer_ValidationError() throws Exception {
        // Given
        testRequest.setName(""); // tên trống - vi phạm @NotBlank

        // When & Then
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(customerService, never()).createCustomer(any(), anyLong());
    }

    @Test
    @DisplayName("POST /api/customers - Email đã tồn tại")
    void testCreateCustomer_EmailExists() throws Exception {
        // Given
        when(customerService.createCustomer(any(CustomerRequest.class), anyLong()))
                .thenThrow(new RuntimeException("Email đã tồn tại"));

        // When & Then
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message", containsString("Email đã tồn tại")));

        verify(customerService, times(1)).createCustomer(any(CustomerRequest.class), anyLong());
    }

    @Test
    @DisplayName("PUT /api/customers/{id} - Cập nhật khách hàng")
    void testUpdateCustomer_Success() throws Exception {
        // Given
        when(customerService.updateCustomer(eq(1L), any(CustomerRequest.class)))
                .thenReturn(testResponse);

        // When & Then
        mockMvc.perform(put("/api/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Cập nhật khách hàng thành công")));

        verify(customerService, times(1)).updateCustomer(eq(1L), any(CustomerRequest.class));
    }

    @Test
    @DisplayName("DELETE /api/customers/{id} - Xóa khách hàng")
    void testDeleteCustomer_Success() throws Exception {
        // Given
        doNothing().when(customerService).deleteCustomer(1L);

        // When & Then
        mockMvc.perform(delete("/api/customers/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Xóa khách hàng thành công")));

        verify(customerService, times(1)).deleteCustomer(1L);
    }

    @Test
    @DisplayName("GET /api/customers/search - Tìm kiếm khách hàng")
    void testSearchCustomers() throws Exception {
        // Given
        List<CustomerResponse> results = Arrays.asList(testResponse);
        when(customerService.searchCustomers("test")).thenReturn(results);

        // When & Then
        mockMvc.perform(get("/api/customers/search")
                        .param("keyword", "test"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].name", is("Test Customer")));

        verify(customerService, times(1)).searchCustomers("test");
    }
}