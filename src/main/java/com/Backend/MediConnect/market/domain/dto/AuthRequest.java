package com.Backend.MediConnect.market.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthRequest {
    private String dni;
    private String password;

}