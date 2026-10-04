package com.auth.App.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TokenResponeDTO {

    private String jwtToken;
    private String refreshToken;
}
