package com.lf.srb.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lf.srb.core.pojo.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lf.srb.core.pojo.query.UserInfoQuery;
import com.lf.srb.core.pojo.vo.LoginVO;
import com.lf.srb.core.pojo.vo.RegisterVO;
import com.lf.srb.core.pojo.vo.UserIndexVO;
import com.lf.srb.core.pojo.vo.UserInfoVO;

/**
 * <p>
 * 用户基本信息 服务类
 * </p>
 *
 * @author xilou37
 * @since 2021-09-09
 */
public interface UserInfoService extends IService<UserInfo> {

    void register(RegisterVO registerVO);

    UserInfoVO login(LoginVO loginVO, String ip);

    IPage<UserInfo> listPage(Page<UserInfo> pageParam, UserInfoQuery userInfoQuery);

    void lock(Long id, Integer status);

    boolean checkMobile(String mobile);

    UserIndexVO getIndexUserInfo(Long userId);
}
