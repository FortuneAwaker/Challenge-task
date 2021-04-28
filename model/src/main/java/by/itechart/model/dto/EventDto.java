package by.itechart.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class EventDto extends ResponseDto {

    private String eventType;

    public EventDto(final Integer statusCode, final String message, final String timestamp, final String eventType) {
        super(statusCode, message, timestamp);
        this.eventType = eventType;
    }

}
