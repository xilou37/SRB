package com.lf.srb.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lf.common.exception.Assert;
import com.lf.common.result.ResponseEnum;
import com.lf.common.util.MD5;
import com.lf.srb.base.util.JwtUtils;
import com.lf.srb.core.mapper.UserAccountMapper;
import com.lf.srb.core.mapper.UserInfoMapper;
import com.lf.srb.core.mapper.UserLoginRecordMapper;
import com.lf.srb.core.pojo.entity.UserAccount;
import com.lf.srb.core.pojo.entity.UserInfo;
import com.lf.srb.core.pojo.entity.UserLoginRecord;
import com.lf.srb.core.pojo.query.UserInfoQuery;
import com.lf.srb.core.pojo.vo.LoginVO;
import com.lf.srb.core.pojo.vo.RegisterVO;
import com.lf.srb.core.pojo.vo.UserIndexVO;
import com.lf.srb.core.pojo.vo.UserInfoVO;
import com.lf.srb.core.service.UserInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * <p>
 * 用户基本信息 服务实现类
 * </p>
 *
 * @author xilou37
 * @since 2021-09-09
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    @Resource
    private UserAccountMapper userAccountMapper;
    @Resource
    private UserLoginRecordMapper userLoginRecordMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(RegisterVO registerVO) {
        //判断用户是否已被注册
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("mobile",registerVO.getMobile());
        Integer count = baseMapper.selectCount(userInfoQueryWrapper);
        Assert.isTrue(count == 0, ResponseEnum.MOBILE_EXIST_ERROR);

        //插入用户信息记录：user_info
        UserInfo userInfo = new UserInfo();
        userInfo.setUserType(registerVO.getUserType());
        userInfo.setNickName(registerVO.getMobile());
        userInfo.setName(registerVO.getMobile());
        userInfo.setMobile(registerVO.getMobile());
        userInfo.setPassword(MD5.encrypt(registerVO.getPassword()));
        userInfo.setStatus(UserInfo.STATUS_NORMAL);
        userInfo.setHeadImg(UserInfo.USER_AVATAR);
        baseMapper.insert(userInfo);
        //插入用户记录：user_account
        UserAccount userAccount = new UserAccount();
        userAccount.setUserId(userInfo.getId());
        userAccountMapper.insert(userAccount);

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserInfoVO login(LoginVO loginVO, String ip) {


        String mobile = loginVO.getMobile();
        String password = loginVO.getPassword();
        Integer userType = loginVO.getUserType();
        //用户是否存在
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper
                .eq("mobile",mobile)
                .eq("user_type",userType);
        UserInfo userInfo = baseMapper.selectOne(userInfoQueryWrapper);
        Assert.notNull(userInfo,ResponseEnum.LOGIN_MOBILE_ERROR);
        //密码是否正确
        Assert.equals(MD5.encrypt(password),userInfo.getPassword(),ResponseEnum.LOGIN_PASSWORD_ERROR);

        //用户是否被禁用
        Assert.equals(userInfo.getStatus(),userInfo.STATUS_NORMAL,ResponseEnum.LOGIN_LOKED_ERROR);
        //记录登入日志
        UserLoginRecord userLoginRecord = new UserLoginRecord();
        userLoginRecord.setUserId(userInfo.getId());
        userLoginRecord.setIp(ip);
        userLoginRecordMapper.insert(userLoginRecord);

        //生成token
        String token = JwtUtils.createToken(userInfo.getId(), userInfo.getName());

        //组装userInfoVO
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setToken(token);
        userInfoVO.setMobile(mobile);
        userInfoVO.setUserType(userType);
        userInfoVO.setHeadImg(userInfo.getHeadImg());
        userInfoVO.setName(userInfo.getName());
        userInfoVO.setNickName(userInfo.getNickName());
        //返回
        return userInfoVO;
    }

    @Override
    public IPage<UserInfo> listPage(Page<UserInfo> pageParam, UserInfoQuery userInfoQuery) {
        if (userInfoQuery == null){
            return baseMapper.selectPage(pageParam,null);
        }
        String mobile = userInfoQuery.getMobile();
        Integer status = userInfoQuery.getStatus();
        Integer userType = userInfoQuery.getUserType();
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper
                .eq(StringUtils.isNotBlank(mobile),"mobile",mobile)
                .eq(status != null ,"status",status)
                .eq(userType != null,"user_type",userType);
        return baseMapper.selectPage(pageParam,userInfoQueryWrapper);

    }

    @Override
    public void lock(Long id, Integer status) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        userInfo.setStatus(status);
        baseMapper.updateById(userInfo);
    }

    @Override
    public boolean checkMobile(String mobile) {
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(userInfoQueryWrapper);
        return count > 0 ;

    }

    @Override
    public UserIndexVO getIndexUserInfo(Long userId) {

        //用户信息
        UserInfo userInfo = baseMapper.selectById(userId);

        //账户信息
        QueryWrapper<UserAccount> userAccountQueryWrapper = new QueryWrapper<>();
        userAccountQueryWrapper.eq("user_id", userId);
        UserAccount userAccount = userAccountMapper.selectOne(userAccountQueryWrapper);

        //登录信息
        QueryWrapper<UserLoginRecord> userLoginRecordQueryWrapper = new QueryWrapper<>();
        userLoginRecordQueryWrapper
                .eq("user_id", userId)
                .orderByDesc("id")
                .last("limit 1");
        UserLoginRecord userLoginRecord = userLoginRecordMapper.selectOne(userLoginRecordQueryWrapper);

        //组装结果数据
        UserIndexVO userIndexVO = new UserIndexVO();
        userIndexVO.setUserId(userInfo.getId());
        userIndexVO.setUserType(userInfo.getUserType());
        userIndexVO.setName(userInfo.getName());
        userIndexVO.setNickName(userInfo.getNickName());
        userIndexVO.setHeadImg(userInfo.getHeadImg());
        userIndexVO.setBindStatus(userInfo.getBindStatus());
        userIndexVO.setAmount(userAccount.getAmount());
        userIndexVO.setFreezeAmount(userAccount.getFreezeAmount());
        userIndexVO.setLastLoginTime(userLoginRecord.getCreateTime());

        return userIndexVO;
    }
}
