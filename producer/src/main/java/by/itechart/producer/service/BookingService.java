package by.itechart.producer.service;

import by.itechart.model.dto.BookingDtoWithId;
import by.itechart.model.dto.BookingDtoWithoutId;
import by.itechart.model.dto.BookingResponseDto;
import by.itechart.model.dto.EventDto;
import by.itechart.model.dto.ResponseDto;
import by.itechart.producer.gateway.RabbitGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitGateway rabbitGateway;

    private static final String AUDIT_LITERAL = "audit";
    @Value("${rabbit.message-exchange}")
    private String directExchangeLiteral;

    public BookingResponseDto addBooking(final BookingDtoWithoutId bookingDtoWithoutId) {
        BookingResponseDto response = rabbitGateway.addBooking(bookingDtoWithoutId);
        rabbitTemplate.convertAndSend(directExchangeLiteral, AUDIT_LITERAL,
                "Message to create booking was sent.");
        return response;
    }

    public BookingResponseDto editBooking(final BookingDtoWithId bookingDtoWithId) {
        BookingResponseDto response = rabbitGateway.editBooking(bookingDtoWithId);
        rabbitTemplate.convertAndSend(directExchangeLiteral, AUDIT_LITERAL,
                "Message to edit booking with id " + bookingDtoWithId.getId() + " was sent.");
        return response;
    }

    public EventDto deleteBooking(final Long id) {
        EventDto response = rabbitGateway.deleteBooking(id);
        rabbitTemplate.convertAndSend(directExchangeLiteral, AUDIT_LITERAL,
                "Message to delete booking with id " + id + " was sent.");
        return response;
    }
}
