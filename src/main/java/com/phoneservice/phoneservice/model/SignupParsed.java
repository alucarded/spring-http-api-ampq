package com.phoneservice.phoneservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupParsed {
    private String firstName;
    private String lastName;
    private String phone;
}
