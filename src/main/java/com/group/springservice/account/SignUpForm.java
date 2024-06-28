package com.group.springservice.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class SignUpForm {

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]{3,20}$")
    @Length(min = 3, max = 20)
    private String nickname;

    @Email
    @NotBlank
    private String email;

    @NotNull
    @Length(min = 8, max = 50)
    private String password;
}
