package by.itechart.producer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.amqp.outbound.AmqpOutboundEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OutboundGatewayService {

    private static final String ADD_LITERAL = "add";
    private static final String EDIT_LITERAL = "edit";
    private static final String DELETE_LITERAL = "delete";
    @Value("${rabbit.booking-exchange}")
    private String bookingExchangeLiteral;

    @Bean
    @ServiceActivator(inputChannel = "add-booking")
    public AmqpOutboundEndpoint addBookingOutbound(AmqpTemplate amqpTemplate) {
        AmqpOutboundEndpoint outbound = new AmqpOutboundEndpoint(amqpTemplate);
        outbound.setExpectReply(true);
        outbound.setExchangeName(bookingExchangeLiteral);
        outbound.setRoutingKey(ADD_LITERAL);
        return outbound;
    }

    @Bean
    @ServiceActivator(inputChannel = "edit-booking")
    public AmqpOutboundEndpoint editBookingOutbound(AmqpTemplate amqpTemplate) {
        AmqpOutboundEndpoint outbound = new AmqpOutboundEndpoint(amqpTemplate);
        outbound.setExpectReply(true);
        outbound.setExchangeName(bookingExchangeLiteral);
        outbound.setRoutingKey(EDIT_LITERAL);
        return outbound;
    }

    @Bean
    @ServiceActivator(inputChannel = "delete-booking")
    public AmqpOutboundEndpoint deleteBookingOutbound(AmqpTemplate amqpTemplate) {
        AmqpOutboundEndpoint outbound = new AmqpOutboundEndpoint(amqpTemplate);
        outbound.setExpectReply(true);
        outbound.setExchangeName(bookingExchangeLiteral);
        outbound.setRoutingKey(DELETE_LITERAL);
        return outbound;
    }

}
