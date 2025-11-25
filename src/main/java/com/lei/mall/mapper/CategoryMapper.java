package com.lei.mall.mapper;

import com.lei.mall.model.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author lei
* @description 针对表【category(商品类别表)】的数据库操作Mapper
* @createDate 2025-11-22 17:58:10
* @Entity com.lei.mall.model.entity.Category
*/
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}




