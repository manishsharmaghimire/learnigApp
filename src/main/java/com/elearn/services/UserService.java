package com.elearn.services;

import com.elearn.dto.UserDto;
import com.elearn.dto.UserResponseDto;

public interface UserService {

    UserResponseDto create(UserDto dto);

    UserDto getById(String userId);
}
