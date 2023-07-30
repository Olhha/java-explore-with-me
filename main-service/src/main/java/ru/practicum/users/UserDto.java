package ru.practicum.users;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Value
@Builder(toBuilder = true)
public class UserDto {
    Long id;

    @Email(message = "UserDto: email should be in correct email form, e.g user@email.com")
    String email;

    @NotBlank(message = "UserDto: name can't be empty")
    String name;
}
