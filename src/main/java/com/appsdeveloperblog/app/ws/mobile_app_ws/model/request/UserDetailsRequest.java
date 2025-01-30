package com.appsdeveloperblog.app.ws.mobile_app_ws.model.request;


import lombok.Data;

@Data
public class UserDetailsRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
