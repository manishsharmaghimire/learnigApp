package com.elearn.testserv;

import com.elearn.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void testJwt() {
        System.out.println("Testing JWT");

        String token = jwtUtil.generateToken("manis sharma");
        System.out.println("Generated Token: " + token);

        boolean validateToken = jwtUtil.validateToken(token, "manish sharma");
        System.out.println(validateToken);

        String s = jwtUtil.extractUserName(token);
        System.out.println(s);
    }
}
