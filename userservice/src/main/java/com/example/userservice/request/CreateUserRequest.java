package com.example.userservice.request;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateUserRequest {

    @NotNull
    @Length(min = 8, max = 20)
    private String username;

    @NotNull
    @Length(min = 7, max = 20)
    private String password;

    @NotNull
    private String name;

    @NotNull
    @Length(min = 2, max = 10)
    private String nickName;

    @Email
    @NotNull
    private String email;

}
