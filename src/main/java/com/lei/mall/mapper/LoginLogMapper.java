package com.lei.mall.mapper;

import com.lei.mall.model.entity.LoginLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author lei
* @description 针对表【login_log(登录日志表)】的数据库操作Mapper
* @createDate 2025-11-09 14:27:40
* @Entity com.lei.mall.model.entity.LoginLog
*/
@Mapper
public interface LoginLogMapper extends BaseMapper<LoginLog> {

}