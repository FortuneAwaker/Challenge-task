package by.itechart.producer.controller;

import by.itechart.model.dto.BookingDtoWithId;
import by.itechart.model.dto.BookingDtoWithoutId;
import by.itechart.model.dto.BookingResponseDto;
import by.itechart.model.dto.EventDto;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookings")
@Validated
public class BookingController {

    private final BookingService bookingService;

    private static final String TIMEOUT_LITERAL = "Timeout: response wasn't received!";


    @PostMapping
    public ResponseEntity<BookingResponseDto> addBooking(@Valid @RequestBody BookingDtoWithoutId bookingDtoWithoutId) {
        BookingResponseDto response = bookingService.addBooking(bookingDtoWithoutId);
//        if (Objects.isNull(response)) {
//            return new ResponseEntity<>(new ExceptionDto(HttpStatus.GATEWAY_TIMEOUT.value(),
//                    TIMEOUT_LITERAL, Timestamp.valueOf(LocalDateTime.now()).toString()),
//                    HttpStatus.GATEWAY_TIMEOUT);
//        }
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingResponseDto> editBooking(
            @PathVariable @Min(value = 1, message = "id must be more or equals 1") Long id,
            @Valid @RequestBody BookingDtoWithId bookingDtoWithId) {
        bookingDtoWithId.setId(id);
        BookingResponseDto response = bookingService.editBooking(bookingDtoWithId);
//        if (Objects.isNull(response)) {
//            return new ResponseEntity<>(new ExceptionDto(HttpStatus.GATEWAY_TIMEOUT.value(),
//                    TIMEOUT_LITERAL, Timestamp.valueOf(LocalDateTime.now()).toString()),
//                    HttpStatus.GATEWAY_TIMEOUT);
//        }
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EventDto> deleteBooking(
            @PathVariable @Min(value = 1, message = "id must be more or equals 1") Long id) {
        EventDto response = bookingService.deleteBooking(id);
//        if (Objects.isNull(response)) {
//            return new ResponseEntity<>(new ExceptionDto(HttpStatus.GATEWAY_TIMEOUT.value(),
//                    TIMEOUT_LITERAL, Timestamp.valueOf(LocalDateTime.now()).toString()),
//                    HttpStatus.GATEWAY_TIMEOUT);
//        }
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

}
