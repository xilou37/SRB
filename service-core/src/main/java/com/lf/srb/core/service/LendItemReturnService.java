package com.lf.srb.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lf.srb.core.pojo.entity.LendItemReturn;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的出借回款记录表 服务类
 * </p>
 *
 * @author xilou37
 * @since 2021-09-09
 */
public interface LendItemReturnService extends IService<LendItemReturn> {

    List<LendItemReturn> selectByLendId(Long lendId, Long userId);


    List<Map<String, Object>> addReturnDetail(Long lendReturnId);

    List<LendItemReturn> selectLendItemReturnList(Long id);
}
