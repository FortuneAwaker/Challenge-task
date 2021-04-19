package by.itechart.consumer.listener;

import by.itechart.consumer.exception.ResourceNotFoundException;
import by.itechart.consumer.service.BookingService;
import by.itechart.model.dto.BookingDtoWithId;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

@EnableRabbit
@Component
@RequiredArgsConstructor
public class EditBookingListener {

    @Value("${rabbit.message-exchange}")
    private String messageExchangeName;

    private final ObjectMapper objectMapper;
    private final BookingService bookingService;
    private final RabbitTemplate rabbitTemplate;
    private final Validator validator;

    Logger logger = LoggerFactory.getLogger(EditBookingListener.class);

    @RabbitListener(queues = {"booking-edit-queue"})
    public void processEditBookingQueue(final String message) {
        try {
            BookingDtoWithId parsedBooking = objectMapper.readValue(message, BookingDtoWithId.class);
            logger.info("Validating booking with id: {}", parsedBooking.getId());
            validateBooking(parsedBooking);
            logger.info("Trying to edit booking with id: {}", parsedBooking.getId());
            BookingDtoWithId updatedBooking = bookingService.editBooking(parsedBooking);
            logger.info("Booking was updated: {}", updatedBooking);
        } catch (JsonProcessingException | ResourceNotFoundException | ConstraintViolationException e) {
            rabbitTemplate.convertAndSend(messageExchangeName,
                    "exception", e.getMessage());
        }
    }

    private void validateBooking(final BookingDtoWithId bookingDtoWithId) {
        Set<ConstraintViolation<BookingDtoWithId>> validates = validator.validate(bookingDtoWithId);
        if (!validates.isEmpty()) {
            throw new ConstraintViolationException(validates);
        }
    }

}
