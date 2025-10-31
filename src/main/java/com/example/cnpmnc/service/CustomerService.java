package com.example.cnpmnc.service;

import com.example.cnpmnc.dto.CustomerRequest;
import com.example.cnpmnc.dto.CustomerResponse;
import com.example.cnpmnc.entity.Customer;
import com.example.cnpmnc.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    // Lấy tất cả khách hàng
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findByDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Lấy khách hàng theo ID
    public CustomerResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với ID: " + id));
        return mapToResponse(customer);
    }

    // Tạo khách hàng mới
    @Transactional
    public CustomerResponse createCustomer(CustomerRequest request, Long createdBy) {
        // Kiểm tra email đã tồn tại
        if (request.getEmail() != null && customerRepository.findByDeletedAtIsNull()
                .stream()
                .anyMatch(c -> request.getEmail().equals(c.getEmail()))) {
            throw new RuntimeException("Email đã tồn tại");
        }

        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setCompany(request.getCompany());
        customer.setNotes(request.getNotes());
        customer.setProfilePicture(request.getProfilePicture());
        customer.setTeamId(request.getTeamId() != null ? request.getTeamId() : 1L);
        customer.setCreatedBy(createdBy);

        Customer saved = customerRepository.save(customer);
        return mapToResponse(saved);
    }

    // Cập nhật khách hàng
    @Transactional
    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        Customer customer = customerRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với ID: " + id));

        // Kiểm tra email trùng (ngoại trừ chính nó)
        if (request.getEmail() != null && !request.getEmail().equals(customer.getEmail())) {
            boolean emailExists = customerRepository.findByDeletedAtIsNull()
                    .stream()
                    .anyMatch(c -> request.getEmail().equals(c.getEmail()) && !c.getId().equals(id));
            if (emailExists) {
                throw new RuntimeException("Email đã tồn tại");
            }
        }

        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setCompany(request.getCompany());
        customer.setNotes(request.getNotes());
        customer.setProfilePicture(request.getProfilePicture());
        if (request.getTeamId() != null) {
            customer.setTeamId(request.getTeamId());
        }

        Customer updated = customerRepository.save(customer);
        return mapToResponse(updated);
    }

    // Xóa khách hàng (soft delete)
    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với ID: " + id));

        customer.setDeletedAt(java.time.LocalDateTime.now());
        customerRepository.save(customer);
    }

    // Tìm kiếm khách hàng
    public List<CustomerResponse> searchCustomers(String keyword) {
        return customerRepository.searchByKeyword(keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Map Entity to Response DTO
    private CustomerResponse mapToResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .company(customer.getCompany())
                .notes(customer.getNotes())
                .profilePicture(customer.getProfilePicture())
                .teamId(customer.getTeamId())
                .createdBy(customer.getCreatedBy())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .build();
    }
}