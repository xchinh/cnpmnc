package com.example.cnpmnc.controller;

import com.example.cnpmnc.dto.ApiResponse;
import com.example.cnpmnc.dto.CustomerRequest;
import com.example.cnpmnc.dto.CustomerResponse;
import com.example.cnpmnc.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    /**
     * GET /api/customers
     * Lấy danh sách tất cả khách hàng
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> getAllCustomers() {
        try {
            List<CustomerResponse> customers = customerService.getAllCustomers();
            return ResponseEntity.ok(
                    ApiResponse.success("Lấy danh sách khách hàng thành công", customers)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Lỗi hệ thống: " + e.getMessage()));
        }
    }

    /**
     * GET /api/customers/{id}
     * Lấy chi tiết một khách hàng
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomerById(@PathVariable Long id) {
        try {
            CustomerResponse customer = customerService.getCustomerById(id);
            return ResponseEntity.ok(
                    ApiResponse.success("Lấy thông tin khách hàng thành công", customer)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Lỗi hệ thống: " + e.getMessage()));
        }
    }

    /**
     * POST /api/customers
     * Tạo khách hàng mới
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CustomerResponse>> createCustomer(
            @Valid @RequestBody CustomerRequest request) {
        try {
            // TODO: Lấy user ID từ authentication context
            Long currentUserId = 1L; // Tạm thời hardcode

            CustomerResponse customer = customerService.createCustomer(request, currentUserId);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Tạo khách hàng thành công", customer));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Lỗi hệ thống: " + e.getMessage()));
        }
    }

    /**
     * PUT /api/customers/{id}
     * Cập nhật thông tin khách hàng
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequest request) {
        try {
            CustomerResponse customer = customerService.updateCustomer(id, request);
            return ResponseEntity.ok(
                    ApiResponse.success("Cập nhật khách hàng thành công", customer)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Lỗi hệ thống: " + e.getMessage()));
        }
    }

    /**
     * DELETE /api/customers/{id}
     * Xóa khách hàng (soft delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable Long id) {
        try {
            customerService.deleteCustomer(id);
            return ResponseEntity.ok(
                    ApiResponse.success("Xóa khách hàng thành công", null)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Lỗi hệ thống: " + e.getMessage()));
        }
    }

    /**
     * GET /api/customers/search?keyword=abc
     * Tìm kiếm khách hàng
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> searchCustomers(
            @RequestParam String keyword) {
        try {
            List<CustomerResponse> customers = customerService.searchCustomers(keyword);
            return ResponseEntity.ok(
                    ApiResponse.success("Tìm kiếm thành công", customers)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Lỗi hệ thống: " + e.getMessage()));
        }
    }
}
