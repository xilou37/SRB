package com.lf.srb.sms.client;

import com.lf.srb.sms.client.fallback.CoreUserInfoClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author lf
 * @creat 2021-09-21 16:30
 */
@FeignClient(value = "service-core",fallback = CoreUserInfoClientFallback.class)
public interface CoreUserInfoClient {
    @GetMapping("/api/core/userInfo/checkMobile/{mobile}")
    boolean checkMobile(@PathVariable String mobile);
}
