package com.example.cnpmnc.services.impl;

import com.example.cnpmnc.dto.request.note.NoteRequest;
import com.example.cnpmnc.dto.response.note.NoteResponse;
import com.example.cnpmnc.entity.Note;
import com.example.cnpmnc.exception.ForbiddenRequestException;
import com.example.cnpmnc.exception.NotFoundException;
import com.example.cnpmnc.repository.CustomerRepository;
import com.example.cnpmnc.repository.NoteRepository;
import com.example.cnpmnc.repository.UserRepository;
import com.example.cnpmnc.services.INoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements INoteService {

    private final NoteRepository noteRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    @Override
    public List<NoteResponse> getNotesByCustomerId(Long customerId) {
        // Kiểm tra customer tồn tại
        customerRepository.findByIdAndDeletedAtIsNull(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        List<Note> notes = noteRepository.findByCustomerIdAndDeletedAtIsNullOrderByCreatedAtDesc(customerId);

        return notes.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public NoteResponse createNote(Long customerId, NoteRequest request, Long authorId) {
        // Kiểm tra customer tồn tại
        customerRepository.findByIdAndDeletedAtIsNull(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        // Kiểm tra author tồn tại
        userRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Note note = new Note();
        note.setCustomerId(customerId);
        note.setAuthorId(authorId);
        note.setContent(request.getContent());

        Note saved = noteRepository.save(note);
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public NoteResponse updateNote(Long customerId, Long noteId, NoteRequest request, Long authorId) {
        // Kiểm tra customer tồn tại
        customerRepository.findByIdAndDeletedAtIsNull(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        // Kiểm tra note tồn tại
        Note note = noteRepository.findByIdAndDeletedAtIsNull(noteId)
                .orElseThrow(() -> new NotFoundException("Note not found"));

        // Kiểm tra note thuộc về customer
        if (!note.getCustomerId().equals(customerId)) {
            throw new NotFoundException("Note not found for this customer");
        }

        // Kiểm tra quyền: chỉ author mới có thể edit
        if (!note.getAuthorId().equals(authorId)) {
            throw new ForbiddenRequestException("Forbidden: Only author can edit");
        }

        note.setContent(request.getContent());
        Note updated = noteRepository.save(note);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deleteNote(Long customerId, Long noteId, Long authorId) {
        // Kiểm tra customer tồn tại
        customerRepository.findByIdAndDeletedAtIsNull(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        // Kiểm tra note tồn tại
        Note note = noteRepository.findByIdAndDeletedAtIsNull(noteId)
                .orElseThrow(() -> new NotFoundException("Note not found"));

        // Kiểm tra note thuộc về customer
        if (!note.getCustomerId().equals(customerId)) {
            throw new NotFoundException("Note not found for this customer");
        }

        // Kiểm tra quyền: chỉ author mới có thể xóa
        if (!note.getAuthorId().equals(authorId)) {
            throw new ForbiddenRequestException("Forbidden: Only author can delete");
        }

        // Soft delete
        note.setDeletedAt(java.time.LocalDateTime.now());
        noteRepository.save(note);
    }

    private NoteResponse mapToResponse(Note note) {
        // Lấy author name từ User entity
        String authorName = userRepository.findById(note.getAuthorId())
                .map(user -> user.getUsername() != null ? user.getUsername() : user.getEmail())
                .orElse("Unknown");

        return NoteResponse.builder()
                .id(note.getId())
                .customerId(note.getCustomerId())
                .content(note.getContent())
                .authorId(note.getAuthorId())
                .authorName(authorName)
                .createdAt(note.getCreatedAt())
                .updatedAt(note.getUpdatedAt())
                .build();
    }
}
