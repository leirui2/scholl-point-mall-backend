package com.lei.mall.controller;

import com.lei.mall.common.ApiResponse;
import com.lei.mall.common.PageResult;
import com.lei.mall.common.ResultUtils;
import com.lei.mall.exception.BusinessException;
import com.lei.mall.model.entity.Item;
import com.lei.mall.model.request.*;
import com.lei.mall.model.vo.ItemCategoryVO;
import com.lei.mall.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 商品信息接口
 *
 * @author lei
 */
@RestController
@RequestMapping("/item")
@Slf4j
public class ItemController {

    @Resource
    private ItemService itemService;

    /**
     * 添加商品信息
     * @param itemAddRequest 商品信息增加请求参数
     * @param request HTTP请求
     * @return 商品信息ID
     */
    @PostMapping("/add")
    public ApiResponse<Long> addItem(@RequestBody ItemAddRequest itemAddRequest, HttpServletRequest request) {
        log.info("添加商品信息：{}", itemAddRequest);
        long id = itemService.addItem(itemAddRequest,request);
        return ResultUtils.success(id);
    }


    /**
     * 商品信息信息修改
     */
    @PostMapping("/update")
    public ApiResponse<Item> updateItem(@RequestBody ItemUpdateRequest itemUpdateRequest,HttpServletRequest request){
        log.info("修改商品信息信息：{}", itemUpdateRequest);
        if (request == null) {
            throw new BusinessException("请求错误");
        }
        if (StringUtils.isAnyBlank(itemUpdateRequest.getName(), itemUpdateRequest.getId().toString())) {
            throw new BusinessException("商品信息信息缺少");
        }
        Item item = itemService.updateItem(itemUpdateRequest,request);
        return ResultUtils.success(item);
    }


    /**
     * 管理员根据ID查询商品信息完整信息
     * @param id 商品信息ID
     * @param request HTTP请求
     * @return 商品信息完整信息
     */
    @GetMapping("/getItemById")
    public ApiResponse<ItemCategoryVO> getItemById(long id, HttpServletRequest request) {
        ItemCategoryVO itemCategoryVO = itemService.getItemById(id, request);
        return ResultUtils.success(itemCategoryVO);
    }


    /**
     * 管理员更新商品信息状态（启用/禁用）
     * @param id 商品信息ID
     * @param status 商品信息状态 0-正常 1-禁用
     * @param request HTTP请求
     * @return 是否更新成功
     */
    @PostMapping("/updateItemStatus")
    public ApiResponse<Boolean> updateItemStatus(long id, int status, HttpServletRequest request) {
        boolean result = itemService.updateItemStatus(id, status, request);
        return ResultUtils.success(result);
    }


    /**
     * 管理员逻辑删除商品信息
     * @param id 商品信息ID
     * @param request HTTP请求
     * @return 是否删除成功
     */
    @PostMapping("/deleteItem")
    public ApiResponse<Boolean> deleteItem(long id, HttpServletRequest request) {
        boolean result = itemService.deleteItem(id, request);
        return ResultUtils.success(result);
    }

    /**
     * 管理员分页查询商品信息列表
     * @param itemQueryRequest 查询条件
     * @param request HTTP请求
     * @return 商品信息列表分页结果
     */
    @PostMapping("/listItemByPage")
    public ApiResponse<PageResult<Item>> listItemByPage(@RequestBody ItemQueryRequest itemQueryRequest, HttpServletRequest request) {
        PageResult<Item> pageResult = itemService.listItemByPage(itemQueryRequest, request);
        return ResultUtils.success(pageResult);
    }

    /**
     * 管理员分页查询商品和类别信息列表
     * @param itemQueryRequest 查询条件
     * @param request HTTP请求
     * @return 商品和类别信息列表分页结果
     */
    @PostMapping("/listItemCategoryByPage")
    public ApiResponse<PageResult<ItemCategoryVO>> listItemCategoryByPage(@RequestBody ItemQueryRequest itemQueryRequest, HttpServletRequest request) {
        PageResult<ItemCategoryVO> pageResult = itemService.listItemCategoryByPage(itemQueryRequest, request);
        return ResultUtils.success(pageResult);
    }
}