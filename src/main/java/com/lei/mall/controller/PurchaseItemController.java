package com.lei.mall.controller;

import com.lei.mall.common.ApiResponse;
import com.lei.mall.common.ResultUtils;
import com.lei.mall.exception.BusinessException;
import com.lei.mall.model.request.PurchaseItemRequest;
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
        Boolean res = purchaseRecordService.purchaseItem(purchaseItemRequest,request);
        return ResultUtils.success(res);
    }


}
