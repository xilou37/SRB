package com.lf.srb.core.mapper;

import com.lf.srb.core.pojo.dto.ExcelDictDTO;
import com.lf.srb.core.pojo.entity.Dict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 数据字典 Mapper 接口
 * </p>
 *
 * @author xilou37
 * @since 2021-09-09
 */
public interface DictMapper extends BaseMapper<Dict> {

    void insertBatch(List<ExcelDictDTO> list);
}
