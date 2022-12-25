package com.loam.stoody.dto.api.request;

import lombok.*;

@NoArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class RegistrationRequestDTO {
    private String username;
    private String email;
    private String password;
}
