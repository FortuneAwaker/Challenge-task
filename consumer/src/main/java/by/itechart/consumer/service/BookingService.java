package by.itechart.consumer.service;

import by.itechart.consumer.entity.Booking;
import by.itechart.consumer.exception.ResourceNotFoundException;
import by.itechart.consumer.repository.BookingRepository;
import by.itechart.model.dto.BookingDtoWithId;
import by.itechart.model.dto.BookingDtoWithoutId;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public BookingDtoWithId addBooking(final BookingDtoWithoutId bookingDtoWithoutId) {
        Booking bookingFromDto = objectMapper.convertValue(bookingDtoWithoutId, Booking.class);
        Booking createdBooking = bookingRepository.save(bookingFromDto);
        return objectMapper.convertValue(createdBooking, BookingDtoWithId.class);
    }

    @Transactional
    public BookingDtoWithId editBooking(final BookingDtoWithId bookingDtoWithId) throws ResourceNotFoundException {
        Booking fromDb = bookingRepository.findById(bookingDtoWithId.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Edit. Booking with id "
                        + bookingDtoWithId.getId() + " doesn't exist in DB!")
        );
        Booking bookingToUpdate = objectMapper.convertValue(bookingDtoWithId, Booking.class);
        bookingToUpdate.setCreatedOn(fromDb.getCreatedOn());
        return objectMapper.convertValue(bookingRepository.save(bookingToUpdate), BookingDtoWithId.class);
    }

    @Transactional
    public void deleteBookingById(final Long id) throws ResourceNotFoundException {
        bookingRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Delete. Booking with id " + id + " doesn't exist in DB!")
        );
        bookingRepository.deleteById(id);
    }

}
