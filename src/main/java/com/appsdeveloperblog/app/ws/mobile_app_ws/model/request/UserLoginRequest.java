package com.appsdeveloperblog.app.ws.mobile_app_ws.model.request;

import lombok.Data;

@Data
public class UserLoginRequest {

    private String email;
    private String password;
}
