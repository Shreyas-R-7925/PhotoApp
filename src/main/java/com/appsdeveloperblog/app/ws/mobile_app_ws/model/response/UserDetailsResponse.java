package com.appsdeveloperblog.app.ws.mobile_app_ws.model.response;

import lombok.Data;

@Data
public class UserDetailsResponse {

    private String userId;
    private String firstName;
    private String lastName;
    private String email;
}
