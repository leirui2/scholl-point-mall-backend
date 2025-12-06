package com.lei.mall.mapper;

import com.lei.mall.model.entity.SignInRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author lei
* @description 针对表【sign_in_record(签到记录表)】的数据库操作Mapper
* @createDate 2025-12-05 22:56:30
* @Entity com.lei.mall.model.entity.SignInRecord
*/
@Mapper
public interface SignInRecordMapper extends BaseMapper<SignInRecord> {

}




