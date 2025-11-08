package com.example.cnpmnc.controller.note;

import com.example.cnpmnc.dto.ApiResponse;
import com.example.cnpmnc.dto.request.note.NoteRequest;
import com.example.cnpmnc.dto.response.note.NoteResponse;
import com.example.cnpmnc.services.INoteService;
import com.example.cnpmnc.utils.AuthUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/customers/{customerId}/notes")
@RequiredArgsConstructor
@Tag(name = "Note", description = "Note Management APIs")
public class NoteController {

    private final INoteService noteService;

    /**
     * GET /api/customers/{customerId}/notes
     * Lấy danh sách ghi chú của khách hàng
     */
    @GetMapping
    @Operation(
        summary = "List notes of a customer",
        description = "Lấy danh sách ghi chú của khách hàng. Tương thích: response theo dạng {data, total}."
    )
    public ResponseEntity<ApiResponse<Map<String, Object>>> getNotesByCustomerId(
            @PathVariable Long customerId) {
        try {
            List<NoteResponse> notes = noteService.getNotesByCustomerId(customerId);

            // Format response theo spec: {"data": [...], "total": number}
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("data", notes);
            responseData.put("total", notes.size());

            return ResponseEntity.ok(
                    ApiResponse.success("Lấy danh sách ghi chú thành công", responseData)
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
     * POST /api/customers/{customerId}/notes
     * Tạo ghi chú mới
     */
    @PostMapping
    @Operation(
        summary = "Create a note",
        description = "Tạo ghi chú mới cho khách hàng."
    )
    public ResponseEntity<ApiResponse<NoteResponse>> createNote(
            @PathVariable Long customerId,
            @Valid @RequestBody NoteRequest request) {
        try {
            Long currentUserId = AuthUtils.getCurrentUserId();
            if (currentUserId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("User not authenticated"));
            }

            NoteResponse note = noteService.createNote(customerId, request, currentUserId);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Tạo ghi chú thành công", note));
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Customer not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(e.getMessage()));
            } else if (e.getMessage().contains("Forbidden")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error(e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Lỗi hệ thống: " + e.getMessage()));
        }
    }

    /**
     * PUT /api/customers/{customerId}/notes/{id}
     * Cập nhật ghi chú
     */
    @PutMapping("/{id}")
    @Operation(
        summary = "Update a note",
        description = "Cập nhật nội dung ghi chú của khách hàng."
    )
    public ResponseEntity<ApiResponse<NoteResponse>> updateNote(
            @PathVariable Long customerId,
            @PathVariable Long id,
            @Valid @RequestBody NoteRequest request) {
        try {
            Long currentUserId = AuthUtils.getCurrentUserId();
            if (currentUserId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("User not authenticated"));
            }

            NoteResponse note = noteService.updateNote(customerId, id, request, currentUserId);
            return ResponseEntity.ok(
                    ApiResponse.success("Cập nhật ghi chú thành công", note)
            );
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(e.getMessage()));
            } else if (e.getMessage().contains("Forbidden")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error(e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Lỗi hệ thống: " + e.getMessage()));
        }
    }

    /**
     * DELETE /api/customers/{customerId}/notes/{id}
     * Xóa ghi chú
     */
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete a note",
        description = "Xóa ghi chú của khách hàng."
    )
    public ResponseEntity<ApiResponse<Void>> deleteNote(
            @PathVariable Long customerId,
            @PathVariable Long id) {
        try {
            Long currentUserId = AuthUtils.getCurrentUserId();
            if (currentUserId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("User not authenticated"));
            }

            noteService.deleteNote(customerId, id, currentUserId);
            return ResponseEntity.ok(
                    ApiResponse.success("Note deleted successfully", null)
            );
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(e.getMessage()));
            } else if (e.getMessage().contains("Forbidden")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error(e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Lỗi hệ thống: " + e.getMessage()));
        }
    }
}