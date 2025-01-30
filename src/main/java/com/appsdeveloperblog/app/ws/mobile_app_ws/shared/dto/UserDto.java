package com.appsdeveloperblog.app.ws.mobile_app_ws.shared.dto;

import lombok.Data;

import java.io.Serializable;

// Dto -> Data transfer object
@Data
public class UserDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private long id; // value from the database
    private String userId; // this value will be used to get a particular user
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String encryptedPassword;
    private String emailVerificationToken;
    private Boolean emailVerificationStatus = false;
}
