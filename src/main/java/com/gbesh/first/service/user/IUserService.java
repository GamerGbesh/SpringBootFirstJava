package com.gbesh.first.service.user;

import com.gbesh.first.dto.UserDto;

public interface IUserService {
    UserDto getUser(String username, String password);
    UserDto saveUser(String username, String email, String password);
}
