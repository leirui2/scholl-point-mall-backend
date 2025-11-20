package com.lei.usercenter.mapper;

import com.lei.usercenter.model.entity.OperationLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author lei
* @description 针对表【operation_log(操作日志表)】的数据库操作Mapper
* @createDate 2025-11-09 14:27:40
* @Entity com.lei.usercenter.model.entity.OperationLog
*/
@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLog> {

}