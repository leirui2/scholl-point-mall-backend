package com.lei.mall.controller;

import com.lei.mall.common.ApiResponse;
import com.lei.mall.common.PageResult;
import com.lei.mall.common.ResultUtils;
import com.lei.mall.exception.BusinessException;
import com.lei.mall.model.entity.User;
import com.lei.mall.model.request.PurchaseItemRequest;
import com.lei.mall.model.request.PurchaseRecordQueryRequest;
import com.lei.mall.model.request.UserQueryRequest;
import com.lei.mall.model.vo.PurchaseRecordListVO;
import com.lei.mall.service.PurchaseRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author lei
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class PurchaseItemController {

    @Resource
    private PurchaseRecordService purchaseRecordService;

    /**
     * 下单商品
     * @param purchaseItemRequest 下单请求
     */
    @PostMapping("/addPurchaseItem")
    public ApiResponse<Boolean> purchaseItem(@RequestBody PurchaseItemRequest purchaseItemRequest, HttpServletRequest request){
        log.info("下单商品：{}", purchaseItemRequest);
        if (request == null) {
            throw new BusinessException("请求错误");
        }
        ApiResponse<Boolean> res = purchaseRecordService.purchaseItem(purchaseItemRequest, request);
        return ResultUtils.success(res.getData());
    }

    /**
     * 分页查询当前用户是的所有下单记录
     * @param purchaseRecordQueryRequest 已下单查询请求体
     */
    @PostMapping("/listAllRecord")
    public ApiResponse<PageResult<PurchaseRecordListVO>> listAllRecord(@RequestBody PurchaseRecordQueryRequest purchaseRecordQueryRequest, HttpServletRequest request) {
        PageResult<PurchaseRecordListVO> pageResult = purchaseRecordService.listAllRecord(purchaseRecordQueryRequest, request);
        return ResultUtils.success(pageResult);
    }


}
