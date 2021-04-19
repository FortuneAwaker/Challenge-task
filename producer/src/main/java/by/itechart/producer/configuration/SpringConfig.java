package by.itechart.producer.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan(basePackages = "by.itechart.producer")
@EnableWebMvc
public class SpringConfig implements WebMvcConfigurer {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
