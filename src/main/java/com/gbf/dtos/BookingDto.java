package com.gbf.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingDto {

    private Long id;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private Integer numOfAdults;

    private Integer numOfChildren;

    private Integer totalNumOfGuest;

    private String bookingConfirmationCode;

    private UserDto user;

    private RoomDto room;
}
