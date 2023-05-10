package ru.yandex.yandexlavka.common.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }
}
