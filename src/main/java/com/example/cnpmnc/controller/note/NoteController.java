package com.example.cnpmnc.controller.note;

import com.example.cnpmnc.dto.ApiResponse;
import com.example.cnpmnc.dto.request.note.NoteRequest;
import com.example.cnpmnc.dto.response.note.NoteResponse;
import com.example.cnpmnc.services.INoteService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class NoteController {

    private final INoteService noteService;

    /**
     * GET /api/customers/{customerId}/notes
     * Lấy danh sách ghi chú của khách hàng
     */
    @GetMapping
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
    public ResponseEntity<ApiResponse<NoteResponse>> createNote(
            @PathVariable Long customerId,
            @Valid @RequestBody NoteRequest request) {
        try {
            Long currentUserId = 1L; // Tạm thời hardcode - sẽ lấy từ JWT token sau

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
    public ResponseEntity<ApiResponse<NoteResponse>> updateNote(
            @PathVariable Long customerId,
            @PathVariable Long id,
            @Valid @RequestBody NoteRequest request) {
        try {
            Long currentUserId = 1L; // Tạm thời hardcode - sẽ lấy từ JWT token sau

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
    public ResponseEntity<ApiResponse<Void>> deleteNote(
            @PathVariable Long customerId,
            @PathVariable Long id) {
        try {
            Long currentUserId = 1L; // Tạm thời hardcode - sẽ lấy từ JWT token sau

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