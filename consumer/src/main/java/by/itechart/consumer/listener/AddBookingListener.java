package by.itechart.consumer.listener;

import by.itechart.consumer.service.BookingService;
import by.itechart.model.dto.BookingDtoWithId;
import by.itechart.model.dto.BookingDtoWithoutId;
import by.itechart.model.dto.BookingResponseDto;
import by.itechart.model.dto.ExceptionDto;
import by.itechart.model.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;

@EnableRabbit
@Component
@RequiredArgsConstructor
public class AddBookingListener {

    @Value("${rabbit.message-exchange}")
    private String messageExchangeName;

    private final BookingService bookingService;
    private final RabbitTemplate rabbitTemplate;
    private final Validator validator;

    Logger logger = LoggerFactory.getLogger(AddBookingListener.class);

    @RabbitListener(queues = {"booking-add-queue"})
    public ResponseDto processAddBookingQueue(final BookingDtoWithoutId bookingToAdd) {
        try {
            logger.info("Validating new booking");
            validateBooking(bookingToAdd);
            logger.info("Trying to add new booking");
            BookingDtoWithId createdBooking = bookingService.addBooking(bookingToAdd);
            logger.info("Booking was created: {}", createdBooking);
            return new BookingResponseDto(HttpStatus.CREATED.value(), "Booking was created!",
                    Timestamp.valueOf(LocalDateTime.now()).toString(), createdBooking);
        } catch (ConstraintViolationException e) {
            rabbitTemplate.convertAndSend(messageExchangeName, "exception", e.getMessage());
            return new ExceptionDto(HttpStatus.UNPROCESSABLE_ENTITY.value(), e.getMessage(),
                    Timestamp.valueOf(LocalDateTime.now()).toString());
        }
    }

    private void validateBooking(final BookingDtoWithoutId bookingDtoWithoutId) {
        Set<ConstraintViolation<BookingDtoWithoutId>> validates = validator.validate(bookingDtoWithoutId);
        if (!validates.isEmpty()) {
            throw new ConstraintViolationException(validates);
        }
    }

}
