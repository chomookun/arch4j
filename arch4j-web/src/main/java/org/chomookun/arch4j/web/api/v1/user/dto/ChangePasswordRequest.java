package org.chomookun.arch4j.web.api.v1.user.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class ChangePasswordRequest {

    @NotBlank
    private String currentPassword;

    @NotBlank
    private String newPassword;

}
