package by.itechart.consumer.listener;

import by.itechart.consumer.service.BookingService;
import by.itechart.model.dto.BookingDtoWithId;
import by.itechart.model.dto.BookingDtoWithoutId;
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
public class AddBookingListener {

    @Value("${rabbit.message-exchange}")
    private String messageExchangeName;

    private final ObjectMapper objectMapper;
    private final BookingService bookingService;
    private final RabbitTemplate rabbitTemplate;
    private final Validator validator;

    Logger logger = LoggerFactory.getLogger(AddBookingListener.class);

    @RabbitListener(queues = {"booking-add-queue"})
    public void processAddBookingQueue(final String message) {
        try {
            BookingDtoWithoutId parsedBooking = objectMapper.readValue(message, BookingDtoWithoutId.class);
            logger.info("Validating new booking");
            validateBooking(parsedBooking);
            logger.info("Trying to add new booking");
            BookingDtoWithId createdBooking = bookingService.addBooking(parsedBooking);
            logger.info("Booking was created: {}", createdBooking);
        } catch (JsonProcessingException | ConstraintViolationException e) {
            rabbitTemplate.convertAndSend(messageExchangeName,
                    "exception", e.getMessage());
        }
    }

    private void validateBooking(final BookingDtoWithoutId bookingDtoWithoutId) {
        Set<ConstraintViolation<BookingDtoWithoutId>> validates = validator.validate(bookingDtoWithoutId);
        if (!validates.isEmpty()) {
            throw new ConstraintViolationException(validates);
        }
    }

}
