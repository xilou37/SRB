package com.lf.srb.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lf.srb.core.pojo.entity.UserLoginRecord;
import com.lf.srb.core.mapper.UserLoginRecordMapper;
import com.lf.srb.core.service.UserLoginRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户登录记录表 服务实现类
 * </p>
 *
 * @author xilou37
 * @since 2021-09-09
 */
@Service
public class UserLoginRecordServiceImpl extends ServiceImpl<UserLoginRecordMapper, UserLoginRecord> implements UserLoginRecordService {

    @Override
    public List<UserLoginRecord> listTop50(Long userId) {
        QueryWrapper<UserLoginRecord> userLoginRecordQueryWrapper = new QueryWrapper<>();
        userLoginRecordQueryWrapper
                .eq("user_id",userId)
                .orderByDesc("id")
                .last("limit 50");
        List<UserLoginRecord> userLoginRecordList = baseMapper.selectList(userLoginRecordQueryWrapper);
        return  userLoginRecordList;

    }
}
