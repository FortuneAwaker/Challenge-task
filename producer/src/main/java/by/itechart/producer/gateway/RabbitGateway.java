package by.itechart.producer.gateway;

import by.itechart.model.dto.BookingDtoWithId;
import by.itechart.model.dto.BookingDtoWithoutId;
import by.itechart.model.dto.BookingResponseDto;
import by.itechart.model.dto.EventDto;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(errorChannel = "exception")
public interface RabbitGateway {

    @Gateway(requestChannel = "add-booking")
    BookingResponseDto addBooking(BookingDtoWithoutId bookingDtoWithoutId);

    @Gateway(requestChannel = "edit-booking")
    BookingResponseDto editBooking(BookingDtoWithId bookingDtoWithId);

    @Gateway(requestChannel = "delete-booking")
    EventDto deleteBooking(Long id);

}
