package com.example.cnpmnc.dto.response;

import com.example.cnpmnc.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ApiResponse<T> extends ResponseEntity<ApiBody<T>> {
    private ApiResponse(ApiBody<T> body, HttpStatus statusCode) {
        super(body, statusCode);
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }


    public static class Builder<T> {
        private String code;
        private HttpStatus status = HttpStatus.OK;
        private final Map<String, Object> metadata = new HashMap<>();
        private String message = this.status.getReasonPhrase();

        public Builder<T> code(String code) {
            this.code = code;
            return this;
        }

        public Builder<T> status(HttpStatus status) {
            this.status = status;
            return this;
        }

        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }

        public Builder<T> metadata(String key, Object value) {
            this.metadata.put(key, value);
            return this;
        }

        public Builder<T> metadata(Map<String, Object> metadata) {
            if (metadata != null) this.metadata.putAll(metadata);
            return this;
        }

        public ApiResponse<T> build() {
            ApiBody<T> body = new ApiBody<>(
                    code,
                    message == null ? status.getReasonPhrase() : message,
                    metadata
            );

            return new ApiResponse<>(body, status);
        }
    }
}