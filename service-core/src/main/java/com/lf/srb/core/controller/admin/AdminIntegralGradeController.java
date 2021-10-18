package com.lf.srb.core.controller.admin;


import com.lf.common.exception.Assert;
import com.lf.common.result.R;
import com.lf.common.result.ResponseEnum;
import com.lf.srb.core.pojo.entity.IntegralGrade;
import com.lf.srb.core.service.IntegralGradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 积分等级表 前端控制器
 * </p>
 *
 * @author xilou37
 * @since 2021-09-09
 */
@Slf4j
@Api(tags = "积分等级管理")
//@CrossOrigin
@RestController
@RequestMapping("/admin/core/integralGrade")
public class AdminIntegralGradeController {
    @Resource
    private IntegralGradeService integralGradeService;

    @ApiOperation("显示积分等级列表")
    @GetMapping("/list")
    public R listAll(){

        log.info("hi i'm helen");
        log.warn("warning!!!");
        log.error("it's a error");

        List<IntegralGrade> list = integralGradeService.list();
        return R.ok().data("list",list).message("获取列表成功");
    }

    @ApiOperation(value = "根据id删除积分等级记录", notes = "逻辑删除积分等级记录")
    @DeleteMapping("/remove/{id}")
    public R removeById(@ApiParam(value = "数据id",example = "100") @PathVariable Long id){
        boolean result = integralGradeService.removeById(id);
        if (result){
            return R.ok().message("删除成功");
        }else {
            return R.error().message("删除失败");
        }
    }

    @ApiOperation("新增积分等级")
    @PostMapping("/save")
   public R save(
           @ApiParam(value = "积分等级对象",required = true)
           @RequestBody IntegralGrade integralGrade){

//        if (integralGrade.getBorrowAmount() == null){
//            throw new BusinessException(ResponseEnum.BORROW_AMOUNT_NULL_ERROR);
//        }
        Assert.notNull(integralGrade.getBorrowAmount(), ResponseEnum.BORROW_AMOUNT_NULL_ERROR);


       boolean result = integralGradeService.save(integralGrade);
       if (result){
           return R.ok().message("保存成功");
       }else {
           return R.error().message("保存失败");
       }
   }

   @ApiOperation("根据id查询积分等级")
   @GetMapping("/get/{id}")
   public R getById(
           @ApiParam(value = "数据id",required = true,example = "1")
           @PathVariable Long id){
       IntegralGrade integralGrade = integralGradeService.getById(id);
       if (integralGrade != null){
           return R.ok().data("record",integralGrade).message("查询成功");
       }else {
           return R.error().message("查询失败");
       }
   }

    @ApiOperation("修改积分等级")
    @PutMapping ("/update")
    public R updateById(
            @ApiParam(value = "积分等级对象",required = true)
            @RequestBody IntegralGrade integralGrade){
        boolean result = integralGradeService.updateById(integralGrade);
        if (result){
            return R.ok().message("更新成功");
        }else {
            return R.error().message("更新失败");
        }
    }


}

