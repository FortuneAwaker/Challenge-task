package by.itechart.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.sql.Time;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingDtoWithId {

    @Min(value = 1, message = "Id can't be less than 1!")
    private Long id;
    @NotNull(message = "Passenger name is mandatory!")
    @Pattern(regexp = "^[A-Z][0-9A-Za-z\\s-]*$", message = "Name should match pattern ^[A-Z][0-9A-Za-z\\s-]*$")
    @Size(min = 3, max = 50, message = "Name should be longer than 3 letters and shorter than 50.")
    private String passengerName;
    @NotNull(message = "Passenger contact number is mandatory!")
    @Size(min = 12, max = 12, message = "Contact number must be 12 digits!")
    @Pattern(regexp = "[0-9]*$", message = "Contact number must contain digits only!")
    private String passengerContactNumber;
    @NotNull(message = "Pickup time is mandatory!")
    private Time pickupTime;
    @NotNull(message = "ASAP flag is mandatory!")
    private Boolean asap;
    @NotNull(message = "Waiting time is mandatory!")
    private Time waitingTime;
    @NotNull(message = "Number of passengers is mandatory!")
    @Min(value = 1, message = "Must be at least 1 passenger!")
    private Integer numberOfPassengers;
    @NotNull(message = "Price is mandatory!")
    @DecimalMin(value = "0.0", message = "Price can't be less than 0!")
    private Double price;
    @NotNull(message = "Rating is mandatory!")
    @DecimalMin(value = "1.0", message = "Rating can't be less than 1.0!")
    private Double rating;
    @NotNull(message = "Trip waypoints set shouldn't be null!")
    private List<@Valid TripWaypointDtoWithId> tripWaypoints;

}
