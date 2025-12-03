package com.gbf.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gbf.entities.Booking;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomDto {

    private Long id;

    private String roomType;

    private BigDecimal roomPrice;

    private String roomPhotoUrl;

    private String roomDescription;

    private List<BookingDto> bookings;
}
