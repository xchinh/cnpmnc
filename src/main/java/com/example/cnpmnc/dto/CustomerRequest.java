package com.example.cnpmnc.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequest {

    @NotBlank(message = "Tên khách hàng không được để trống")
    private String name;

    @Email(message = "Email không hợp lệ")
    private String email;

    private String phone;

    private String company;

    private String notes;

    private String profilePicture;

    private Long teamId;
}