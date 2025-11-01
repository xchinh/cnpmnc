package com.example.cnpmnc.dto.response;

import com.example.cnpmnc.exception.BaseException;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiBody<T> {
    String code;
    String message;
    Map<String, Object> metadata;

    public ApiBody(String code, String message, Map<String, Object> metadata) {
        this.code = code;
        this.message = message;
        this.metadata = metadata;
    }

    public static <T> ApiBody<T> of(String code, String message, Map<String, Object> metadata) {
        return new ApiBody<>(code, message, metadata);
    }

    public static ApiBody<BaseException> error(BaseException ex) {
        return new ApiBody<>(ex.getCode(), ex.getMessage(), null);
    }
}
