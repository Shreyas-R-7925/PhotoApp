package com.appsdeveloperblog.app.ws.mobile_app_ws.controller;

import com.appsdeveloperblog.app.ws.mobile_app_ws.model.response.UserDetailsResponse;
import com.appsdeveloperblog.app.ws.mobile_app_ws.service.impl.UserServiceImpl;
import com.appsdeveloperblog.app.ws.mobile_app_ws.shared.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    UserServiceImpl userService;

    UserDto userDto;

    String USER_ID = "57ejdssjjkadjsjfl2243";
    @BeforeEach
    void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);

        userDto = new UserDto();
        userDto.setFirstName("Shreyas");
        userDto.setLastName("R");
        userDto.setEmail("test@test.com");
        userDto.setUserId(USER_ID);
        userDto.setEncryptedPassword("5udjase2344");
    }

    @Test
    final void testGetUser(){

        when(userService.getUserByUserId(anyString())).thenReturn(userDto);

        UserDetailsResponse userDetailsResponse = userController.getUser(USER_ID);

        assertNotNull(userDetailsResponse);
        assertEquals(USER_ID, userDetailsResponse.getUserId());
        assertEquals(userDto.getFirstName(), userDetailsResponse.getFirstName());

    }
}
