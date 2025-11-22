package com.lei.mall.mapper;

import com.lei.mall.model.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author lei
* @description 针对表【user(用户)】的数据库操作Mapper
* @createDate 2025-11-08 14:53:59
* @Entity generator.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}