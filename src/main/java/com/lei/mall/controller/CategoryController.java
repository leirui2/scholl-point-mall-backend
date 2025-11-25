package com.lei.mall.controller;

import com.lei.mall.common.ApiResponse;
import com.lei.mall.common.PageResult;
import com.lei.mall.common.ResultUtils;
import com.lei.mall.exception.BusinessException;
import com.lei.mall.model.entity.Category;
import com.lei.mall.model.entity.User;
import com.lei.mall.model.request.*;
import com.lei.mall.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 商品类别接口
 * @author lei
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    /**
     * 添加商品类别
     * @param categoryAddRequest 商品类别增加请求参数
     * @return 商品类别ID
     */
    @PostMapping("/add")
    public ApiResponse<Long> addCategory(@RequestBody CategoryAddRequest categoryAddRequest,HttpServletRequest request) {
        log.info("商品类别增加：{}", categoryAddRequest);
        long id = categoryService.addCategory(categoryAddRequest,request);
        return ResultUtils.success(id);
    }


    /**
     * 商品类别信息修改
     */
    @PostMapping("/update")
    public ApiResponse<Category> updateCategory(@RequestBody CategoryUpdateRequest categoryUpdateRequest,HttpServletRequest request){
        log.info("修改商品类别信息：{}", categoryUpdateRequest);
        if (request == null) {
            throw new BusinessException("请求错误");
        }
        if (StringUtils.isAnyBlank(categoryUpdateRequest.getName(), categoryUpdateRequest.getId().toString())) {
            throw new BusinessException("商品类别信息缺少");
        }
        Category category = categoryService.updateCategory(categoryUpdateRequest,request);
        return ResultUtils.success(category);
    }


    /**
     * 管理员根据ID查询商品类别完整信息
     * @param id 商品类别ID
     * @param request HTTP请求
     * @return 商品类别完整信息
     */
    @GetMapping("/getCategoryById")
    public ApiResponse<Category> getCategoryById(long id, HttpServletRequest request) {
        Category category = categoryService.getCategoryById(id, request);
        return ResultUtils.success(category);
    }


    /**
     * 管理员更新商品类别状态（启用/禁用）
     * @param id 商品类别ID
     * @param status 商品类别状态 0-正常 1-禁用
     * @param request HTTP请求
     * @return 是否更新成功
     */
    @PostMapping("/updateCategoryStatus")
    public ApiResponse<Boolean> updateCategoryStatus(@RequestParam long id, @RequestParam int status, HttpServletRequest request) {
        boolean result = categoryService.updateCategoryStatus(id, status, request);
        return ResultUtils.success(result);
    }


    /**
     * 管理员逻辑删除商品类别
     * @param id 商品类别ID
     * @param request HTTP请求
     * @return 是否删除成功
     */
    @PostMapping("/deleteCategory")
    public ApiResponse<Boolean> deleteCategory(@RequestParam long id, HttpServletRequest request) {
        boolean result = categoryService.deleteCategory(id, request);
        return ResultUtils.success(result);
    }

    /**
     * 管理员分页查询商品类别列表
     * @param categoryQueryRequest 查询条件
     * @param request HTTP请求
     * @return 用户列表分页结果
     */
    @PostMapping("/admin/listCategoryByPage")
    public ApiResponse<PageResult<Category>> listCategoryByPage(@RequestBody CategoryQueryRequest categoryQueryRequest, HttpServletRequest request) {
        PageResult<Category> pageResult = categoryService.listCategoryByPage(categoryQueryRequest, request);
        return ResultUtils.success(pageResult);
    }
}