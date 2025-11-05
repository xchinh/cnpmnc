package com.example.cnpmnc.controller.customer;

import com.example.cnpmnc.dto.ApiResponse;
import com.example.cnpmnc.dto.request.customer.CustomerRequest;
import com.example.cnpmnc.dto.response.customer.CustomerResponse;
import com.example.cnpmnc.services.impl.CustomerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Tag(name="Customer", description="Customer Management APIs")
public class CustomerController {

    private final CustomerService customerService;

    /**
     * GET /api/customers
     * Lấy danh sách tất cả khách hàng
     */
    @GetMapping
    @Operation(
        summary = "List customers",
        description = "Lấy danh sách khách hàng có hỗ trợ phân trang và sắp xếp. Hỗ trợ tìm kiếm qua tham số 'keyword'."
    )
    public ResponseEntity<ApiResponse<Page<CustomerResponse>>> getAllCustomers(
            @Parameter(description = "Trang (bắt đầu từ 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Kích thước trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường sắp xếp") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Thứ tự sắp xếp: asc|desc") @RequestParam(defaultValue = "desc") String sortDir,
            @Parameter(description = "Từ khóa tìm kiếm") @RequestParam(required = false) String keyword
    ) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("asc")
                    ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();

            Pageable pageable = PageRequest.of(page, size, sort);

            Page<CustomerResponse> customers = customerService.getAllCustomers(pageable, keyword);

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
    @Operation(
        summary = "Get customer by ID",
        description = "Lấy chi tiết một khách hàng theo ID. Tương thích: trả về đối tượng `CustomerResponse` đầy đủ."
    )
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
    @Operation(
        summary = "Create customer",
        description = "Tạo khách hàng mới. Tương thích: trả về đối tượng `CustomerResponse` đầy đủ."
    )
    public ResponseEntity<ApiResponse<CustomerResponse>> createCustomer(
            @Valid @RequestBody CustomerRequest request) {
        try {
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
    @Operation(
        summary = "Update a customer",
        description = "Cập nhật thông tin khách hàng theo ID. Tương thích: chấp nhận `CustomerRequest`, trả về `CustomerResponse` đã cập nhật."
    )
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
    @Operation(
        summary = "Delete a customer",
        description = "Xóa (soft delete) khách hàng theo ID. Tương thích: trả về thông báo thành công."
    )
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
     * GET /api/customers/by-location
     * Lọc khách hàng theo location
     */
    @GetMapping("/by-location")
    @Operation(
        summary = "Filter customers by location",
        description = "Lọc khách hàng theo location. Tương thích: trả về đối tượng `Page<CustomerResponse>` đầy đủ."
    )
    public ResponseEntity<ApiResponse<Page<CustomerResponse>>> getCustomersByLocation(
            @Parameter(description = "Địa điểm cần lọc", required = true)
            @RequestParam String location,

            @Parameter(description = "Trang hiện tại (bắt đầu từ 0). Mặc định: 0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Kích thước trang (số bản ghi mỗi trang). Mặc định: 10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Trường dùng để sắp xếp. Ví dụ: 'createdAt', 'name'. Mặc định: 'createdAt'")
            @RequestParam(defaultValue = "createdAt") String sortBy,

            @Parameter(description = "Chiều sắp xếp: 'asc' hoặc 'desc'. Mặc định: 'desc'")
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("asc")
                    ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();
            Pageable pageable = PageRequest.of(page, size, sort);

            Page<CustomerResponse> customers =
                    customerService.filterCustomersByLocation(location, pageable);

            return ResponseEntity.ok(
                    ApiResponse.success("Lọc khách hàng theo location thành công", customers)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Lỗi hệ thống: " + e.getMessage()));
        }
    }

}
