package com.lei.mall.mapper;

import com.lei.mall.model.entity.Item;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author lei
* @description 针对表【item(商品信息表)】的数据库操作Mapper
* @createDate 2025-11-22 17:58:06
* @Entity com.lei.mall.model.entity.Item
*/
@Mapper
public interface ItemMapper extends BaseMapper<Item> {

}




