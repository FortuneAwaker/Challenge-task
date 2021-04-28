package by.itechart.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class BookingResponseDto extends ResponseDto {

    private BookingDtoWithId bookingDtoWithId;

    public BookingResponseDto(final Integer statusCode, final String message, final String timestamp,
                    final BookingDtoWithId bookingDtoWithId) {
        super(statusCode, message, timestamp);
        this.bookingDtoWithId = bookingDtoWithId;
    }

}
