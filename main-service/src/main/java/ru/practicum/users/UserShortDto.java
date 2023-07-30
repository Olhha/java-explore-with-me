package ru.practicum.users;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
@Builder(toBuilder = true)
public class UserShortDto {
    @NotNull
    Long id;

    @NotBlank(message = "UserDto: name can't be empty")
    String name;
}
