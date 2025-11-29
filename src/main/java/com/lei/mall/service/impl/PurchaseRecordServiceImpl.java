package com.lei.mall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lei.mall.common.ErrorCode;
import com.lei.mall.exception.BusinessException;
import com.lei.mall.model.entity.Item;
import com.lei.mall.model.entity.PurchaseRecord;
import com.lei.mall.model.entity.User;
import com.lei.mall.model.request.PurchaseItemRequest;
import com.lei.mall.model.vo.ItemCategoryVO;
import com.lei.mall.model.vo.UserLoginVO;
import com.lei.mall.service.ItemService;
import com.lei.mall.service.PurchaseRecordService;
import com.lei.mall.mapper.PurchaseRecordMapper;
import com.lei.mall.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
* @author lei
* @description 针对表【purchase_record(购买记录表)】的数据库操作Service实现
* @createDate 2025-11-29 19:07:48
*/
@Service
public class PurchaseRecordServiceImpl extends ServiceImpl<PurchaseRecordMapper, PurchaseRecord>
    implements PurchaseRecordService{

    @Resource
    private ItemService itemService;


    @Resource
    private UserService userService;

    /**
     * 下单商品
     * @param purchaseItemRequest 下单请求
     */
    @Override
    public Boolean purchaseItem(PurchaseItemRequest purchaseItemRequest, HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "请求参数错误");
        }
        Long itemId = purchaseItemRequest.getItemId();
        Long userId = userService.getLoginUser(request).getId();
        Integer num = purchaseItemRequest.getNum();

        if(StringUtils.isAnyBlank(itemId.toString(),userId.toString(),num.toString())){
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "下单商品参数缺少");
        }
        // 获取当前用户
        UserLoginVO loginUser = userService.getLoginUser(request);
        Item item = itemService.getById(itemId);
        //剩余积分
        int remainingPoints = loginUser.getPoints() - (item.getPrice() * num);
        if ( remainingPoints < 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(),"剩余积分不够");
        }
        if(item.getStock() < num){
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(),"商品库存不够");
        }
        //修改用户剩余积分
        User user = userService.getById(loginUser.getId());
        user.setPoints(remainingPoints);
        userService.updateById(user);

        //修改商品剩余数量
        item.setStock(item.getStock()-num);
        itemService.updateById(item);

        //添加购买记录
        PurchaseRecord purchaseRecord = new PurchaseRecord();
        purchaseRecord.setItemId(itemId);
        purchaseRecord.setUserId(userId);
        purchaseRecord.setNum(num);

        return this.save(purchaseRecord);
    }
}