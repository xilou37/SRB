package com.lf.srb.base.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lf
 * @creat 2021-10-18 16:40
 */
@Data
@ApiModel(description = "短信")
public class SmsDTO {
    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "消息内容")
    private String message;
}
