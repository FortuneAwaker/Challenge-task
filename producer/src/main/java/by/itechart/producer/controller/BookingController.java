package by.itechart.producer.controller;

import by.itechart.model.dto.BookingDtoWithId;
import by.itechart.model.dto.BookingDtoWithoutId;
import by.itechart.model.dto.BookingResponseDto;
import by.itechart.model.dto.EventDto;
import by.itechart.model.dto.ExceptionDto;
import by.itechart.model.dto.ResponseDto;
import by.itechart.producer.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookings")
@Validated
public class BookingController {

    private final BookingService bookingService;
    private final RabbitTemplate rabbitTemplate;

    @PostMapping
    public ResponseEntity<? extends ResponseDto> addBooking(
            @Valid @RequestBody BookingDtoWithoutId bookingDtoWithoutId) {
        BookingResponseDto response = bookingService.addBooking(bookingDtoWithoutId);
        if (Objects.isNull(response.getBookingDtoWithId())) {
            ExceptionDto exceptionDto = (ExceptionDto) rabbitTemplate
                    .receiveAndConvert("exception-add-queue");
            int statusCode = Objects.isNull(exceptionDto) ? 500 : exceptionDto.getStatusCode();
            return new ResponseEntity<>(exceptionDto, HttpStatus.valueOf(statusCode));
        }
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<? extends ResponseDto> editBooking(
            @PathVariable @Min(value = 1, message = "id must be more or equals 1") Long id,
            @Valid @RequestBody BookingDtoWithId bookingDtoWithId) {
        bookingDtoWithId.setId(id);
        BookingResponseDto response = bookingService.editBooking(bookingDtoWithId);
        if (Objects.isNull(response.getBookingDtoWithId())) {
            ExceptionDto exceptionDto = (ExceptionDto) rabbitTemplate
                    .receiveAndConvert("exception-edit-queue");
            int statusCode = Objects.isNull(exceptionDto) ? 500 : exceptionDto.getStatusCode();
            return new ResponseEntity<>(exceptionDto, HttpStatus.valueOf(statusCode));
        }
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<? extends ResponseDto> deleteBooking(
            @PathVariable @Min(value = 1, message = "id must be more or equals 1") Long id) {
        EventDto response = bookingService.deleteBooking(id);
        if (Objects.isNull(response.getEventType())) {
            ExceptionDto exceptionDto = (ExceptionDto) rabbitTemplate
                    .receiveAndConvert("exception-delete-queue");
            int statusCode = Objects.isNull(exceptionDto) ? 500 : exceptionDto.getStatusCode();
            return new ResponseEntity<>(exceptionDto, HttpStatus.valueOf(statusCode));
        }
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

}
