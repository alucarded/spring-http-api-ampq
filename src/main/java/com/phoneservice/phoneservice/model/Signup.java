package com.phoneservice.phoneservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Signup {
    @NotBlank
    private String fullName;
    @NotBlank
    private String phone;
}
