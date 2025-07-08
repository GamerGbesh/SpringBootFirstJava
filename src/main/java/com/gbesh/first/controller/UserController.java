package com.gbesh.first.controller;


import com.gbesh.first.dto.UserDto;
import com.gbesh.first.exceptions.AlreadyExistsException;
import com.gbesh.first.exceptions.IncorrectPasswordException;
import com.gbesh.first.exceptions.ResourceNotFoundException;
import com.gbesh.first.exceptions.UserNotFoundException;
import com.gbesh.first.request.UserRequest;
import com.gbesh.first.response.ApiResponse;
import com.gbesh.first.service.user.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private IUserService userService;

    @GetMapping("/get")
    public ResponseEntity<ApiResponse> getUser(@RequestBody UserRequest request){
        try {
            UserDto userDto = userService.getUser(request.getUsername(), request.getPassword());
            return ResponseEntity.ok(new ApiResponse("Found", userDto));
        } catch (ResourceNotFoundException | IncorrectPasswordException e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(new ApiResponse(e.getMessage(), null));
        } catch (UserNotFoundException e){
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createUser(@RequestBody UserRequest request){
        try {
            UserDto userDto = userService.saveUser(request.getUsername(), request.getEmail(), request.getPassword());
            return ResponseEntity.ok(new ApiResponse("Created", userDto));
        } catch (AlreadyExistsException | ResourceNotFoundException e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }
}
