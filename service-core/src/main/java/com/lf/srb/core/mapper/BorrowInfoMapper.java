package com.lf.srb.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lf.srb.core.pojo.entity.BorrowInfo;

import java.util.List;

/**
 * <p>
 * 借款信息表 Mapper 接口
 * </p>
 *
 * @author xilou37
 * @since 2021-09-09
 */
public interface BorrowInfoMapper extends BaseMapper<BorrowInfo> {
    List<BorrowInfo> selectBorrowInfoList();
}
