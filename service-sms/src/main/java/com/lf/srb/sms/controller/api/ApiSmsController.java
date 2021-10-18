package com.lf.srb.sms.controller.api;

import com.lf.common.exception.Assert;
import com.lf.common.result.R;
import com.lf.common.result.ResponseEnum;
import com.lf.common.util.RandomUtils;
import com.lf.common.util.RegexValidateUtils;
import com.lf.srb.sms.client.CoreUserInfoClient;
import com.lf.srb.sms.service.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author lf
 * @creat 2021-09-16 17:27
 */
@RestController
@RequestMapping("/api/sms")
@Api(tags = "短信管理")
//@CrossOrigin //跨域
@Slf4j
public class ApiSmsController {
    @Resource
    private SmsService smsService;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private CoreUserInfoClient coreUserInfoClient;

    @ApiOperation("获取验证码")
    @GetMapping("/send/{mobile}")
    public R send(
            @ApiParam(value = "手机号",required = true)
            @PathVariable String mobile){
        //校验手机号码不能为空
        Assert.notNull(mobile, ResponseEnum.CODE_NULL_ERROR);
        //是否是合法的手机号
        Assert.isTrue(RegexValidateUtils.checkCellphone(mobile),ResponseEnum.MOBILE_ERROR);

        //判断手机号是否已注册
        boolean result = coreUserInfoClient.checkMobile(mobile);
        log.info("result" + result);
        Assert.isTrue(result == false,ResponseEnum.MOBILE_EXIST_ERROR);


        String code = RandomUtils.getFourBitRandom();
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", code);
//        smsService.send(mobile, SmsProperties.TEMPLATE_CODE,map);

        //将验证码存入redis
        redisTemplate.opsForValue().set("srb:sms:code"+mobile,code,5, TimeUnit.MINUTES);


        return R.ok().message("验证码发送成功");
    }
}
