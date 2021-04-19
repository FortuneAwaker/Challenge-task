package by.itechart.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TripWaypointDtoWithId {

    @Min(value = 1, message = "Id can't be less than 1!")
    private Long id;
    @NotNull(message = "Locality is mandatory!")
    private String locality;
    @NotNull(message = "Longitude is mandatory!")
    @DecimalMin(value = "-180.0", message = "Longitude can't be less than -180.0!")
    @DecimalMax(value = "180.0", message = "Longitude can't be less more 180.0!")
    private Double longitude;
    @NotNull(message = "Latitude is mandatory!")
    @DecimalMin(value = "-90.0", message = "Latitude can't be less than -90.0!")
    @DecimalMax(value = "90.0", message = "Latitude can't be more than 90.0!")
    private Double latitude;

}
