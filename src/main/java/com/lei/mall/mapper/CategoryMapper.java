package com.lei.mall.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lei.mall.model.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lei.mall.model.request.HotCategoryQueryRequest;
import com.lei.mall.model.vo.CategoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @author lei
* @description 针对表【category(商品类别表)】的数据库操作Mapper
* @createDate 2025-11-22 17:58:10
* @Entity com.lei.mall.model.entity.Category
*/
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 热门类别分页查询
     * @param page 分页参数
     * @param hotCategoryQueryRequest 查询条件
     * @return 热门类别列表
     */
    IPage<CategoryVO> selectHotCategoryByPage(IPage<CategoryVO> page, @Param("query") HotCategoryQueryRequest hotCategoryQueryRequest);

}