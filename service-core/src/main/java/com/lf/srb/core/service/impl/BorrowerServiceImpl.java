package com.lf.srb.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lf.srb.core.enums.BorrowerStatusEnum;
import com.lf.srb.core.enums.IntegralEnum;
import com.lf.srb.core.mapper.BorrowerAttachMapper;
import com.lf.srb.core.mapper.BorrowerMapper;
import com.lf.srb.core.mapper.UserInfoMapper;
import com.lf.srb.core.mapper.UserIntegralMapper;
import com.lf.srb.core.pojo.entity.Borrower;
import com.lf.srb.core.pojo.entity.BorrowerAttach;
import com.lf.srb.core.pojo.entity.UserInfo;
import com.lf.srb.core.pojo.entity.UserIntegral;
import com.lf.srb.core.pojo.vo.BorrowerApprovalVO;
import com.lf.srb.core.pojo.vo.BorrowerAttachVO;
import com.lf.srb.core.pojo.vo.BorrowerDetailVO;
import com.lf.srb.core.pojo.vo.BorrowerVO;
import com.lf.srb.core.service.BorrowerAttachService;
import com.lf.srb.core.service.BorrowerService;
import com.lf.srb.core.service.DictService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 借款人 服务实现类
 * </p>
 *
 * @author xilou37
 * @since 2021-09-09
 */
@Service
public class BorrowerServiceImpl extends ServiceImpl<BorrowerMapper, Borrower> implements BorrowerService {
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private BorrowerAttachMapper borrowerAttachMapper;
    @Resource
    private DictService dictService;
    @Resource
    private BorrowerAttachService borrowerAttachService;
    @Resource
    private UserIntegralMapper userIntegralMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveBorrowerVOByUserId(BorrowerVO borrowerVO, Long userId) {
        //获取用户基本信息
        UserInfo userInfo = userInfoMapper.selectById(userId);
        //保存借款人信息
        Borrower borrower = new Borrower();
        BeanUtils.copyProperties(borrowerVO,borrower);
        borrower.setUserId(userId);
        borrower.setName(userInfo.getName());
        borrower.setIdCard(userInfo.getIdCard());
        borrower.setMobile(userInfo.getMobile());
        borrower.setStatus(BorrowerStatusEnum.AUTH_RUN.getStatus());//  认证中
        baseMapper.insert(borrower);
        //保存附件
        List<BorrowerAttach> borrowerAttachList = borrowerVO.getBorrowerAttachList();
        borrowerAttachList.forEach(borrowerAttach -> {
            borrowerAttach.setBorrowerId(borrower.getId());
            borrowerAttachMapper.insert(borrowerAttach);
        });
        //跟新userinfo中的认证状态
        userInfo.setBorrowAuthStatus(BorrowerStatusEnum.AUTH_RUN.getStatus());
        userInfoMapper.updateById(userInfo);

    }

    @Override
    public Integer getBorrowerStatus(Long userId) {

        QueryWrapper<Borrower> borrowerQueryWrapper = new QueryWrapper<>();
        borrowerQueryWrapper.select("status").eq("user_id",userId);
        List<Object> objects = baseMapper.selectObjs(borrowerQueryWrapper);
        if (objects.size() == 0 ){
            //借款人尚未提交信息
            return BorrowerStatusEnum.NO_AUTH.getStatus();
        }
       Integer status = (Integer) objects.get(0);
        return status;
    }

    @Override
    public IPage<Borrower> listPage(Page<Borrower> pageParam, String keyword) {

        if (StringUtils.isBlank(keyword)){
            return baseMapper.selectPage(pageParam,null);
        }
        QueryWrapper<Borrower> borrowerQueryWrapper = new QueryWrapper<>();
        borrowerQueryWrapper
                .like("name",keyword)
                .or().like("id_card",keyword)
                .or().like("mobile",keyword)
                .orderByDesc("id");
        return baseMapper.selectPage(pageParam,borrowerQueryWrapper);
    }

    @Override
    public BorrowerDetailVO getBorrowerDetailVOById(Long id) {

        //获取借款人信息
        Borrower borrower = baseMapper.selectById(id);
        //填充基本借款人信息
        BorrowerDetailVO borrowerDetailVO = new BorrowerDetailVO();
        BeanUtils.copyProperties(borrower,borrowerDetailVO);
        //婚否
        borrowerDetailVO.setMarry(borrower.getMarry()?"是":"否");
        //性别
        borrowerDetailVO.setSex(borrower.getSex() == 1 ?"男":"女");
        //下拉列表
        borrowerDetailVO.setEducation(dictService.getNameByParentDictCodeAndValue("education",borrower.getEducation()));
        borrowerDetailVO.setIndustry(dictService.getNameByParentDictCodeAndValue("industry",borrower.getIndustry()));
        borrowerDetailVO.setIncome(dictService.getNameByParentDictCodeAndValue("income",borrower.getIncome()));
        borrowerDetailVO.setReturnSource(dictService.getNameByParentDictCodeAndValue("returnSource",borrower.getReturnSource()));
        borrowerDetailVO.setContactsRelation(dictService.getNameByParentDictCodeAndValue("relation",borrower.getContactsRelation()));
        //审批状态
        String status = BorrowerStatusEnum.getMsgByStatus(borrower.getStatus());
        borrowerDetailVO.setStatus(status);

        //附件列表
        List<BorrowerAttachVO> borrowerAttachVOList = borrowerAttachService.selectBorrowerAttachVOList(id);
        borrowerDetailVO.setBorrowerAttachVOList(borrowerAttachVOList);


        return borrowerDetailVO;

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void approval(BorrowerApprovalVO borrowerApprovalVO) {
        //借款人认证状态
        Long borrowerId = borrowerApprovalVO.getBorrowerId();
        Borrower borrower = baseMapper.selectById(borrowerId);
        borrower.setStatus(borrowerApprovalVO.getStatus());
        baseMapper.updateById(borrower);

        Long userId = borrower.getUserId();
        UserInfo userInfo = userInfoMapper.selectById(userId);

        //添加积分
        UserIntegral userIntegral = new UserIntegral();
        userIntegral.setUserId(userId);
        userIntegral.setIntegral(borrowerApprovalVO.getInfoIntegral());
        userIntegral.setContent("借款人基本信息");
        userIntegralMapper.insert(userIntegral);

        int curIntegral = userInfo.getIntegral() + borrowerApprovalVO.getInfoIntegral();
        if (borrowerApprovalVO.getIsIdCardOk()){
            curIntegral += IntegralEnum.BORROWER_IDCARD.getIntegral();
            userIntegral = new UserIntegral();
            userIntegral.setUserId(userId);
            userIntegral.setIntegral(IntegralEnum.BORROWER_IDCARD.getIntegral());
            userIntegral.setContent(IntegralEnum.BORROWER_IDCARD.getMsg());
            userIntegralMapper.insert(userIntegral);
        }
        if (borrowerApprovalVO.getIsCarOk()){
            curIntegral += IntegralEnum.BORROWER_CAR.getIntegral();
            userIntegral = new UserIntegral();
            userIntegral.setUserId(userId);
            userIntegral.setIntegral(IntegralEnum.BORROWER_CAR.getIntegral());
            userIntegral.setContent(IntegralEnum.BORROWER_CAR.getMsg());
            userIntegralMapper.insert(userIntegral);
        }
        if (borrowerApprovalVO.getIsHouseOk()){
            curIntegral += IntegralEnum.BORROWER_HOUSE.getIntegral();
            userIntegral = new UserIntegral();
            userIntegral.setUserId(userId);
            userIntegral.setIntegral(IntegralEnum.BORROWER_HOUSE.getIntegral());
            userIntegral.setContent(IntegralEnum.BORROWER_HOUSE.getMsg());
            userIntegralMapper.insert(userIntegral);
        }
        userInfo.setIntegral(curIntegral);
        //修改审核状态
        userInfo.setBorrowAuthStatus(borrowerApprovalVO.getStatus());
        userInfoMapper.updateById(userInfo);
    }
}
