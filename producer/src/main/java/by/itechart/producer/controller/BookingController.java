package by.itechart.producer.controller;

import by.itechart.model.dto.BookingDtoWithId;
import by.itechart.model.dto.BookingDtoWithoutId;
import by.itechart.model.dto.ExceptionDto;
import by.itechart.model.dto.ResponseDto;
import by.itechart.producer.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookings")
@Validated
public class BookingController {

    private final BookingService bookingService;

    private static final String TIMEOUT_LITERAL = "Timeout: response wasn't received!";


    @PostMapping
    public ResponseEntity<ResponseDto> addBooking(@Valid @RequestBody BookingDtoWithoutId bookingDtoWithoutId) {
        ResponseDto response = bookingService.addBooking(bookingDtoWithoutId);
        if (Objects.isNull(response)) {
            return new ResponseEntity<>(new ExceptionDto(HttpStatus.GATEWAY_TIMEOUT.value(),
                    TIMEOUT_LITERAL, Timestamp.valueOf(LocalDateTime.now()).toString()),
                    HttpStatus.GATEWAY_TIMEOUT);
        }
        if (Objects.nonNull(checkException(response))) {
            return checkException(response);
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto> editBooking(
            @PathVariable @Min(value = 1, message = "id must be more or equals 1") Long id,
            @Valid @RequestBody BookingDtoWithId bookingDtoWithId) {
        bookingDtoWithId.setId(id);
        ResponseDto response = bookingService.editBooking(bookingDtoWithId);
        if (Objects.isNull(response)) {
            return new ResponseEntity<>(new ExceptionDto(HttpStatus.GATEWAY_TIMEOUT.value(),
                    TIMEOUT_LITERAL, Timestamp.valueOf(LocalDateTime.now()).toString()),
                    HttpStatus.GATEWAY_TIMEOUT);
        }
        if (Objects.nonNull(checkException(response))) {
            return checkException(response);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteBooking(
            @PathVariable @Min(value = 1, message = "id must be more or equals 1") Long id) {
        ResponseDto response = bookingService.deleteBooking(id);
        if (Objects.isNull(response)) {
            return new ResponseEntity<>(new ExceptionDto(HttpStatus.GATEWAY_TIMEOUT.value(),
                    TIMEOUT_LITERAL, Timestamp.valueOf(LocalDateTime.now()).toString()),
                    HttpStatus.GATEWAY_TIMEOUT);
        }
        if (Objects.nonNull(checkException(response))) {
            return checkException(response);
        }
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    private ResponseEntity<ResponseDto> checkException(final ResponseDto responseDto) {
        if (responseDto.getClass().equals(ExceptionDto.class)) {
            ExceptionDto exceptionDto = (ExceptionDto) responseDto;
            return new ResponseEntity<>(exceptionDto, HttpStatus.valueOf(exceptionDto.getStatusCode()));
        }
        return null;
    }

}
