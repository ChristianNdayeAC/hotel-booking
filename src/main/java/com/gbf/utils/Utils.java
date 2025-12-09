package com.gbf.utils;

import com.gbf.dtos.BookingDto;
import com.gbf.dtos.RoomDto;
import com.gbf.dtos.UserDto;
import com.gbf.entities.Booking;
import com.gbf.entities.Room;
import com.gbf.entities.User;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    private static final String ALPHANUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateRandomAlphaNumeric(int length){
        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < length; i++){
            int randomIndex = secureRandom.nextInt(ALPHANUMERIC_STRING.length());
            char randomChar = ALPHANUMERIC_STRING.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }

        return stringBuilder.toString();
    }

    public static UserDto mapUserEntityToUserDTO(User user){
        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setRole(user.getRole());

        return userDto;
    }

    public static RoomDto mapRoomEntityToRoomDTO(Room room){
        RoomDto roomDto = new RoomDto();

        roomDto.setId(room.getId());
        roomDto.setRoomType(room.getRoomType());
        roomDto.setRoomPrice(room.getRoomPrice());
        roomDto.setRoomPhotoUrl(room.getRoomPhotoUrl());
        roomDto.setRoomDescription(room.getRoomDescription());

        return roomDto;
    }

    public static RoomDto mapRoomEntityToRoomDTOPlusBookings(Room room){
        RoomDto roomDto = new RoomDto();

        roomDto.setId(room.getId());
        roomDto.setRoomType(room.getRoomType());
        roomDto.setRoomPrice(room.getRoomPrice());
        roomDto.setRoomPhotoUrl(room.getRoomPhotoUrl());
        roomDto.setRoomDescription(room.getRoomDescription());

        if(room.getBookings() != null){
            roomDto.setBookings(room.getBookings().stream().map(Utils::mapBookingsEntityToBookingsDTO).collect(Collectors.toList()));
        }

        return roomDto;
    }

    private static BookingDto mapBookingsEntityToBookingsDTO(Booking booking) {
        BookingDto bookingDto = new BookingDto();

        bookingDto.setId(bookingDto.getId());
        bookingDto.setCheckInDate(booking.getCheckInDate());
        bookingDto.setCheckOutDate(booking.getCheckOutDate());
        bookingDto.setNumOfAdults(booking.getNumOfAdults());
        bookingDto.setNumOfChildren(booking.getNumOfChildren());
        bookingDto.setTotalNumOfGuest(booking.getTotalNumOfGuest());
        bookingDto.setBookingConfirmationCode(booking.getBookingConfirmationCode());

        return bookingDto;
    }


    public static UserDto mapUserEntityToUserDTOPlusBookingsAndRooms(User user){
        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setRole(user.getRole());

        if(!user.getBookings().isEmpty()){
            userDto.setBookings(user.getBookings().stream().map(booking -> mapBookingEntityToBookingDTOPlusBookedRooms(booking, false)).collect(Collectors.toList()));
        }

        return userDto;
    }

    private static BookingDto mapBookingEntityToBookingDTOPlusBookedRooms(Booking booking, boolean mapUser) {
        BookingDto bookingDto = new BookingDto();

        bookingDto.setId(bookingDto.getId());
        bookingDto.setCheckInDate(booking.getCheckInDate());
        bookingDto.setCheckOutDate(booking.getCheckOutDate());
        bookingDto.setNumOfAdults(booking.getNumOfAdults());
        bookingDto.setNumOfChildren(booking.getNumOfChildren());
        bookingDto.setTotalNumOfGuest(booking.getTotalNumOfGuest());
        bookingDto.setBookingConfirmationCode(booking.getBookingConfirmationCode());

        if(mapUser){
            bookingDto.setUser(Utils.mapUserEntityToUserDTO(booking.getUser()));
        }
        if(booking.getRoom() != null){

            bookingDto.setRoom(Utils.mapRoomEntityToRoomDTO(booking.getRoom()));
        }

        return bookingDto;
    }

    public static List<UserDto> mapUserListEntityToUserListDTO(List<User> userList){
        return userList.stream().map(Utils::mapUserEntityToUserDTO).collect(Collectors.toList());
    }

    public static List<RoomDto> mapRoomListEntityToRoomListDTO(List<Room> roomList){
        return roomList.stream().map(Utils::mapRoomEntityToRoomDTO).collect(Collectors.toList());
    }

    public static List<BookingDto> mapBookingListEntityToBookingListDTO(List<Booking> bookingList){
        return bookingList.stream().map(Utils::mapBookingsEntityToBookingsDTO).collect(Collectors.toList());
    }
}
