package by.itechart.consumer.listener;

import by.itechart.consumer.exception.ResourceNotFoundException;
import by.itechart.consumer.service.BookingService;
import by.itechart.model.dto.EventDto;
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

import java.sql.Timestamp;
import java.time.LocalDateTime;

@EnableRabbit
@Component
@RequiredArgsConstructor
public class DeleteBookingListener {

    @Value("${rabbit.message-exchange}")
    private String messageExchangeName;

    private final BookingService bookingService;
    private final RabbitTemplate rabbitTemplate;

    Logger logger = LoggerFactory.getLogger(DeleteBookingListener.class);

    @RabbitListener(queues = {"booking-delete-queue"})
    public ResponseDto processDeleteBookingQueue(final Long idOfBookingToDelete) {
        try {
            logger.info("Trying to delete booking with id: {}", idOfBookingToDelete);
            bookingService.deleteBookingById(idOfBookingToDelete);
            logger.info("Book with id {} was deleted!", idOfBookingToDelete);
            return new EventDto(HttpStatus.NO_CONTENT.value(), "Book with id = " + idOfBookingToDelete
                    + " was deleted!",
                    Timestamp.valueOf(LocalDateTime.now()).toString(), "delete");
        } catch (ResourceNotFoundException e) {
            rabbitTemplate.convertAndSend(messageExchangeName, "exception", e.getMessage());
            return new ExceptionDto(HttpStatus.NOT_FOUND.value(), e.getMessage(),
                    Timestamp.valueOf(LocalDateTime.now()).toString());
        }
    }

}
