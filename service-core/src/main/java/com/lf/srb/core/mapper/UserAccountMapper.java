package com.lf.srb.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lf.srb.core.pojo.entity.UserAccount;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * <p>
 * 用户账户 Mapper 接口
 * </p>
 *
 * @author xilou37
 * @since 2021-09-09
 */
public interface UserAccountMapper extends BaseMapper<UserAccount> {

    void updateAccount(
            @Param("bindCode")String bindCode,
            @Param("amount")BigDecimal amount,
            @Param("freezeAmount")BigDecimal freezeAmount);
}
