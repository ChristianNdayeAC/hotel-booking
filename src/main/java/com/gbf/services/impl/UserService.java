package com.gbf.services.impl;

import com.gbf.dtos.LoginRequest;
import com.gbf.dtos.Response;
import com.gbf.dtos.UserDto;
import com.gbf.entities.User;
import com.gbf.exceptions.OurException;
import com.gbf.repositories.UserRepository;
import com.gbf.services.interfac.IUserService;
import com.gbf.utils.JWTUtils;
import com.gbf.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;


    @Override
    public Response register(User user) {
        Response response = new Response();

        try{
            if(user.getRole() == null || user.getRole().isBlank()){
                user.setRole("USER");
            }
            if(userRepository.existsByEmail(user.getEmail())){
                throw new OurException(user.getEmail() + " already exists, please try another email.");
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);
            UserDto userDto = Utils.mapUserEntityToUserDTO(savedUser);
            response.setStatusCode(200);
            response.setUser(userDto);

        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error occurred during user registration " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response login(LoginRequest loginRequest) {

        Response response = new Response();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new OurException("User not found"));

            var token = jwtUtils.generateToken(user);
            response.setStatusCode(200);
            response.setToken(token);
            response.setRole(user.getRole());
            response.getExpirationTime();
            response.setMessage("Login successfully");

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error occurred during user login " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getAllUsers() {

        Response response = new Response();

        try {
            List<User> userList = userRepository.findAll();
            List<UserDto> userDtoList = Utils.mapUserListEntityToUserListDTO(userList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setUserList(userDtoList);

        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting all users " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getUserBookingHistory(String userId) {

        Response response = new Response();

        try{
            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new OurException("User not found"));
            UserDto userDto = Utils.mapUserEntityToUserDTOPlusBookingsAndRooms(user);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setUser(userDto);


        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error occurred while getting user's booking history " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response deleteUser(String userId) {
        Response response = new Response();

        try{
            userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new OurException("User not found"));
            userRepository.deleteById(Long.valueOf(userId));
            response.setStatusCode(200);
            response.setMessage("successful");

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error occurred while deleting a user  " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getUserById(String userId) {
        Response response = new Response();

        try{
            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new OurException("User not found"));
            UserDto userDto = Utils.mapUserEntityToUserDTO(user);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setUser(userDto);


        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error occurred while getting a user " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getMyInfo(String email) {
        Response response = new Response();

        try{
            User user = userRepository.findByEmail(email).orElseThrow(() -> new OurException("User not found"));
            UserDto userDto = Utils.mapUserEntityToUserDTOPlusBookingsAndRooms(user);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setUser(userDto);


        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error occurred while getting user's booking history " + e.getMessage());
        }

        return response;
    }
}
