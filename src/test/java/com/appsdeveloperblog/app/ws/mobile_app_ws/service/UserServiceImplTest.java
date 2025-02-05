package com.appsdeveloperblog.app.ws.mobile_app_ws.service;

import com.appsdeveloperblog.app.ws.mobile_app_ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.mobile_app_ws.repositories.UserRepository;
import com.appsdeveloperblog.app.ws.mobile_app_ws.service.impl.UserServiceImpl;
import com.appsdeveloperblog.app.ws.mobile_app_ws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.mobile_app_ws.shared.utils.Utils;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    Utils utils;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
    }

    @Test
    final void testGetUser(){

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setFirstName("shreyas");
        user.setUserId("hgghghgh");
        user.setEncryptedPassword("gjsajeuejej");
        user.setLastName("R");

        when(userRepository.findByEmail(anyString())).thenReturn(user);

        UserDto userDto = userService.getUser("test@test.com");

        assertNotNull(userDto);
//        assertNull(userDto);
        assertEquals("shreyas", userDto.getFirstName());
    }

    @Test
    final void testGetUserUsernameNotFoundException(){
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        assertThrows(UsernameNotFoundException.class,
                () -> {
                    userService.getUser("test@test.com");
                }
                );
    }

    @Test
    final void testCreateExistingUser(){

        UserEntity existingUser = new UserEntity();
        existingUser.setId(12L);
        existingUser.setFirstName("Harman");
        existingUser.setLastName("Preet");
        existingUser.setEmail("test@test.com");

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(existingUser, userDto);

        when(userRepository.findByEmail(anyString())).thenReturn(existingUser);
        assertThrows(RuntimeException.class, () -> {
            userService.createUser(userDto);
        });
    }

    @Test
    final void testCreateNewUser(){

        UserEntity newUser = new UserEntity();
        newUser.setId(12L);
        newUser.setFirstName("Harman");
        newUser.setLastName("Preet");
        newUser.setEmail("test@test.com");

        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(utils.generateUserId(anyInt())).thenReturn("ajdhdhhgdak234");
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("akdd22842jdadkjafkdhq");

        when(userRepository.save(any(UserEntity.class))).thenReturn(newUser);

        UserDto userDto = new UserDto();
        UserDto storedDetails = userService.createUser(userDto);

        assertNotNull(storedDetails);
        assertEquals(newUser.getFirstName(), storedDetails.getFirstName());
    }
}
