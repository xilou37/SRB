package com.lf.srb.sms.client.fallback;

import com.lf.srb.sms.client.CoreUserInfoClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lf
 * @creat 2021-09-21 17:09
 */
@Service
@Slf4j
public class CoreUserInfoClientFallback implements CoreUserInfoClient {
    @Override
    public boolean checkMobile(String mobile) {
        log.error("远程调用失败，服务熔断");
        return false;//手机号不重复
    }
}
