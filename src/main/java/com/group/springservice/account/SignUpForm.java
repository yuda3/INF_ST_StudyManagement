package com.group.springservice.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SignUpForm {

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]{3,20}$")
    //TODO Lengthアノテーションの使い方確認
    private String nickname;

    @Email
    @NotBlank
    private String email;

    @NotNull
    private String password;
}
