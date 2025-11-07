package com.example.cnpmnc.services.impl;

import com.example.cnpmnc.dto.request.customer.CustomerRequest;
import com.example.cnpmnc.dto.response.customer.CustomerResponse;
import com.example.cnpmnc.entity.Customer;
import com.example.cnpmnc.repository.CustomerRepository;
import com.example.cnpmnc.services.ICustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService implements ICustomerService {

    private final CustomerRepository customerRepository;

    // Lấy tất cả khách hàng
    public Page<CustomerResponse> getAllCustomers(Pageable pageable, String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return customerRepository.findByDeletedAtIsNull(pageable)
                    .map(this::mapToResponse);
        }
        return customerRepository.searchByKeyword(keyword.trim(), pageable)
                .map(this::mapToResponse);
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
        customer.setLocation(request.getLocation());
        customer.setJobTitle(request.getJobTitle());

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
        customer.setJobTitle(request.getJobTitle());
        customer.setLocation(request.getLocation());
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

    public Page<CustomerResponse> filterCustomersByLocation(String location, Pageable pageable) {
        if (location == null || location.trim().isEmpty()) {
            throw new RuntimeException("Tham số 'location' là bắt buộc");
        }
        return customerRepository
                .findByLocationContainingIgnoreCaseAndDeletedAtIsNull(location.trim(), pageable)
                .map(this::mapToResponse);
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
                .jobTitle(customer.getJobTitle())
                .location(customer.getLocation())
                .build();
    }
}