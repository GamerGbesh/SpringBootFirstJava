package com.gbesh.first.service.user;

import com.gbesh.first.dto.UserDto;
import com.gbesh.first.exceptions.AlreadyExistsException;
import com.gbesh.first.exceptions.IncorrectPasswordException;
import com.gbesh.first.exceptions.ResourceNotFoundException;
import com.gbesh.first.exceptions.UserNotFoundException;
import com.gbesh.first.model.Users;
import com.gbesh.first.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public UserDto getUser(String username, String password) {
        if(StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            throw new ResourceNotFoundException("Username or password is empty");
        }

        Users user = Optional.ofNullable(userRepository.findByUsername(username))
                .orElseThrow(() -> new UserNotFoundException("User does not exists"));

        if (passwordEncoder.matches(password, user.getPassword())){
            return modelMapper.map(user, UserDto.class);
        }

        throw new IncorrectPasswordException("Password is incorrect");
    }

    @Override
    public UserDto saveUser(String username, String email, String password) {
        boolean usernameTaken = userRepository.findByUsername(username) != null;
        if (usernameTaken){
            throw new AlreadyExistsException("Username has already been taken!");
        }
        boolean emailTaken = userRepository.findByEmail(email) != null;
        if (emailTaken){
            throw new AlreadyExistsException("Email has already been taken!");
        }

        if (StringUtils.isBlank(username) || StringUtils.isBlank(email) || StringUtils.isBlank(password)){
            throw new ResourceNotFoundException("Username or Email or Password is missing");
        }

        Users user = new Users(username, email);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        return modelMapper.map(user, UserDto.class);
    }
}
