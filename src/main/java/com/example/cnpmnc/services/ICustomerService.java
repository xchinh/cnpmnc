package com.example.cnpmnc.services;

import com.example.cnpmnc.dto.CustomerRequest;
import com.example.cnpmnc.dto.CustomerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICustomerService {
    /**
     * Lấy danh sách tất cả khách hàng với phân trang
     */
    Page<CustomerResponse> getAllCustomers(Pageable pageable);
    
    /**
     * Lấy chi tiết khách hàng theo ID
     */
    CustomerResponse getCustomerById(Long id);
    
    /**
     * Tạo khách hàng mới
     */
    CustomerResponse createCustomer(CustomerRequest request, Long createdBy);
    
    /**
     * Cập nhật thông tin khách hàng
     */
    CustomerResponse updateCustomer(Long id, CustomerRequest request);
    
    /**
     * Xóa khách hàng (soft delete)
     */
    void deleteCustomer(Long id);
    
    /**
     * Tìm kiếm khách hàng theo từ khóa
     */
    List<CustomerResponse> searchCustomers(String keyword);
}