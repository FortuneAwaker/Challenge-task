package by.itechart.producer.controller;

import by.itechart.model.dto.BookingDtoWithId;
import by.itechart.model.dto.BookingDtoWithoutId;
import by.itechart.model.dto.EventDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookings")
@Validated
public class BookingController {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    private static final String ADD_LITERAL = "add";
    private static final String EDIT_LITERAL = "edit";
    private static final String DELETE_LITERAL = "delete";
    private static final String AUDIT_LITERAL = "audit";
    @Value("${rabbit.message-exchange}")
    private String directExchangeLiteral;
    @Value("${rabbit.booking-exchange}")
    private String bookingExchangeLiteral;

    @PostMapping
    public ResponseEntity<EventDto> addBooking(@Valid @RequestBody BookingDtoWithoutId bookingDtoWithoutId)
            throws JsonProcessingException {
        String bookingDtoJson = objectMapper.writeValueAsString(bookingDtoWithoutId);
        rabbitTemplate.convertAndSend(bookingExchangeLiteral, ADD_LITERAL, bookingDtoJson);
        rabbitTemplate.convertAndSend(directExchangeLiteral, AUDIT_LITERAL,
                "Message to create booking was sent.");
        return new ResponseEntity<>(createEventDto(HttpStatus.OK.value(),
                "Message to add booking was sent successfully!", ADD_LITERAL), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventDto> editBooking(
            @PathVariable @Min(value = 1, message = "id must be more or equals 1") Long id,
            @Valid @RequestBody BookingDtoWithId bookingDtoWithId)
            throws JsonProcessingException {
        bookingDtoWithId.setId(id);
        String bookingDtoJson = objectMapper.writeValueAsString(bookingDtoWithId);
        rabbitTemplate.convertAndSend(bookingExchangeLiteral, EDIT_LITERAL, bookingDtoJson);
        rabbitTemplate.convertAndSend(directExchangeLiteral, AUDIT_LITERAL,
                "Message to edit booking with id " + id + " was sent.");
        return new ResponseEntity<>(createEventDto(HttpStatus.OK.value(),
                "Message to edit booking was sent successfully!", EDIT_LITERAL), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EventDto> deleteBooking(
            @PathVariable @Min(value = 1, message = "id must be more or equals 1") Long id) {
        rabbitTemplate.convertAndSend(bookingExchangeLiteral, DELETE_LITERAL, id);
        rabbitTemplate.convertAndSend(directExchangeLiteral, AUDIT_LITERAL,
                "Message to delete booking with id " + id + " was sent.");
        return new ResponseEntity<>(createEventDto(HttpStatus.OK.value(),
                "Message to delete booking was sent successfully!", DELETE_LITERAL), HttpStatus.OK);
    }

    private EventDto createEventDto(final int statusCode, final String message, String eventType) {
        return new EventDto(statusCode, message, Timestamp.valueOf(LocalDateTime.now()).toString(), eventType);
    }
}
