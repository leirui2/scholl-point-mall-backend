package com.lei.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lei.mall.common.ApiResponse;
import com.lei.mall.common.ErrorCode;
import com.lei.mall.common.ResultUtils;
import com.lei.mall.exception.BusinessException;
import com.lei.mall.mapper.PointTransactionMapper;
import com.lei.mall.mapper.SignInRuleMapper;
import com.lei.mall.model.entity.*;
import com.lei.mall.model.vo.UserLoginVO;
import com.lei.mall.service.PointTransactionService;
import com.lei.mall.service.SignInRecordService;
import com.lei.mall.mapper.SignInRecordMapper;
import com.lei.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
* @author lei
* @description 针对表【sign_in_record(签到记录表)】的数据库操作Service实现
* @createDate 2025-12-05 22:56:30
*/
@Service
public class SignInRecordServiceImpl extends ServiceImpl<SignInRecordMapper, SignInRecord>
    implements SignInRecordService{
    @Resource
    private SignInRecordMapper signInRecordMapper;

    @Resource
    private UserService userService;

    @Resource
    private SignInRuleMapper signInRuleMapper;

    @Resource
    private PointTransactionService pointTransactionService;

    /**
     * 签到
     * @return 签到获得的积分点数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse<Integer> signIn(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "请求参数错误");
        }
        UserLoginVO loginUser = userService.getLoginUser(request);
        if(loginUser == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR.getCode(), "未登录");
        }
        
        try {
            //签到记录对象
            SignInRecord signInRecord = new SignInRecord();
            signInRecord.setUserId(loginUser.getId());
            signInRecord.setSignInDate(new Date());

            //判断昨天是否签到，修改user对象
            User userToUpdate = new User();
            
            // 从数据库获取最新的用户信息而不是使用缓存中的信息
            User currentUser = userService.getById(loginUser.getId());
            
            Long cnt = getSignInDaysNum(loginUser.getId());
            if(cnt == 0){
                //没有签到过，直接连续签到天数设为1
                userToUpdate.setConsecutiveSignInDays(1);
            }else{
                // 使用从数据库获取的最新连续签到天数
                userToUpdate.setConsecutiveSignInDays(currentUser.getConsecutiveSignInDays()+1);
            }

            //根据连续签到天数，判断应该得到多少积分
            QueryWrapper<SignInRule> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("consecutiveDays",userToUpdate.getConsecutiveSignInDays());
            queryWrapper.eq("status",1);
            queryWrapper.select("points");
            SignInRule signInRule = signInRuleMapper.selectOne(queryWrapper);
            //设置签到记录对象积分
            signInRecord.setPoints(signInRule.getPoints());
            signInRecord.setConsecutiveDays(userToUpdate.getConsecutiveSignInDays());
            boolean saveResult = this.save(signInRecord);
            
            if (!saveResult) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR.getCode(), "签到记录保存失败");
            }

            // 直接更新用户的连续签到天数和积分，
            UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
            userUpdateWrapper.eq("id", loginUser.getId())
                    .set("consecutiveSignInDays", userToUpdate.getConsecutiveSignInDays())
                    .set("points", currentUser.getPoints() + signInRecord.getPoints());
            boolean updateResult = userService.update(null, userUpdateWrapper);
            
            if (!updateResult) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR.getCode(), "用户信息更新失败");
            }

            //积分流水记录表
            PointTransaction pointTransaction = new PointTransaction();
            pointTransaction.setPoints(signInRecord.getPoints());
            pointTransaction.setType(1);
            pointTransaction.setUserId(loginUser.getId());
            //获取签到记录表对象的ID
            pointTransaction.setBusinessId(signInRecord.getId());
            String msg =  loginUser.getId() + "签到成功，连续 " + userToUpdate.getConsecutiveSignInDays() + "天签到，获得了 "+signInRecord.getPoints() + "积分。" ;
            pointTransaction.setDescription(msg);
            boolean res = pointTransactionService.save(pointTransaction);
            if (!res) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR.getCode(), "积分流水记录表保存失败");
            }

            return ResultUtils.success(signInRule.getPoints());
        } catch (Exception e) {
            // 记录异常日志
            log.error("签到过程中发生异常：", e);
            // 重新抛出异常以便触发事务回滚
            throw e;
        }
    }

    /**
     * 根据用户ID获取签到天数
     * @param id 用户ID
     * @return 签到天数
     */
    private Long getSignInDaysNum(Long id) {
        // 获取今天的日期和昨天的日期
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        
        // 使用统一的日期格式化器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String todayStr = today.format(formatter);
        String yesterdayStr = yesterday.format(formatter);
        
        // 判断今天是否已经签到过
        QueryWrapper<SignInRecord> todayQueryWrapper = new QueryWrapper<>();
        todayQueryWrapper.eq("userId", id);
        todayQueryWrapper.gt("createTime", todayStr);
        long todayCnt = signInRecordMapper.selectCount(todayQueryWrapper);
        
        if (todayCnt > 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR.getCode(), "今天已经签到过啦！");
        }
        
        // 判断昨天是否签到
        QueryWrapper<SignInRecord> yesterdayQueryWrapper = new QueryWrapper<>();
        yesterdayQueryWrapper.eq("userId", id);
        yesterdayQueryWrapper.gt("createTime", yesterdayStr);
        
        // 如果有昨天的数据，就表示昨天已经签到，否则签到天数重置为1
        return signInRecordMapper.selectCount(yesterdayQueryWrapper);
    }
}




