package ru.practicum.users.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
@Builder(toBuilder = true)
public class UserDto {
    Long id;

    @NotBlank
    @Size(min = 6, max = 254, message = "UserDto: email length should be from {min} to {max}")
    @Email(message = "UserDto: email should be in correct email form, e.g user@email.com")
    String email;

    @NotBlank(message = "UserDto: name can't be empty")
    @Size(min = 2, max = 250, message = "UserDto: name length should be from {min} to {max}")
    String name;
}
