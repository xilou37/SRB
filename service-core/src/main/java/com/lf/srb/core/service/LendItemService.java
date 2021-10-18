package com.lf.srb.core.service;

import com.lf.srb.core.pojo.entity.LendItem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lf.srb.core.pojo.vo.InvestVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的出借记录表 服务类
 * </p>
 *
 * @author xilou37
 * @since 2021-09-09
 */
public interface LendItemService extends IService<LendItem> {

    String commitInvest(InvestVO investVo);

    void notify(Map<String, Object> paramMap);

    List<LendItem> selectByLendId(Long lendId, Integer status);

    List<LendItem> selectByLendId(Long lendId);
}
