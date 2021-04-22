package by.itechart.consumer.service.booking;

import by.itechart.consumer.entity.Booking;
import by.itechart.consumer.entity.TripWaypoint;
import by.itechart.consumer.exception.ResourceNotFoundException;
import by.itechart.consumer.repository.BookingRepository;
import by.itechart.consumer.service.BookingService;
import by.itechart.model.dto.BookingDtoWithId;
import by.itechart.model.dto.BookingDtoWithoutId;
import by.itechart.model.dto.TripWaypointDtoWithId;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private BookingService bookingService;

    @Test
    void BookingDtoToEdit_EditBookingWithIdThatDoesNotExist_ThrowResourceNotFoundException() {
        // given
        Long bookingId = 12503L;
        TripWaypointDtoWithId waypoint = TripWaypointDtoWithId.builder()
                .locality("Minsk")
                .latitude(53.893009)
                .longitude(27.567444)
                .build();
        List<TripWaypointDtoWithId> waypoints = new ArrayList<>();
        waypoints.add(waypoint);
        BookingDtoWithId bookingToEdit = BookingDtoWithId.builder()
                .id(bookingId)
                .passengerName("Alex")
                .passengerContactNumber("123456654321")
                .asap(false)
                .pickupTime(Time.valueOf("11:00:00"))
                .waitingTime(Time.valueOf("12:00:00"))
                .numberOfPassengers(20)
                .price(50.3)
                .rating(7.8)
                .tripWaypoints(waypoints)
                .build();
        // when
        Mockito.when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());
        // then
        Assertions.assertThrows(ResourceNotFoundException.class, () -> bookingService.editBooking(bookingToEdit));

    }

    @Test
    void IdOfBookingToDelete_DeleteBookingByIdThatDoesNotExist_ThrowResourceNotFoundException() {
        // given
        Long bookingId = 12503L;
        // when
        Mockito.when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());
        // then
        Assertions.assertThrows(ResourceNotFoundException.class, () -> bookingService.deleteBookingById(bookingId));

    }

    @Test
    void BookingDtoToAdd_AddBooking_ReturnAddedBookingDto() {
        // given
        Long bookingId = 1L;
        TripWaypointDtoWithId waypointDtoBeforeAdd = TripWaypointDtoWithId.builder()
                .locality("Minsk")
                .latitude(53.893009)
                .longitude(27.567444)
                .build();
        TripWaypoint waypointBeforeAdd = TripWaypoint.builder()
                .locality("Minsk")
                .latitude(53.893009)
                .longitude(27.567444)
                .build();
        TripWaypointDtoWithId waypointDtoAfterAdd = TripWaypointDtoWithId.builder()
                .id(1L)
                .locality("Minsk")
                .latitude(53.893009)
                .longitude(27.567444)
                .build();
        TripWaypoint waypointAfterAdd = TripWaypoint.builder()
                .id(1L)
                .locality("Minsk")
                .latitude(53.893009)
                .longitude(27.567444)
                .build();

        List<TripWaypointDtoWithId> waypointDtoSetBeforeAdd = new ArrayList<>();
        List<TripWaypointDtoWithId> waypointDtoSetAfterAdd = new ArrayList<>();
        List<TripWaypoint> waypointSetBeforeAdd = new ArrayList<>();
        List<TripWaypoint> waypointSetAfterAdd = new ArrayList<>();

        waypointDtoSetBeforeAdd.add(waypointDtoBeforeAdd);
        waypointDtoSetAfterAdd.add(waypointDtoAfterAdd);
        waypointSetBeforeAdd.add(waypointBeforeAdd);
        waypointSetAfterAdd.add(waypointAfterAdd);

        BookingDtoWithoutId bookingDtoToAdd = BookingDtoWithoutId.builder()
                .passengerName("Alex")
                .passengerContactNumber("123456654321")
                .asap(false)
                .pickupTime(Time.valueOf("11:00:00"))
                .waitingTime(Time.valueOf("12:00:00"))
                .numberOfPassengers(20)
                .price(50.3)
                .rating(7.8)
                .tripWaypoints(waypointDtoSetBeforeAdd)
                .build();
        Booking bookingToAdd = Booking.builder()
                .passengerName("Alex")
                .passengerContactNumber("123456654321")
                .asap(false)
                .pickupTime(Time.valueOf("11:00:00"))
                .waitingTime(Time.valueOf("12:00:00"))
                .numberOfPassengers(20)
                .price(50.3)
                .rating(7.8)
                .tripWaypoints(waypointSetBeforeAdd)
                .build();
        BookingDtoWithId addedBookingDto = BookingDtoWithId.builder()
                .id(bookingId)
                .passengerName("Alex")
                .passengerContactNumber("123456654321")
                .asap(false)
                .pickupTime(Time.valueOf("11:00:00"))
                .waitingTime(Time.valueOf("12:00:00"))
                .numberOfPassengers(20)
                .price(50.3)
                .rating(7.8)
                .tripWaypoints(waypointDtoSetAfterAdd)
                .build();
        Booking addedBooking = Booking.builder()
                .id(bookingId)
                .passengerName("Alex")
                .passengerContactNumber("123456654321")
                .asap(false)
                .pickupTime(Time.valueOf("11:00:00"))
                .waitingTime(Time.valueOf("12:00:00"))
                .numberOfPassengers(20)
                .price(50.3)
                .rating(7.8)
                .tripWaypoints(waypointSetAfterAdd)
                .build();

        // when
        Mockito.when(objectMapper.convertValue(bookingDtoToAdd, Booking.class)).thenReturn(bookingToAdd);
        Mockito.when(objectMapper.convertValue(addedBooking, BookingDtoWithId.class)).thenReturn(addedBookingDto);
        Mockito.when(bookingRepository.save(bookingToAdd)).thenReturn(addedBooking);
        // then
        Assertions.assertEquals(addedBookingDto, bookingService.addBooking(bookingDtoToAdd));

    }

    @Test
    void BookingDtoToEdit_EditBooking_ReturnEditedBookingDto() throws ResourceNotFoundException {
        // given
        Long bookingId = 1L;
        TripWaypointDtoWithId waypointDtoBeforeEdit = TripWaypointDtoWithId.builder()
                .id(1L)
                .locality("Minsk")
                .latitude(53.893009)
                .longitude(27.567444)
                .build();
        TripWaypoint waypointBeforeEdit = TripWaypoint.builder()
                .id(1L)
                .locality("Minsk")
                .latitude(53.893009)
                .longitude(27.567444)
                .build();
        TripWaypoint waypointAfterEdit = TripWaypoint.builder()
                .id(1L)
                .locality("Brest")
                .latitude(52.097622)
                .longitude(23.734051)
                .build();

        List<TripWaypointDtoWithId> waypointDtoSetBeforeEdit = new ArrayList<>();
        List<TripWaypoint> waypointSetBeforeEdit = new ArrayList<>();
        List<TripWaypoint> waypointSetAfterEdit = new ArrayList<>();

        waypointDtoSetBeforeEdit.add(waypointDtoBeforeEdit);
        waypointSetBeforeEdit.add(waypointBeforeEdit);
        waypointSetAfterEdit.add(waypointAfterEdit);

        Booking bookingVersionFromDB = Booking.builder()
                .id(bookingId)
                .passengerName("Alex")
                .passengerContactNumber("123456654321")
                .asap(false)
                .pickupTime(Time.valueOf("11:00:00"))
                .waitingTime(Time.valueOf("12:00:00"))
                .numberOfPassengers(20)
                .price(50.3)
                .rating(7.8)
                .tripWaypoints(waypointSetBeforeEdit)
                .build();
        BookingDtoWithId bookingDtoToEdit = BookingDtoWithId.builder()
                .id(bookingId)
                .passengerName("Alexander")
                .passengerContactNumber("778877887788")
                .asap(true)
                .pickupTime(Time.valueOf("12:00:00"))
                .waitingTime(Time.valueOf("13:00:00"))
                .numberOfPassengers(30)
                .price(70.3)
                .rating(8.8)
                .tripWaypoints(waypointDtoSetBeforeEdit)
                .build();
        Booking editedBooking = Booking.builder()
                .id(bookingId)
                .passengerName("Alexander")
                .passengerContactNumber("778877887788")
                .asap(true)
                .pickupTime(Time.valueOf("12:00:00"))
                .waitingTime(Time.valueOf("13:00:00"))
                .numberOfPassengers(30)
                .price(70.3)
                .rating(8.8)
                .tripWaypoints(waypointSetAfterEdit)
                .build();

        // when
        Mockito.when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(bookingVersionFromDB));
        Mockito.when(objectMapper.convertValue(bookingDtoToEdit, Booking.class)).thenReturn(editedBooking);
        Mockito.when(objectMapper.convertValue(editedBooking, BookingDtoWithId.class)).thenReturn(bookingDtoToEdit);
        Mockito.when(bookingRepository.save(editedBooking)).thenReturn(editedBooking);
        // then
        Assertions.assertEquals(bookingDtoToEdit, bookingService.editBooking(bookingDtoToEdit));

    }

}
