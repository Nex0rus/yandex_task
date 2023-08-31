package ru.yandex.yandexlavka.common.types;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;


public record ErrorResponse(@JsonUnwrapped ApiError error, HttpHeaders headers) {
}
