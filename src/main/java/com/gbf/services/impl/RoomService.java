package com.gbf.services.impl;

import com.gbf.dtos.Response;
import com.gbf.dtos.RoomDto;
import com.gbf.entities.Room;
import com.gbf.exceptions.OurException;
import com.gbf.repositories.BookingRepository;
import com.gbf.repositories.RoomRepository;
import com.gbf.services.AwsS3Service;
import com.gbf.services.interfac.IRoomService;
import com.gbf.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class  RoomService implements IRoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private AwsS3Service awsS3Service;


    @Override
    public Response addNewRoom(MultipartFile file, String roomType, BigDecimal roomPrice, String description) {

        Response response = new Response();

        try{
            String imageUrl = awsS3Service.saveImageToS3(file);
            Room room = new Room();
            room.setRoomPhotoUrl(imageUrl);
            room.setRoomType(roomType);
            room.setRoomPrice(roomPrice);
            room.setRoomDescription(description);
            Room savedRoom = roomRepository.save(room);
            RoomDto roomDto = Utils.mapRoomEntityToRoomDTO(savedRoom);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setRoom(roomDto);


        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error saving a room." + e.getMessage());
        }

        return response;
    }

    @Override
    public List<String> getAllRoomTypes() {

        return roomRepository.findDistinctRoomTypes();
    }

    @Override
    public Response getAllRooms() {
        Response response = new Response();

        try{
            List<Room> roomList = roomRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<RoomDto> roomDtoList = Utils.mapRoomListEntityToRoomListDTO(roomList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setRoomList(roomDtoList);


        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error while getting all rooms." + e.getMessage());
        }

        return response;
    }

    @Override
    public Response deleteRoom(Long roomId) {
        Response response = new Response();

        try{
            roomRepository.findById(roomId).orElseThrow(() -> new OurException("Room Not Found."));
            roomRepository.deleteById(roomId);
            response.setStatusCode(200);
            response.setMessage("successful");


        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error while deleting a room." + e.getMessage());
        }

        return response;
    }

    @Override
    public Response updateRoom(Long roomId, String roomType, BigDecimal roomPrice, MultipartFile file, String description) {
        Response response = new Response();

        try{
            String imageUrl = null;
            if(file != null && !file.isEmpty()){
                imageUrl = awsS3Service.saveImageToS3(file);
            }

            Room room = roomRepository.findById(roomId).orElseThrow(() -> new OurException("Room not found."));

            if(roomType != null){
                room.setRoomType(roomType);
            }
            if(roomPrice != null){
                room.setRoomPrice(roomPrice);
            }
            if(description != null){
                room.setRoomDescription(description);
            }
            if(imageUrl != null){
                room.setRoomPhotoUrl(imageUrl);
            }
            Room updatedRoom = roomRepository.save(room);
            RoomDto roomDto = Utils.mapRoomEntityToRoomDTO(updatedRoom);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setRoom(roomDto);


        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error updating a room." + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getRoomById(Long roomId) {
        Response response = new Response();

        try{
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new OurException("Room Not Found."));
            RoomDto roomDto = Utils.mapRoomEntityToRoomDTOPlusBookings(room);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setRoom(roomDto);


        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error while getting a room." + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getAvailableRoomsByDateAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        Response response = new Response();

        try{
            List<Room> availableRooms = roomRepository.findAvailableRoomsByDatesandTypes(checkInDate, checkOutDate, roomType);
            List<RoomDto> roomDtoList = Utils.mapRoomListEntityToRoomListDTO(availableRooms);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setRoomList(roomDtoList);


        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error while getting a room." + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getAllAvailableRooms() {
        Response response = new Response();

        try{
            List<Room> availableRoomList = roomRepository.getAllAvailableRooms();
            List<RoomDto> roomDtoList = Utils.mapRoomListEntityToRoomListDTO(availableRoomList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setRoomList(roomDtoList);


        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error while getting all available rooms." + e.getMessage());
        }

        return response;
    }
}
