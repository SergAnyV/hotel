package com.asv.hotel.services;

import com.asv.hotel.entities.User;

public interface UserInternalService extends UserService {

    User findUserByLastNameAndFirstName(String lastName, String firstName);

    User findUserByNickName(String nickName);
}
