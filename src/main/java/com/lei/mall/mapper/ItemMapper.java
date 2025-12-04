package com.lei.mall.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lei.mall.model.entity.Item;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lei.mall.model.request.ItemQueryRequest;
import com.lei.mall.model.vo.ItemCategoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
* @author lei
* @description 针对表【item(商品信息表)】的数据库操作Mapper
* @createDate 2025-11-22 17:58:06
* @Entity com.lei.mall.model.entity.Item
*/
@Mapper
public interface ItemMapper extends BaseMapper<Item> {

    /**
     * 根据ID查询商品信息
     * @param id 商品信息ID
     * @return 商品信息
     */
    Item selectById(Long id);

    /**
     * 管理员根据ID查询商品信息完整信息
     * @param id 商品信息ID
     * @return 商品信息完整信息VO
     */
    ItemCategoryVO selectVoById(String id);

    /**
     * 分页联表查询商品和类别信息
     * @param page 分页参数
     * @param itemQueryRequest 查询条件
     * @return 商品和类别信息列表
     */
    IPage<ItemCategoryVO> selectItemCategoryVoByPage(IPage<ItemCategoryVO> page,  @Param("query") ItemQueryRequest itemQueryRequest);
}