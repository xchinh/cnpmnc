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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

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
    @DisplayName("GET /api/customers - Lấy danh sách khách hàng với phân trang")
    void testGetAllCustomers() throws Exception {
        // Given
        List<CustomerResponse> customers = Arrays.asList(testResponse);
        Page<CustomerResponse> page = new PageImpl<>(customers, PageRequest.of(0, 10), 1);
        when(customerService.getAllCustomers(any())).thenReturn(page);

        // When & Then
        mockMvc.perform(get("/api/customers")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "createdAt")
                        .param("sortDir", "desc"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Lấy danh sách khách hàng thành công")))
                .andExpect(jsonPath("$.data.content", hasSize(1)))
                .andExpect(jsonPath("$.data.content[0].name", is("Test Customer")))
                .andExpect(jsonPath("$.data.content[0].email", is("test@example.com")))
                .andExpect(jsonPath("$.data.totalElements", is(1)))
                .andExpect(jsonPath("$.data.totalPages", is(1)))
                .andExpect(jsonPath("$.data.number", is(0)));

        verify(customerService, times(1)).getAllCustomers(any());
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

    @Test
    @DisplayName("GET /api/customers/search?q=test - Tìm kiếm với param 'q'")
    void testSearchCustomersWithQParam() throws Exception {
        // Given
        CustomerResponse customer1 = CustomerResponse.builder()
                .id(1L)
                .name("Test Customer")
                .email("test@example.com")
                .company("Test Company")
                .build();
        
        CustomerResponse customer2 = CustomerResponse.builder()
                .id(2L)
                .name("Another Test")
                .email("another@test.com")
                .company("Testing Corp")
                .build();
        
        List<CustomerResponse> results = Arrays.asList(customer1, customer2);
        when(customerService.searchCustomers("test")).thenReturn(results);

        // When & Then
        mockMvc.perform(get("/api/customers/search")
                        .param("q", "test"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Tìm kiếm thành công")))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].id", is(1)))
                .andExpect(jsonPath("$.data[0].name", is("Test Customer")))
                .andExpect(jsonPath("$.data[0].email", is("test@example.com")))
                .andExpect(jsonPath("$.data[0].company", is("Test Company")))
                .andExpect(jsonPath("$.data[1].id", is(2)))
                .andExpect(jsonPath("$.data[1].name", is("Another Test")));

        verify(customerService, times(1)).searchCustomers("test");
    }

    @Test
    @DisplayName("GET /api/customers/search - Tìm kiếm theo tên")
    void testSearchCustomersByName() throws Exception {
        // Given
        CustomerResponse customer = CustomerResponse.builder()
                .id(1L)
                .name("Nguyễn Văn A")
                .email("nguyenvana@example.com")
                .company("TechCorp")
                .build();
        
        when(customerService.searchCustomers("Nguyễn")).thenReturn(Arrays.asList(customer));

        // When & Then
        mockMvc.perform(get("/api/customers/search")
                        .param("q", "Nguyễn"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].name", containsString("Nguyễn")));

        verify(customerService, times(1)).searchCustomers("Nguyễn");
    }

    @Test
    @DisplayName("GET /api/customers/search - Tìm kiếm theo email")
    void testSearchCustomersByEmail() throws Exception {
        // Given
        CustomerResponse customer = CustomerResponse.builder()
                .id(2L)
                .name("Test User")
                .email("test@techcorp.vn")
                .company("TechCorp Vietnam")
                .build();
        
        when(customerService.searchCustomers("techcorp.vn")).thenReturn(Arrays.asList(customer));

        // When & Then
        mockMvc.perform(get("/api/customers/search")
                        .param("q", "techcorp.vn"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].email", containsString("techcorp.vn")));

        verify(customerService, times(1)).searchCustomers("techcorp.vn");
    }

    @Test
    @DisplayName("GET /api/customers/search - Tìm kiếm theo công ty")
    void testSearchCustomersByCompany() throws Exception {
        // Given
        List<CustomerResponse> customers = Arrays.asList(
                CustomerResponse.builder()
                        .id(1L)
                        .name("User 1")
                        .email("user1@company.com")
                        .company("ABC Company")
                        .build(),
                CustomerResponse.builder()
                        .id(2L)
                        .name("User 2")
                        .email("user2@company.com")
                        .company("ABC Corporation")
                        .build()
        );
        
        when(customerService.searchCustomers("ABC")).thenReturn(customers);

        // When & Then
        mockMvc.perform(get("/api/customers/search")
                        .param("q", "ABC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].company", containsString("ABC")))
                .andExpect(jsonPath("$.data[1].company", containsString("ABC")));

        verify(customerService, times(1)).searchCustomers("ABC");
    }

    @Test
    @DisplayName("GET /api/customers/search - Không tìm thấy kết quả")
    void testSearchCustomersNotFound() throws Exception {
        // Given
        when(customerService.searchCustomers("nonexistent")).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/customers/search")
                        .param("q", "nonexistent"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(0)))
                .andExpect(jsonPath("$.data", is(empty())));

        verify(customerService, times(1)).searchCustomers("nonexistent");
    }

    @Test
    @DisplayName("GET /api/customers/search - Thiếu keyword (Bad Request)")
    void testSearchCustomersMissingKeyword() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/customers/search"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message", containsString("required")));

        verify(customerService, never()).searchCustomers(any());
    }

    @Test
    @DisplayName("GET /api/customers/search - Keyword rỗng (Bad Request)")
    void testSearchCustomersEmptyKeyword() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/customers/search")
                        .param("q", ""))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", is(false)));

        verify(customerService, never()).searchCustomers(any());
    }

    @Test
    @DisplayName("GET /api/customers/search - Tìm kiếm case-insensitive")
    void testSearchCustomersCaseInsensitive() throws Exception {
        // Given
        CustomerResponse customer = CustomerResponse.builder()
                .id(1L)
                .name("TechCorp Vietnam")
                .email("contact@TECHCORP.vn")
                .company("TECHCORP")
                .build();
        
        when(customerService.searchCustomers("techcorp")).thenReturn(Arrays.asList(customer));

        // When & Then - Tìm với lowercase
        mockMvc.perform(get("/api/customers/search")
                        .param("q", "techcorp"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)));

        verify(customerService, times(1)).searchCustomers("techcorp");
    }

    @Test
    @DisplayName("GET /api/customers/search - Tìm với special characters")
    void testSearchCustomersWithSpecialCharacters() throws Exception {
        // Given
        CustomerResponse customer = CustomerResponse.builder()
                .id(1L)
                .name("Test & Co.")
                .email("test@example.com")
                .company("Test & Company")
                .build();
        
        when(customerService.searchCustomers("Test &")).thenReturn(Arrays.asList(customer));

        // When & Then
        mockMvc.perform(get("/api/customers/search")
                        .param("q", "Test &"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].name", is("Test & Co.")));

        verify(customerService, times(1)).searchCustomers("Test &");
    }

}