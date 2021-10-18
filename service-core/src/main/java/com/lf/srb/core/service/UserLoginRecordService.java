package com.lf.srb.core.service;

import com.lf.srb.core.pojo.entity.UserLoginRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户登录记录表 服务类
 * </p>
 *
 * @author xilou37
 * @since 2021-09-09
 */
public interface UserLoginRecordService extends IService<UserLoginRecord> {

    List<UserLoginRecord> listTop50(Long userId);
}
