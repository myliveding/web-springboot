package com.dzr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author dingzr
 * @Description
 * @ClassName UserTest
 * @since 2017/6/29 15:24
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class UserTest {

    @Test
    public void getInfo(){
        System.err.println("11111111");
    }

}
