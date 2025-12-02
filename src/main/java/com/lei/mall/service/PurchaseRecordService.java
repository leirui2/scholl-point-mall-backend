package com.lei.mall.service;

import com.lei.mall.common.ApiResponse;
import com.lei.mall.common.PageResult;
import com.lei.mall.model.entity.PurchaseRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lei.mall.model.request.PurchaseItemRequest;
import com.lei.mall.model.request.PurchaseRecordQueryRequest;
import com.lei.mall.model.vo.PurchaseRecordListVO;

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
    ApiResponse<Boolean> purchaseItem(PurchaseItemRequest purchaseItemRequest, HttpServletRequest request);

    /**
     * 分页查询当前用户是的所有下单记录
     * @param purchaseRecordQueryRequest 已下单查询请求体
     */
    PageResult<PurchaseRecordListVO> listAllRecord(PurchaseRecordQueryRequest purchaseRecordQueryRequest, HttpServletRequest request);
}
