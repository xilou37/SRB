package com.lf.srb.core.controller.api;


import com.lf.common.result.R;
import com.lf.srb.base.util.JwtUtils;
import com.lf.srb.core.pojo.vo.BorrowerVO;
import com.lf.srb.core.service.BorrowerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 借款人 前端控制器
 * </p>
 *
 * @author xilou37
 * @since 2021-09-09
 */
@Api(tags = "借款人")
@RestController
@RequestMapping("/api/core/borrower")
public class BorrowerController {

    @Resource
    private BorrowerService borrowerService;

    @ApiOperation("保存借款人信息")
    @PostMapping("/auth/save")
    public R save(@RequestBody  BorrowerVO borrowerVO, HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        borrowerService.saveBorrowerVOByUserId(borrowerVO,userId);
        return R.ok().message("信息提交成功");

    }

    @ApiOperation("获取借款人的认证状态")
    @GetMapping("/auth/getBorrowerStatus")
    public R getBorrowerStatus(HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        Integer status = borrowerService.getBorrowerStatus(userId);
        return R.ok().data("borrowerStatus", status);
    }
}

