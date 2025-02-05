package com.appsdeveloperblog.app.ws.mobile_app_ws.utils;

import com.appsdeveloperblog.app.ws.mobile_app_ws.shared.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UtilsTest {

    @Autowired
    Utils utils;

    @BeforeEach
    void setUp() throws Exception{

    }

    @Test
    final void testGenerateUserId(){
        String userId = utils.generateUserId(30);
        String userId2 = utils.generateUserId(30);

        assertNotNull(userId);
        assertNotNull(userId2);

        assertTrue(userId.length() == 30);
        assertTrue(userId2.length() == 30);

        assertFalse(userId.equalsIgnoreCase(userId2));
    }

}
