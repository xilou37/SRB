package com.lf.srb.core.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 积分等级表 前端控制器
 * </p>
 *
 * @author xilou37
 * @since 2021-09-09
 */
@Api(tags = "网站积分等级接口")
@RestController
@RequestMapping("/api/core/integralGrade")
public class IntegralGradeController {
    @ApiOperation("测试")
    @GetMapping("/test")
    public void test(){
        return;
    }

}

