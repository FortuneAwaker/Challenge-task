package by.itechart.consumer.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@EnableRabbit
@Component
public class AuditListener {

    Logger logger = LoggerFactory.getLogger(AuditListener.class);

    @RabbitListener(queues = {"message-audit-queue"})
    public void processAuditQueue(final String message) {
        logger.info("Received from audit queue: {}", message);
    }

}
