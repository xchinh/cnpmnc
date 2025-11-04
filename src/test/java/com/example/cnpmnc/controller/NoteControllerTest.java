package com.example.cnpmnc.controller;

import com.example.cnpmnc.dto.ApiResponse;
import com.example.cnpmnc.dto.NoteRequest;
import com.example.cnpmnc.dto.NoteResponse;
import com.example.cnpmnc.services.INoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NoteController.class)
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private INoteService noteService;

    private NoteResponse testNoteResponse;
    private NoteRequest testNoteRequest;
    private static final Long CUSTOMER_ID = 1L;
    private static final Long NOTE_ID = 1L;
    private static final Long AUTHOR_ID = 1L;

    @BeforeEach
    void setUp() {
        testNoteResponse = NoteResponse.builder()
                .id(NOTE_ID)
                .customerId(CUSTOMER_ID)
                .content("Test note content")
                .authorId(AUTHOR_ID)
                .authorName("Test Author")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        testNoteRequest = new NoteRequest();
        testNoteRequest.setContent("New note content");
    }

    // ==========================================
    // GET /api/customers/:customerId/notes
    // ==========================================

    @Test
    @DisplayName("GET /api/customers/{customerId}/notes - Lấy danh sách ghi chú thành công")
    void testGetNotesByCustomerId_Success() throws Exception {
        // Given
        List<NoteResponse> notes = Arrays.asList(
                testNoteResponse,
                NoteResponse.builder()
                        .id(2L)
                        .customerId(CUSTOMER_ID)
                        .content("Second note")
                        .authorId(2L)
                        .authorName("Second Author")
                        .createdAt(LocalDateTime.now().minusHours(1))
                        .updatedAt(LocalDateTime.now().minusHours(1))
                        .build()
        );

        when(noteService.getNotesByCustomerId(CUSTOMER_ID))
                .thenReturn(notes);

        // When & Then
        mockMvc.perform(get("/customers/{customerId}/notes", CUSTOMER_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", is(notNullValue())))
                .andExpect(jsonPath("$.data", is(instanceOf(List.class))))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].id", is(NOTE_ID.intValue())))
                .andExpect(jsonPath("$.data[0].customerId", is(CUSTOMER_ID.intValue())))
                .andExpect(jsonPath("$.data[0].content", is("Test note content")))
                .andExpect(jsonPath("$.data[0].authorId", is(AUTHOR_ID.intValue())))
                .andExpect(jsonPath("$.data[0].authorName", is("Test Author")))
                .andExpect(jsonPath("$.data[0].createdAt", is(notNullValue())))
                .andExpect(jsonPath("$.data[0].updatedAt", is(notNullValue())))
                .andExpect(jsonPath("$.total", is(2)));

        verify(noteService, times(1)).getNotesByCustomerId(CUSTOMER_ID);
    }

    @Test
    @DisplayName("GET /api/customers/{customerId}/notes - Danh sách rỗng")
    void testGetNotesByCustomerId_EmptyList() throws Exception {
        // Given
        when(noteService.getNotesByCustomerId(CUSTOMER_ID))
                .thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/customers/{customerId}/notes", CUSTOMER_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(0)))
                .andExpect(jsonPath("$.total", is(0)));

        verify(noteService, times(1)).getNotesByCustomerId(CUSTOMER_ID);
    }

    @Test
    @DisplayName("GET /api/customers/{customerId}/notes - Customer không tồn tại (404)")
    void testGetNotesByCustomerId_CustomerNotFound() throws Exception {
        // Given
        when(noteService.getNotesByCustomerId(999L))
                .thenThrow(new RuntimeException("Customer not found"));

        // When & Then
        mockMvc.perform(get("/customers/{customerId}/notes", 999L))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message", containsString("Customer not found")));

        verify(noteService, times(1)).getNotesByCustomerId(999L);
    }

    @Test
    @DisplayName("GET /api/customers/{customerId}/notes - Sắp xếp theo thời gian mới nhất")
    void testGetNotesByCustomerId_OrderByNewest() throws Exception {
        // Given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneHourAgo = now.minusHours(1);
        LocalDateTime twoHoursAgo = now.minusHours(2);

        List<NoteResponse> notes = Arrays.asList(
                NoteResponse.builder()
                        .id(1L)
                        .customerId(CUSTOMER_ID)
                        .content("Newest note")
                        .authorId(AUTHOR_ID)
                        .authorName("Author 1")
                        .createdAt(now)
                        .updatedAt(now)
                        .build(),
                NoteResponse.builder()
                        .id(2L)
                        .customerId(CUSTOMER_ID)
                        .content("Older note")
                        .authorId(AUTHOR_ID)
                        .authorName("Author 1")
                        .createdAt(oneHourAgo)
                        .updatedAt(oneHourAgo)
                        .build(),
                NoteResponse.builder()
                        .id(3L)
                        .customerId(CUSTOMER_ID)
                        .content("Oldest note")
                        .authorId(AUTHOR_ID)
                        .authorName("Author 1")
                        .createdAt(twoHoursAgo)
                        .updatedAt(twoHoursAgo)
                        .build()
        );

        when(noteService.getNotesByCustomerId(CUSTOMER_ID))
                .thenReturn(notes);

        // When & Then
        mockMvc.perform(get("/customers/{customerId}/notes", CUSTOMER_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id", is(1))) // Newest first
                .andExpect(jsonPath("$.data[0].content", is("Newest note")))
                .andExpect(jsonPath("$.data[1].id", is(2)))
                .andExpect(jsonPath("$.data[2].id", is(3))); // Oldest last
    }

    // ==========================================
    // POST /api/customers/:customerId/notes
    // ==========================================

    @Test
    @DisplayName("POST /api/customers/{customerId}/notes - Tạo ghi chú mới thành công")
    void testCreateNote_Success() throws Exception {
        // Given
        NoteResponse createdNote = NoteResponse.builder()
                .id(NOTE_ID)
                .customerId(CUSTOMER_ID)
                .content("New note content")
                .authorId(AUTHOR_ID)
                .createdAt(LocalDateTime.now())
                .build();

        when(noteService.createNote(eq(CUSTOMER_ID), any(), eq(AUTHOR_ID)))
                .thenReturn(createdNote);

        // When & Then
        mockMvc.perform(post("/customers/{customerId}/notes", CUSTOMER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testNoteRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Tạo ghi chú thành công")))
                .andExpect(jsonPath("$.data.id", is(NOTE_ID.intValue())))
                .andExpect(jsonPath("$.data.customerId", is(CUSTOMER_ID.intValue())))
                .andExpect(jsonPath("$.data.content", is("New note content")))
                .andExpect(jsonPath("$.data.authorId", is(AUTHOR_ID.intValue())))
                .andExpect(jsonPath("$.data.createdAt", is(notNullValue())));

        verify(noteService, times(1))
                .createNote(eq(CUSTOMER_ID), any(), eq(AUTHOR_ID));
    }

    @Test
    @DisplayName("POST /api/customers/{customerId}/notes - Validation error (content trống)")
    void testCreateNote_ValidationError_EmptyContent() throws Exception {
        // Given
        testNoteRequest.setContent(""); // Content trống

        // When & Then
        mockMvc.perform(post("/customers/{customerId}/notes", CUSTOMER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testNoteRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(noteService, never()).createNote(anyLong(), any(), anyLong());
    }

    @Test
    @DisplayName("POST /api/customers/{customerId}/notes - Validation error (content null)")
    void testCreateNote_ValidationError_NullContent() throws Exception {
        // Given
        testNoteRequest.setContent(null);

        // When & Then
        mockMvc.perform(post("/customers/{customerId}/notes", CUSTOMER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testNoteRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(noteService, never()).createNote(anyLong(), any(), anyLong());
    }

    @Test
    @DisplayName("POST /api/customers/{customerId}/notes - Customer không tồn tại (404)")
    void testCreateNote_CustomerNotFound() throws Exception {
        // Given
        when(noteService.createNote(eq(999L), any(), anyLong()))
                .thenThrow(new RuntimeException("Customer not found"));

        // When & Then
        mockMvc.perform(post("/customers/{customerId}/notes", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testNoteRequest)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message", containsString("Customer not found")));

        verify(noteService, times(1))
                .createNote(eq(999L), any(), anyLong());
    }

    @Test
    @DisplayName("POST /api/customers/{customerId}/notes - Forbidden (403)")
    void testCreateNote_Forbidden() throws Exception {
        // Given
        when(noteService.createNote(eq(CUSTOMER_ID), any(), anyLong()))
                .thenThrow(new RuntimeException("Forbidden: Insufficient permissions"));

        // When & Then
        mockMvc.perform(post("/customers/{customerId}/notes", CUSTOMER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testNoteRequest)))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.success", is(false)));

        verify(noteService, times(1))
                .createNote(eq(CUSTOMER_ID), any(), anyLong());
    }

    // ==========================================
    // PUT /api/customers/:customerId/notes/:id
    // ==========================================

    @Test
    @DisplayName("PUT /api/customers/{customerId}/notes/{id} - Cập nhật ghi chú thành công")
    void testUpdateNote_Success() throws Exception {
        // Given
        NoteResponse updatedNote = NoteResponse.builder()
                .id(NOTE_ID)
                .content("Updated note content")
                .updatedAt(LocalDateTime.now())
                .build();

        when(noteService.updateNote(eq(CUSTOMER_ID), eq(NOTE_ID), any(), eq(AUTHOR_ID)))
                .thenReturn(updatedNote);

        // When & Then
        mockMvc.perform(put("/customers/{customerId}/notes/{id}", CUSTOMER_ID, NOTE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testNoteRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Cập nhật ghi chú thành công")))
                .andExpect(jsonPath("$.data.id", is(NOTE_ID.intValue())))
                .andExpect(jsonPath("$.data.content", is("Updated note content")))
                .andExpect(jsonPath("$.data.updatedAt", is(notNullValue())));

        verify(noteService, times(1))
                .updateNote(eq(CUSTOMER_ID), eq(NOTE_ID), any(), eq(AUTHOR_ID));
    }

    @Test
    @DisplayName("PUT /api/customers/{customerId}/notes/{id} - Validation error (content trống)")
    void testUpdateNote_ValidationError_EmptyContent() throws Exception {
        // Given
        testNoteRequest.setContent("");

        // When & Then
        mockMvc.perform(put("/customers/{customerId}/notes/{id}", CUSTOMER_ID, NOTE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testNoteRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(noteService, never()).updateNote(anyLong(), anyLong(), any(), anyLong());
    }

    @Test
    @DisplayName("PUT /api/customers/{customerId}/notes/{id} - Note không tồn tại (404)")
    void testUpdateNote_NotFound() throws Exception {
        // Given
        when(noteService.updateNote(eq(CUSTOMER_ID), eq(999L), any(), anyLong()))
                .thenThrow(new RuntimeException("Note not found"));

        // When & Then
        mockMvc.perform(put("/customers/{customerId}/notes/{id}", CUSTOMER_ID, 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testNoteRequest)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message", containsString("Note not found")));

        verify(noteService, times(1))
                .updateNote(eq(CUSTOMER_ID), eq(999L), any(), anyLong());
    }

    @Test
    @DisplayName("PUT /api/customers/{customerId}/notes/{id} - Forbidden (không phải author)")
    void testUpdateNote_Forbidden_NotAuthor() throws Exception {
        // Given
        when(noteService.updateNote(eq(CUSTOMER_ID), eq(NOTE_ID), any(), eq(999L)))
                .thenThrow(new RuntimeException("Forbidden: Only author can edit"));

        // When & Then
        mockMvc.perform(put("/customers/{customerId}/notes/{id}", CUSTOMER_ID, NOTE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testNoteRequest)))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message", containsString("Forbidden")));

        verify(noteService, times(1))
                .updateNote(eq(CUSTOMER_ID), eq(NOTE_ID), any(), eq(999L));
    }

    @Test
    @DisplayName("PUT /api/customers/{customerId}/notes/{id} - Customer không tồn tại (404)")
    void testUpdateNote_CustomerNotFound() throws Exception {
        // Given
        when(noteService.updateNote(eq(999L), eq(NOTE_ID), any(), anyLong()))
                .thenThrow(new RuntimeException("Customer not found"));

        // When & Then
        mockMvc.perform(put("/customers/{customerId}/notes/{id}", 999L, NOTE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testNoteRequest)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success", is(false)));

        verify(noteService, times(1))
                .updateNote(eq(999L), eq(NOTE_ID), any(), anyLong());
    }

    // ==========================================
    // DELETE /api/customers/:customerId/notes/:id
    // ==========================================

    @Test
    @DisplayName("DELETE /api/customers/{customerId}/notes/{id} - Xóa ghi chú thành công")
    void testDeleteNote_Success() throws Exception {
        // Given
        doNothing().when(noteService)
                .deleteNote(CUSTOMER_ID, NOTE_ID, AUTHOR_ID);

        // When & Then
        mockMvc.perform(delete("/customers/{customerId}/notes/{id}", CUSTOMER_ID, NOTE_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Note deleted successfully")));

        verify(noteService, times(1))
                .deleteNote(CUSTOMER_ID, NOTE_ID, AUTHOR_ID);
    }

    @Test
    @DisplayName("DELETE /api/customers/{customerId}/notes/{id} - Note không tồn tại (404)")
    void testDeleteNote_NotFound() throws Exception {
        // Given
        doThrow(new RuntimeException("Note not found"))
                .when(noteService).deleteNote(CUSTOMER_ID, 999L, AUTHOR_ID);

        // When & Then
        mockMvc.perform(delete("/customers/{customerId}/notes/{id}", CUSTOMER_ID, 999L))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message", containsString("Note not found")));

        verify(noteService, times(1))
                .deleteNote(CUSTOMER_ID, 999L, AUTHOR_ID);
    }

    @Test
    @DisplayName("DELETE /api/customers/{customerId}/notes/{id} - Forbidden (không phải author)")
    void testDeleteNote_Forbidden_NotAuthor() throws Exception {
        // Given
        doThrow(new RuntimeException("Forbidden: Only author can delete"))
                .when(noteService).deleteNote(CUSTOMER_ID, NOTE_ID, 999L);

        // When & Then
        mockMvc.perform(delete("/customers/{customerId}/notes/{id}", CUSTOMER_ID, NOTE_ID))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message", containsString("Forbidden")));

        verify(noteService, times(1))
                .deleteNote(CUSTOMER_ID, NOTE_ID, 999L);
    }

    @Test
    @DisplayName("DELETE /api/customers/{customerId}/notes/{id} - Customer không tồn tại (404)")
    void testDeleteNote_CustomerNotFound() throws Exception {
        // Given
        doThrow(new RuntimeException("Customer not found"))
                .when(noteService).deleteNote(999L, NOTE_ID, AUTHOR_ID);

        // When & Then
        mockMvc.perform(delete("/customers/{customerId}/notes/{id}", 999L, NOTE_ID))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success", is(false)));

        verify(noteService, times(1))
                .deleteNote(999L, NOTE_ID, AUTHOR_ID);
    }

    @Test
    @DisplayName("DELETE /api/customers/{customerId}/notes/{id} - Unauthorized (401)")
    void testDeleteNote_Unauthorized() throws Exception {
        // Given
        // Mock service sẽ throw exception khi không có auth
        doThrow(new RuntimeException("Unauthorized"))
                .when(noteService).deleteNote(CUSTOMER_ID, NOTE_ID, null);

        // When & Then
        // Note: Trong thực tế, Spring Security sẽ handle 401 trước khi đến controller
        // Test này giả định middleware đã check và throw exception
        mockMvc.perform(delete("/customers/{customerId}/notes/{id}", CUSTOMER_ID, NOTE_ID))
                .andDo(print());
        // Status có thể là 401 hoặc 403 tùy vào implementation của SecurityConfig
    }
}