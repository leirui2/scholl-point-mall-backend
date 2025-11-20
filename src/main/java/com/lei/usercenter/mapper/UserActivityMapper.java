package com.lei.usercenter.mapper;

import com.lei.usercenter.model.entity.UserActivity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author lei
* @description 针对表【user_activity(用户活跃度表)】的数据库操作Mapper
* @createDate 2025-11-09 14:27:40
* @Entity com.lei.usercenter.model.entity.UserActivity
*/
@Mapper
public interface UserActivityMapper extends BaseMapper<UserActivity> {

}