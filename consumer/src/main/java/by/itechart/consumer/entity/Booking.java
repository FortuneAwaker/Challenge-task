package by.itechart.consumer.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "passenger_name", nullable = false)
    private String passengerName;

    @Column(name = "passenger_contact_number", nullable = false)
    private String passengerContactNumber;

    @Column(name = "pickup_time", nullable = false)
    private Time pickupTime;

    @Column(name = "asap", nullable = false)
    private Boolean asap;

    @Column(name = "waiting_time", nullable = false)
    private Time waitingTime;

    @Column(name = "number_of_passengers", nullable = false)
    private Integer numberOfPassengers;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "rating", nullable = false)
    private Double rating;

    @CreatedDate
    @Column(name = "created_on", nullable = false)
    private LocalDate createdOn;
    @LastModifiedDate
    @Column(name = "last_modified_on", nullable = false)
    private LocalDate lastModifiedOn;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "booking_id")
    private List<TripWaypoint> tripWaypoints = new ArrayList<>();

}
