package com.gbf.services.interfac;

import com.gbf.dtos.LoginRequest;
import com.gbf.dtos.Response;
import com.gbf.entities.User;

public interface IUserService {

    Response register(User user);

    Response login(LoginRequest loginRequest);

    Response getAllUsers();

    Response getUserBookingHistory(String userId);

    Response deleteUser(String userId);

    Response getUserById(String userId);

    Response getMyInfo(String email);
}
