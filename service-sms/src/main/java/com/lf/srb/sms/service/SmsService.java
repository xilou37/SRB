package com.lf.srb.sms.service;

import java.util.Map;

/**
 * @author lf
 * @creat 2021-09-16 17:06
 */
public interface SmsService {
    void send(String mobile, String templateCode, Map<String,Object> param);
}
