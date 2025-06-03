package com.elearn.services;

import com.elearn.dto.UserDto;

public interface UserService {

    UserDto create(UserDto dto);
    UserDto geById(String userId);

}
