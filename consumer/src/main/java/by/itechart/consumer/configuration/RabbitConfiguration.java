package by.itechart.consumer.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;

@Configuration
@PropertySource("classpath:application.properties")
public class RabbitConfiguration implements RabbitListenerConfigurer {

    @Value("${rabbit.message-exchange}")
    private String messageExchangeName;
    @Value("${rabbit.booking-exchange}")
    private String bookingExchangeName;

    @Value("${rabbit.booking-add-queue}")
    private String addBookingQueueName;
    @Value("${rabbit.booking-edit-queue}")
    private String editBookingQueueName;
    @Value("${rabbit.booking-delete-queue}")
    private String deleteBookingQueueName;
    @Value("${rabbit.message-audit-queue}")
    private String messageAuditQueueName;
    @Value("${rabbit.exception-queue}")
    private String exceptionQueueName;

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
    }

    @Bean
    public MessageHandlerMethodFactory messageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory messageHandlerMethodFactory = new DefaultMessageHandlerMethodFactory();
        messageHandlerMethodFactory.setMessageConverter(consumerJackson2MessageConverter());
        return messageHandlerMethodFactory;
    }

    @Bean
    public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
        return new MappingJackson2MessageConverter();
    }

    @Bean
    public Queue addBookingQueue() {
        return new Queue(addBookingQueueName);
    }

    @Bean
    public Queue editBookingQueue() {
        return new Queue(editBookingQueueName);
    }

    @Bean
    public Queue deleteBookingQueue() {
        return new Queue(deleteBookingQueueName);
    }

    @Bean
    public Queue auditQueue() {
        return new Queue(messageAuditQueueName);
    }

    @Bean
    public Queue exceptionQueue() {
        return new Queue(exceptionQueueName);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(messageExchangeName);
    }

    @Bean
    public DirectExchange bookingExchange() {
        return new DirectExchange(bookingExchangeName);
    }

    @Bean
    public Binding bindingAddBookingExchange() {
        return BindingBuilder.bind(bookingExchange()).to(directExchange()).with("add");
    }

    @Bean
    public Binding bindingAddBookingQueueToBookingExchange() {
        return BindingBuilder.bind(addBookingQueue()).to(bookingExchange()).with("add");
    }

    @Bean
    public Binding bindingEditBookingExchange() {
        return BindingBuilder.bind(bookingExchange()).to(directExchange()).with("edit");
    }

    @Bean
    public Binding bindingEditBookingQueueToBookingExchange() {
        return BindingBuilder.bind(editBookingQueue()).to(bookingExchange()).with("edit");
    }

    @Bean
    public Binding bindingDeleteBookingExchange() {
        return BindingBuilder.bind(bookingExchange()).to(directExchange()).with("delete");
    }

    @Bean
    public Binding bindingDeleteBookingQueueToBookingExchange() {
        return BindingBuilder.bind(deleteBookingQueue()).to(bookingExchange()).with("delete");
    }

    @Bean
    public Binding bindingAudit() {
        return BindingBuilder.bind(auditQueue()).to(directExchange()).with("audit");
    }

    @Bean
    public Binding bindingException() {
        return BindingBuilder.bind(exceptionQueue()).to(directExchange()).with("exception");
    }

}
