package com.Backend.MediConnect.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterRequest {

    private String email;
    private String password;
    private String dni;
    private String fullName;
    private String role;

}