package com.appsdeveloperblog.app.ws.mobile_app_ws.security;

import com.appsdeveloperblog.app.ws.mobile_app_ws.SpringApplicationContext;
import org.springframework.core.env.Environment;

public class SecurityConstants {

        public static final long EXPIRATION_TIME=864000000; // 10 days
        public static final String TOKEN_PREFIX="Bearer ";
        public static final String HEADER_STRING="Authorization";
        public static final String SIGN_UP_URL="/users/signup";
        public static final String TOKEN_SECRET="jf9i4jgu83nfl0jf9i4jgu83nfl0jf9i4jgu83nfl0jf9i4jgu83nfl0jf9i4jgu83nfl0jf9i4jgu83nfl0";
        public static final String H2_CONSOLE = "/h2-console/**";

        public static String getTokenSecret(){
                Environment environment = (Environment) SpringApplicationContext.getBean("environment");
                return environment.getProperty("tokenSecret");
        }
}
