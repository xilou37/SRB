package com.lf.srb.sms;

import com.lf.srb.sms.util.SmsProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author lf
 * @creat 2021-09-16 16:01
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UtilsTest {
    @Test
    public void testProperties(){
        System.out.println(SmsProperties.REGION_Id);
        System.out.println(SmsProperties.KEY_ID);
        System.out.println(SmsProperties.KEY_SECRET);
        System.out.println(SmsProperties.TEMPLATE_CODE);
        System.out.println(SmsProperties.SIGN_NAME);
    }


}
