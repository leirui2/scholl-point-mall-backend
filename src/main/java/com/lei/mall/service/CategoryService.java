package com.lei.mall.service;

import com.lei.mall.common.PageResult;
import com.lei.mall.model.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lei.mall.model.request.CategoryAddRequest;
import com.lei.mall.model.request.CategoryQueryRequest;
import com.lei.mall.model.request.CategoryUpdateRequest;

import javax.servlet.http.HttpServletRequest;

/**
* @author lei
* @description 针对表【category(商品类别表)】的数据库操作Service
* @createDate 2025-11-22 17:00:14
*/
public interface CategoryService extends IService<Category> {

    /**
     * 添加商品类别
     * @param categoryAddRequest 商品类别增加请求参数
     * @return 商品类别ID
     */
    long addCategory(CategoryAddRequest categoryAddRequest,HttpServletRequest request);

    /**
     * 商品类别信息修改
     */
    Category updateCategory(CategoryUpdateRequest categoryUpdateRequest, HttpServletRequest request);


    /**
     * 管理员根据ID查询商品类别完整信息
     * @param id 商品类别ID
     * @param request HTTP请求
     * @return 商品类别完整信息
     */
    Category getCategoryById(long id, HttpServletRequest request);

    /**
     * 管理员逻辑删除商品类别
     * @param id 商品类别ID
     * @param request HTTP请求
     * @return 是否删除成功
     */
    boolean deleteCategory(long id, HttpServletRequest request);

    /**
     * 管理员分页查询商品类别列表
     * @param categoryQueryRequest 查询条件
     * @param request HTTP请求
     * @return 用户列表分页结果
     */
    PageResult<Category> listCategoryByPage(CategoryQueryRequest categoryQueryRequest, HttpServletRequest request);
}
