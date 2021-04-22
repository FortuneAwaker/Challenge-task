package by.itechart.producer.service;

import by.itechart.model.dto.BookingDtoWithId;
import by.itechart.model.dto.BookingDtoWithoutId;
import by.itechart.model.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final RabbitTemplate rabbitTemplate;

    private static final String ADD_LITERAL = "add";
    private static final String EDIT_LITERAL = "edit";
    private static final String DELETE_LITERAL = "delete";
    private static final String AUDIT_LITERAL = "audit";
    @Value("${rabbit.message-exchange}")
    private String directExchangeLiteral;
    @Value("${rabbit.booking-exchange}")
    private String bookingExchangeLiteral;

    public ResponseDto addBooking(final BookingDtoWithoutId bookingDtoWithoutId) {
        ResponseDto response = (ResponseDto) rabbitTemplate
                .convertSendAndReceive(bookingExchangeLiteral, ADD_LITERAL, bookingDtoWithoutId);
        rabbitTemplate.convertAndSend(directExchangeLiteral, AUDIT_LITERAL,
                "Message to create booking was sent.");
        return response;
    }

    public ResponseDto editBooking(final BookingDtoWithId bookingDtoWithId) {
        ResponseDto response = (ResponseDto) rabbitTemplate
                .convertSendAndReceive(bookingExchangeLiteral, EDIT_LITERAL, bookingDtoWithId);
        rabbitTemplate.convertAndSend(directExchangeLiteral, AUDIT_LITERAL,
                "Message to edit booking with id " + bookingDtoWithId.getId() + " was sent.");
        return response;
    }

    public ResponseDto deleteBooking(final Long id) {
        ResponseDto response = (ResponseDto) rabbitTemplate
                .convertSendAndReceive(bookingExchangeLiteral, DELETE_LITERAL, id);
        rabbitTemplate.convertAndSend(directExchangeLiteral, AUDIT_LITERAL,
                "Message to delete booking with id " + id + " was sent.");
        return response;
    }
}
