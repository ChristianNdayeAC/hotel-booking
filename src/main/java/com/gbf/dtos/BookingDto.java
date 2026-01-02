package com.gbf.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gbf.entities.Room;
import com.gbf.entities.User;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
