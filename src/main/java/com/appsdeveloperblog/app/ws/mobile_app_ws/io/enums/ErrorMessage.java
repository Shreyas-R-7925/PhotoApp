package com.appsdeveloperblog.app.ws.mobile_app_ws.io.enums;

import lombok.*;

import java.util.Date;

@Getter
@AllArgsConstructor
public class ErrorMessage {

    private Date timestamp;
    private String message;
}
