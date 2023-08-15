package ru.practicum.comments.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
public class CommentDto {
    Long id;
    Long authorId;
    Long eventId;
    @NotBlank
    @Size(min = 2, max = 7000, message = "CommentDto: comment text length should be from {min} to {max}")
    String text;
    LocalDateTime timestamp;
}
