package com.weareadaptive.auction.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9.]+$")
    @Size(max = 50)
    String username,

    @NotBlank
    @Size(min = 8, max = 20)
    String password,

    @NotBlank
    @Size(max = 100)
    String firstName,

    @NotBlank
    @Size(max = 100)
    String lastName,

    @NotBlank
    @Size(max = 100)
    String organisation
) {
}
