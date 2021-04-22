package by.itechart.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExceptionDto implements ResponseDto {

    private Integer statusCode;
    private String message;
    private String timestamp;

}
