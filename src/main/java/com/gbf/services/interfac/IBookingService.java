package com.gbf.services.interfac;

import com.gbf.dtos.Response;
import com.gbf.entities.Booking;

public interface IBookingService {

    Response saveBooking(Long roomId, Long UserId, Booking bookingRequest);

    Response findBookingByConfirmationCode(String confirmationCode);

    Response getAllBookings();

    Response cancelBooking(Long bookingId);
}
