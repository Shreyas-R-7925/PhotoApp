package com.appsdeveloperblog.app.ws.mobile_app_ws.service.impl;

import com.appsdeveloperblog.app.ws.mobile_app_ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.mobile_app_ws.repositories.UserRepository;
import com.appsdeveloperblog.app.ws.mobile_app_ws.service.UserService;
import com.appsdeveloperblog.app.ws.mobile_app_ws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.mobile_app_ws.shared.utils.Utils;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    Utils utils;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto createUser(UserDto user) {

        if(userRepository.findByEmail(user.getEmail()) != null){
            throw new RuntimeException("Record already exists!");
        }

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);

        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        String publicUserId = utils.generateUserId(30);
        userEntity.setUserId(publicUserId);

        UserEntity storedUserDetails = userRepository.save(userEntity);

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(storedUserDetails, returnValue);

        return returnValue;
    }

    @Override
    public UserDto getUser(String email) {

        UserEntity userEntity = userRepository.findByEmail(email);

        if(userEntity == null)
            throw new UsernameNotFoundException(email);

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(userEntity, returnValue);

        return returnValue;
    }

    @Override
    public UserDto getUserByUserId(String id) {

        UserEntity userEntity = userRepository.findByUserId(id);

        if(userEntity == null){
            throw new UsernameNotFoundException(id);
        }

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userEntity, userDto);

        return userDto;
    }

    @Override
    public UserDto updateUser(String userId, UserDto userDto) {

        UserEntity userEntity = userRepository.findByUserId(userId);

        if(userEntity == null){
            throw new UsernameNotFoundException(userId);
        }

        if(userDto.getEmail() != null){
            userEntity.setEmail(userDto.getEmail());
        }

        if(userDto.getFirstName() != null){
            userEntity.setFirstName(userDto.getFirstName());
        }

        if(userDto.getLastName() != null){
            userEntity.setLastName(userDto.getLastName());
        }

        UserEntity storedUserDetails = userRepository.save(userEntity);

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(storedUserDetails, returnValue);

        return returnValue;
    }

    @Transactional
    @Override
    public void deleteUser(String id) {

        UserEntity userEntity = userRepository.findByUserId(id);

        if(userEntity == null){
            throw new UsernameNotFoundException(id);
        }

        userRepository.deleteByUserId(id);
    }

    @Override
    public List<UserDto> getUsers(int page, int limit) {

        List<UserDto> returnValue = new ArrayList<>();

        if(page > 0)
            page -= 1;

        Pageable pageableRequest =  PageRequest.of(page, limit);

        Page<UserEntity> userPage = userRepository.findAll(pageableRequest);
        List<UserEntity> users = userPage.getContent();

        for(UserEntity userEntity : users){
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(userEntity, userDto);
            returnValue.add(userDto);
        }

        return returnValue;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findByEmail(username);

        if(userEntity == null){
            throw new UsernameNotFoundException(username);
        }

        return new User(username, userEntity.getEncryptedPassword(), new ArrayList<>()); // third parameter is a list of granted authorizations
    }
}
