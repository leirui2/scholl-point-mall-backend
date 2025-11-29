package com.lei.mall.service;

import com.lei.mall.model.entity.PurchaseRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lei.mall.model.request.PurchaseItemRequest;

import javax.servlet.http.HttpServletRequest;

/**
* @author lei
* @description 针对表【purchase_record(购买记录表)】的数据库操作Service
* @createDate 2025-11-29 19:07:48
*/
public interface PurchaseRecordService extends IService<PurchaseRecord> {

    /**
     * 下单商品
     * @param purchaseItemRequest 下单请求
     */
    Boolean purchaseItem(PurchaseItemRequest purchaseItemRequest, HttpServletRequest request);
}
