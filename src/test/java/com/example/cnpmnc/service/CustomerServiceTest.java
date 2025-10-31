package com.example.cnpmnc.service;


import com.example.cnpmnc.dto.CustomerRequest;
import com.example.cnpmnc.dto.CustomerResponse;
import com.example.cnpmnc.entity.Customer;
import com.example.cnpmnc.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer testCustomer;
    private CustomerRequest testRequest;

    @BeforeEach
    void setUp() {
        // Setup test data
        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setName("Test Customer");
        testCustomer.setEmail("test@example.com");
        testCustomer.setPhone("0901234567");
        testCustomer.setCompany("Test Company");
        testCustomer.setNotes("Test notes");
        testCustomer.setTeamId(1L);
        testCustomer.setCreatedBy(1L);
        testCustomer.setCreatedAt(LocalDateTime.now());
        testCustomer.setUpdatedAt(LocalDateTime.now());

        testRequest = new CustomerRequest();
        testRequest.setName("New Customer");
        testRequest.setEmail("new@example.com");
        testRequest.setPhone("0909876543");
        testRequest.setCompany("New Company");
        testRequest.setTeamId(1L);
    }

    @Test
    @DisplayName("Test lấy tất cả khách hàng thành công")
    void testGetAllCustomers_Success() {
        // Given
        List<Customer> customers = Arrays.asList(testCustomer);
        when(customerRepository.findByDeletedAtIsNull()).thenReturn(customers);

        // When
        List<CustomerResponse> result = customerService.getAllCustomers();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Test Customer");
        assertThat(result.get(0).getEmail()).isEqualTo("test@example.com");
        
        verify(customerRepository, times(1)).findByDeletedAtIsNull();
    }

    @Test
    @DisplayName("Test lấy khách hàng theo ID thành công")
    void testGetCustomerById_Success() {
        // Given
        when(customerRepository.findByIdAndDeletedAtIsNull(1L))
                .thenReturn(Optional.of(testCustomer));

        // When
        CustomerResponse result = customerService.getCustomerById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Test Customer");
        
        verify(customerRepository, times(1)).findByIdAndDeletedAtIsNull(1L);
    }

    @Test
    @DisplayName("Test lấy khách hàng theo ID không tồn tại")
    void testGetCustomerById_NotFound() {
        // Given
        when(customerRepository.findByIdAndDeletedAtIsNull(999L))
                .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> customerService.getCustomerById(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Không tìm thấy khách hàng với ID: 999");
        
        verify(customerRepository, times(1)).findByIdAndDeletedAtIsNull(999L);
    }

    @Test
    @DisplayName("Test tạo khách hàng mới thành công")
    void testCreateCustomer_Success() {
        // Given
        when(customerRepository.findByDeletedAtIsNull()).thenReturn(List.of());
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        // When
        CustomerResponse result = customerService.createCustomer(testRequest, 1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        
        verify(customerRepository, times(1)).findByDeletedAtIsNull();
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    @DisplayName("Test tạo khách hàng với email đã tồn tại")
    void testCreateCustomer_EmailExists() {
        // Given
        testRequest.setEmail("test@example.com"); // email đã tồn tại
        when(customerRepository.findByDeletedAtIsNull())
                .thenReturn(Arrays.asList(testCustomer));

        // When & Then
        assertThatThrownBy(() -> customerService.createCustomer(testRequest, 1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Email đã tồn tại");
        
        verify(customerRepository, times(1)).findByDeletedAtIsNull();
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    @DisplayName("Test cập nhật khách hàng thành công")
    void testUpdateCustomer_Success() {
        // Given
        when(customerRepository.findByIdAndDeletedAtIsNull(1L))
                .thenReturn(Optional.of(testCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        // When
        CustomerResponse result = customerService.updateCustomer(1L, testRequest);

        // Then
        assertThat(result).isNotNull();
        verify(customerRepository, times(1)).findByIdAndDeletedAtIsNull(1L);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    @DisplayName("Test cập nhật khách hàng không tồn tại")
    void testUpdateCustomer_NotFound() {
        // Given
        when(customerRepository.findByIdAndDeletedAtIsNull(999L))
                .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> customerService.updateCustomer(999L, testRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Không tìm thấy khách hàng với ID: 999");
        
        verify(customerRepository, times(1)).findByIdAndDeletedAtIsNull(999L);
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    @DisplayName("Test xóa khách hàng thành công (soft delete)")
    void testDeleteCustomer_Success() {
        // Given
        when(customerRepository.findByIdAndDeletedAtIsNull(1L))
                .thenReturn(Optional.of(testCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        // When
        customerService.deleteCustomer(1L);

        // Then
        verify(customerRepository, times(1)).findByIdAndDeletedAtIsNull(1L);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    @DisplayName("Test xóa khách hàng không tồn tại")
    void testDeleteCustomer_NotFound() {
        // Given
        when(customerRepository.findByIdAndDeletedAtIsNull(999L))
                .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> customerService.deleteCustomer(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Không tìm thấy khách hàng với ID: 999");
        
        verify(customerRepository, times(1)).findByIdAndDeletedAtIsNull(999L);
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    @DisplayName("Test tìm kiếm khách hàng")
    void testSearchCustomers_Success() {
        // Given
        when(customerRepository.searchByKeyword("test"))
                .thenReturn(Arrays.asList(testCustomer));

        // When
        List<CustomerResponse> result = customerService.searchCustomers("test");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Test Customer");
        
        verify(customerRepository, times(1)).searchByKeyword("test");
    }
}