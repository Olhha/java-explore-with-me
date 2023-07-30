package ru.practicum;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
@Builder(toBuilder = true)
public class EndpointHitDto {
    @NotBlank(message = "Statistics EndpointHitDto: параметр app не может быть пустым.")
    String app;
    @NotBlank(message = "Statistics EndpointHitDto: параметр uri не может быть пустым.")
    String uri;
    @NotBlank(message = "Statistics EndpointHitDto: параметр ip не может быть пустым.")
    String ip;
    @NotNull(message = "Statistics EndpointHitDto: параметр timestamp не может быть пустым.")
    String timestamp;
}

