package com.lei.mall.service;

import com.lei.mall.common.PageResult;
import com.lei.mall.model.entity.Item;
import com.lei.mall.model.request.HotItemQueryRequest;
import com.lei.mall.model.request.ItemAddRequest;
import com.lei.mall.model.request.ItemQueryRequest;
import com.lei.mall.model.request.ItemUpdateRequest;
import com.lei.mall.model.vo.ItemCategoryVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lei.mall.model.vo.ItemVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lei
 * @description 针对表【item(商品信息表)】的数据库操作Service
 * @createDate 2025-11-22 17:00:14
 */
public interface ItemService extends IService<Item> {

    /**
     * 添加商品信息
     * @param itemAddRequest 商品信息增加请求参数
     * @return 商品信息ID
     */
    long addItem(ItemAddRequest itemAddRequest, HttpServletRequest request);

    /**
     * 商品信息信息修改
     */
    Item updateItem(ItemUpdateRequest itemUpdateRequest, HttpServletRequest request);

    /**
     * 管理员根据ID查询商品信息完整信息
     * @param id 商品信息ID
     * @param request HTTP请求
     * @return 商品信息完整信息
     */
    ItemCategoryVO getItemById(String id, HttpServletRequest request);

    /**
     * 管理员更新商品信息状态（启用/禁用）
     * @param id 商品信息ID
     * @param status 商品信息状态 0-正常 1-禁用
     * @param request HTTP请求
     * @return 是否更新成功
     */
    boolean updateItemStatus(long id, int status, HttpServletRequest request);

    /**
     * 管理员逻辑删除商品信息
     * @param id 商品信息ID
     * @param request HTTP请求
     * @return 是否删除成功
     */
    boolean deleteItem(long id, HttpServletRequest request);

    /**
     * 管理员分页查询商品信息列表
     * @param itemQueryRequest 查询条件
     * @param request HTTP请求
     * @return 用户列表分页结果
     */
    PageResult<Item> listItemByPage(ItemQueryRequest itemQueryRequest, HttpServletRequest request);

    /**
     * 管理员分页查询商品和类别信息列表
     * @param itemQueryRequest 查询条件
     * @param request HTTP请求
     * @return 商品和类别信息列表分页结果
     */
    PageResult<ItemCategoryVO> listItemCategoryByPage(ItemQueryRequest itemQueryRequest, HttpServletRequest request);

    /**
     * 普通用户分页查询商品信息列表
     * @param itemQueryRequest 查询条件
     * @param request HTTP请求
     * @return 商品信息列表分页结果
     */
    PageResult<ItemVO> listItemByPageUser(ItemQueryRequest itemQueryRequest, HttpServletRequest request);

    /**
     * 获取热门商品列表推荐（前30名）
     * @param hotItemQueryRequest 查询条件
     * @param request HTTP请求
     * @return 商品信息列表分页结果
     */
    PageResult<ItemVO> hotListItemByPage(HotItemQueryRequest hotItemQueryRequest, HttpServletRequest request);
}