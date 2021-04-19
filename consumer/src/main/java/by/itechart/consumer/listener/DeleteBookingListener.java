package by.itechart.consumer.listener;

import by.itechart.consumer.exception.ResourceNotFoundException;
import by.itechart.consumer.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
    public void processDeleteBookingQueue(final String message) {
        try {
            Long idOfBookingToDelete = Long.valueOf(message);
            logger.info("Trying to delete booking with id: {}", idOfBookingToDelete);
            bookingService.deleteBookingById(idOfBookingToDelete);
            logger.info("Book with id {} was deleted", idOfBookingToDelete);
        } catch (NumberFormatException | ResourceNotFoundException exception) {
            rabbitTemplate.convertAndSend(messageExchangeName,
                    "exception", exception.getMessage());

        }
    }

}
