package com.lf.srb.core.controller.api;


import com.alibaba.fastjson.JSON;
import com.lf.common.result.R;
import com.lf.srb.base.util.JwtUtils;
import com.lf.srb.core.hfb.RequestHelper;
import com.lf.srb.core.pojo.vo.UserBindVO;
import com.lf.srb.core.service.UserBindService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 用户绑定表 前端控制器
 * </p>
 *
 * @author xilou37
 * @since 2021-09-09
 */
@Api(tags = "会员账号绑定")
@RestController
@RequestMapping("/api/core/userBind")
@Slf4j
public class UserBindController {
    @Resource
    private UserBindService userBindService;


    @ApiOperation("账户绑定提交数据")
    @PostMapping("/auth/bind")
    public R bind (@RequestBody UserBindVO userBindVO, HttpServletRequest request){
        //从head中获取token，并对token进行校验 确保用户已经登入 并从token中提取userId
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        //根据用户id做账户绑定 生成一个动态表单的字符串
        String formStr = userBindService.commitBindUser(userBindVO,userId);
        return R.ok().data("formStr",formStr);

    }

    @ApiOperation("账户绑定异步回调")
    @PostMapping("/notify")
    public String notify(HttpServletRequest request){
        Map<String, Object> paramMap = RequestHelper.switchMap(request.getParameterMap());
        log.info("用户账号绑定异步回调："+ JSON.toJSONString(paramMap));

        //校验签名
        if(!RequestHelper.isSignEquals(paramMap)){
            log.error("用户账号绑定异步回调签名错误："+ JSON.toJSONString(paramMap));
            return "fail";
        }
        //修改绑定状态
        userBindService.notify(paramMap);
        return "success";
    }

}

