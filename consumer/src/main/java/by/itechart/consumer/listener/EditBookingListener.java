package by.itechart.consumer.listener;

import by.itechart.consumer.exception.ResourceNotFoundException;
import by.itechart.consumer.service.BookingService;
import by.itechart.model.dto.BookingDtoWithId;
import by.itechart.model.dto.BookingResponseDto;
import by.itechart.model.dto.ExceptionDto;
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
public class EditBookingListener {

    @Value("${rabbit.message-exchange}")
    private String messageExchangeName;

    private final BookingService bookingService;
    private final RabbitTemplate rabbitTemplate;
    private final Validator validator;

    Logger logger = LoggerFactory.getLogger(EditBookingListener.class);

    @RabbitListener(queues = {"booking-edit-queue"})
    public BookingResponseDto processEditBookingQueue(final BookingDtoWithId bookingToEdit) {
        try {
            logger.info("Validating booking with id: {}", bookingToEdit.getId());
            validateBooking(bookingToEdit);
            logger.info("Trying to edit booking with id: {}", bookingToEdit.getId());
            BookingDtoWithId updatedBooking = bookingService.editBooking(bookingToEdit);
            logger.info("Booking was updated: {}", updatedBooking);
            return new BookingResponseDto(HttpStatus.CREATED.value(),
                    "Booking with id " + updatedBooking.getId() + " was edited!",
                    Timestamp.valueOf(LocalDateTime.now()).toString(), updatedBooking);
        } catch (ConstraintViolationException e) {
            rabbitTemplate.convertAndSend(messageExchangeName, "exception", e.getMessage());
            rabbitTemplate.convertAndSend(messageExchangeName, "exception-edit",
                    new ExceptionDto(HttpStatus.UNPROCESSABLE_ENTITY.value(), e.getMessage(),
                            Timestamp.valueOf(LocalDateTime.now()).toString()));
        } catch (ResourceNotFoundException e) {
            rabbitTemplate.convertAndSend(messageExchangeName, "exception", e.getMessage());
            rabbitTemplate.convertAndSend(messageExchangeName, "exception-edit",
                    new ExceptionDto(HttpStatus.NOT_FOUND.value(), e.getMessage(),
                            Timestamp.valueOf(LocalDateTime.now()).toString()));
        }
        return new BookingResponseDto();
    }

    private void validateBooking(final BookingDtoWithId bookingDtoWithId) {
        Set<ConstraintViolation<BookingDtoWithId>> validates = validator.validate(bookingDtoWithId);
        if (!validates.isEmpty()) {
            throw new ConstraintViolationException(validates);
        }
    }

}
