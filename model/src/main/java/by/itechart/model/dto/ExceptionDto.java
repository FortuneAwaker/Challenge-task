package by.itechart.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class ExceptionDto extends ResponseDto {

    public ExceptionDto(final Integer statusCode, final String message, final String timestamp) {
        super(statusCode, message, timestamp);
    }

}
