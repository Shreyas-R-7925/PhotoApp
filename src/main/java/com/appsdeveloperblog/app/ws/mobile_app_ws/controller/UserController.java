package com.appsdeveloperblog.app.ws.mobile_app_ws.controller;

import com.appsdeveloperblog.app.ws.mobile_app_ws.exceptions.UserServiceException;
import com.appsdeveloperblog.app.ws.mobile_app_ws.io.enums.ErrorMessages;
import com.appsdeveloperblog.app.ws.mobile_app_ws.model.request.UserDetailsRequest;
import com.appsdeveloperblog.app.ws.mobile_app_ws.model.response.OperationalStatus;
import com.appsdeveloperblog.app.ws.mobile_app_ws.model.response.UserDetailsResponse;
import com.appsdeveloperblog.app.ws.mobile_app_ws.service.UserService;
import com.appsdeveloperblog.app.ws.mobile_app_ws.shared.dto.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(value = "{id}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserDetailsResponse getUser(@PathVariable String id){

        UserDetailsResponse returnValue = new UserDetailsResponse();

        UserDto userDto = userService.getUserByUserId(id);
        BeanUtils.copyProperties(userDto, returnValue);

        return returnValue;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserDetailsResponse createUser(@RequestBody UserDetailsRequest userDetails) throws Exception{
        UserDetailsResponse returnValue = new UserDetailsResponse();

        if(userDetails.getFirstName().isEmpty())
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto createdUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createdUser, returnValue);

        return returnValue;
    }

    @PutMapping(path = "{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserDetailsResponse updateUser(@PathVariable String id, @RequestBody UserDetailsRequest userDetailsRequest){

        UserDetailsResponse returnValue = new UserDetailsResponse();

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetailsRequest, userDto);

        UserDto updatedUser = userService.updateUser(id, userDto);
        BeanUtils.copyProperties(updatedUser, returnValue);

        return returnValue;
    }

    @DeleteMapping(path = "{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public OperationalStatus deleteUser(@PathVariable String id){

        OperationalStatus returnValue = new OperationalStatus();

        userService.deleteUser(id);

        returnValue.setOperationName("DELETE");
        returnValue.setOperationResult("SUCCESS");
        return null;
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<UserDetailsResponse> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                              @RequestParam(value = "limit", defaultValue = "25") int limit){

        List<UserDetailsResponse> returnValue = new ArrayList<>();

        List<UserDto> users = userService.getUsers(page, limit);

        for(UserDto userDto : users){
            UserDetailsResponse userResponse = new UserDetailsResponse();
            BeanUtils.copyProperties(userDto, userResponse);

            returnValue.add(userResponse);
        }

        return returnValue;
    }
}
