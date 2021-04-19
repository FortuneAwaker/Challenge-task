package by.itechart.consumer.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@EnableRabbit
@Component
public class ExceptionListener {

    Logger logger = LoggerFactory.getLogger(ExceptionListener.class);

    @RabbitListener(queues = {"exception-queue"})
    public void processEditBookingQueue(final String message) {
        logger.info("Received from exception queue: {}", message);
    }

}
