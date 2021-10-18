package com.lf.srb.core.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lf.srb.core.listener.ExcelDictDTOListener;
import com.lf.srb.core.mapper.DictMapper;
import com.lf.srb.core.pojo.dto.ExcelDictDTO;
import com.lf.srb.core.pojo.entity.Dict;
import com.lf.srb.core.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 数据字典 服务实现类
 * </p>
 *
 * @author xilou37
 * @since 2021-09-09
 */
@Slf4j
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    @Resource
    private RedisTemplate redisTemplate;

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void importData(InputStream inputStream) {
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(inputStream, ExcelDictDTO.class, new ExcelDictDTOListener(baseMapper)).sheet().doRead();
        log.info("importData finished");
    }

    @Override
    public List<ExcelDictDTO> listDictData() {
        List<Dict> dicts = baseMapper.selectList(null);
        ArrayList<ExcelDictDTO> excelDictDTOList = new ArrayList<>(dicts.size());
         dicts.forEach(dict -> {
             ExcelDictDTO excelDictDTO = new ExcelDictDTO();
             BeanUtils.copyProperties(dict,excelDictDTO);
             excelDictDTOList.add(excelDictDTO);
         });
        return excelDictDTOList;
    }

    @Override
    public List<Dict> listByParentId(Long parentId) {
        //首先查询redis中是否存在数据列表
        try {
            List<Dict> dictList = (List<Dict>) redisTemplate.opsForValue().get("srb:core:dictList" + parentId);
            //如果存在直接从redis中返回数据列表
            if (dictList != null){
                log.info("从redis中获取数据列表");
                return dictList;
            }
        } catch (Exception e) {
            log.error("redis服务器异常："+ ExceptionUtils.getStackTrace(e));
        }
        //如果不存在则查询数据库
        log.info("从数据库中获取数据列表");
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("parent_id",parentId);
        List<Dict> dictList = baseMapper.selectList(dictQueryWrapper);
        //填充hasChildren字段
        dictList.forEach(dict -> {
            //判断当前节点是否有子节点
            boolean hasChildren = this.hasChildren(dict.getId());
            dict.setHasChildren(hasChildren);
        });
        try {
            //将数据存入redis
            log.info("将数据存入redis");
            redisTemplate.opsForValue().set("srb:core:dictList" + parentId,dictList,5, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("redis服务器异常："+ ExceptionUtils.getStackTrace(e));
        }

        //返回数据列表
        return dictList;
    }

    @Override
    public List<Dict> findByDictCode(String dictCode) {
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("dict_code",dictCode);
        Dict dict = baseMapper.selectOne(dictQueryWrapper);
        return this.listByParentId(dict.getId());

    }

    @Override
    public String getNameByParentDictCodeAndValue(String dictCode, Integer value) {

        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("dict_code",dictCode);
        Dict parentDict = baseMapper.selectOne(dictQueryWrapper);
        if (parentDict == null){
            return "";
        }
        dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper
                .eq("parent_id",parentDict.getId())
                .eq("value",value);
        Dict dict = baseMapper.selectOne(dictQueryWrapper);
        if (dict == null){
            return "";
        }
        return dict.getName();

    }

    private boolean hasChildren(Long id){
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("parent_id",id);
        Integer count = baseMapper.selectCount(dictQueryWrapper);
        if (count.intValue() > 0){
            return true;
        }else{
            return false;
        }

    }
}
