package com.lf.srb.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lf.srb.core.pojo.entity.BorrowerAttach;
import com.lf.srb.core.pojo.vo.BorrowerAttachVO;

import java.util.List;

/**
 * <p>
 * 借款人上传资源表 服务类
 * </p>
 *
 * @author xilou37
 * @since 2021-09-09
 */
public interface BorrowerAttachService extends IService<BorrowerAttach> {
    List<BorrowerAttachVO> selectBorrowerAttachVOList(Long borrowerId);
}
