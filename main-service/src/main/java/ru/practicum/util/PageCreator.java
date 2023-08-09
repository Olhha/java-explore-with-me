package ru.practicum.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageCreator {
    public static Pageable getPage(int from, int size) {
        return PageRequest.of(from / size, size);
    }
}
