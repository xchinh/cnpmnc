package com.example.cnpmnc.services;

import com.example.cnpmnc.dto.request.customer.CustomerRequest;
import com.example.cnpmnc.dto.response.customer.CustomerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICustomerService {
    Page<CustomerResponse> getAllCustomers(Pageable pageable, String keyword);
    CustomerResponse getCustomerById(Long id);
    CustomerResponse createCustomer(CustomerRequest request, Long createdBy);
    CustomerResponse updateCustomer(Long id, CustomerRequest request);
    void deleteCustomer(Long id);
    Page<CustomerResponse> filterCustomersByLocation(String location, Pageable pageable);
}
