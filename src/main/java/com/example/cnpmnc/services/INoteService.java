package com.example.cnpmnc.services;

import com.example.cnpmnc.dto.NoteRequest;
import com.example.cnpmnc.dto.NoteResponse;

import java.util.List;

public interface INoteService {
    /**
     * Lấy danh sách ghi chú của khách hàng
     */
    List<NoteResponse> getNotesByCustomerId(Long customerId);

    /**
     * Tạo ghi chú mới
     */
    NoteResponse createNote(Long customerId, NoteRequest request, Long authorId);

    /**
     * Cập nhật ghi chú
     */
    NoteResponse updateNote(Long customerId, Long noteId, NoteRequest request, Long authorId);

    /**
     * Xóa ghi chú (soft delete)
     */
    void deleteNote(Long customerId, Long noteId, Long authorId);
}