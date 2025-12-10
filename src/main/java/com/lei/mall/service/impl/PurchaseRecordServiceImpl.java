package com.lei.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lei.mall.common.ApiResponse;
import com.lei.mall.common.ErrorCode;
import com.lei.mall.common.PageResult;
import com.lei.mall.common.ResultUtils;
import com.lei.mall.exception.BusinessException;
import com.lei.mall.mapper.ItemMapper;
import com.lei.mall.model.entity.Item;
import com.lei.mall.model.entity.PointTransaction;
import com.lei.mall.model.entity.PurchaseRecord;
import com.lei.mall.model.entity.User;
import com.lei.mall.model.request.PurchaseItemRequest;
import com.lei.mall.model.request.PurchaseRecordQueryRequest;
import com.lei.mall.model.vo.PurchaseRecordListVO;
import com.lei.mall.model.vo.UserLoginVO;
import com.lei.mall.service.ItemService;
import com.lei.mall.service.PointTransactionService;
import com.lei.mall.service.PurchaseRecordService;
import com.lei.mall.mapper.PurchaseRecordMapper;
import com.lei.mall.service.UserService;
import com.lei.mall.utils.OrderNoUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author lei
* @description 针对表【purchase_record(购买记录表)】的数据库操作Service实现
* @createDate 2025-11-29 19:07:48
*/
@Service
@Slf4j
public class PurchaseRecordServiceImpl extends ServiceImpl<PurchaseRecordMapper, PurchaseRecord>
    implements PurchaseRecordService{

    @Resource
    private ItemService itemService;

    @Resource
    private ItemMapper itemMapper;

    @Resource
    private UserService userService;

    @Resource
    private PointTransactionService pointTransactionService;

    /**
     * 下单商品
     * @param purchaseItemRequest 下单请求
     */
    @Override
    @Transactional(rollbackFor = Exception.class)   //事务
    public ApiResponse<Boolean> purchaseItem(PurchaseItemRequest purchaseItemRequest, HttpServletRequest request) throws BusinessException {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "请求参数错误");
        }
        Long itemId = purchaseItemRequest.getItemId();
        Long userId = userService.getLoginUser(request).getId();
        Integer num = purchaseItemRequest.getNum();

        if(itemId == null || userId == null || num == null || num <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "下单商品参数缺少或无效");
        }
        // 获取当前用户
        UserLoginVO loginUser = userService.getLoginUser(request);
        //获取当前商品
        Item item = itemMapper.selectById(itemId);

        // 检查商品状态 // 假设0表示上架状态
        if (item == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "商品不存在");
        }
        
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "用户未登录");
        }
        
        if (item.getStatus() != 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "商品已下架");
        }
        // 检查用户状态 // 假设0表示正常状态
        if (loginUser.getUserStatus() != 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "用户账户异常");
        }

        //剩余积分
        User user = userService.getById(loginUser.getId());
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "用户不存在");
        }
        int remainingPoints = user.getPoints() - (item.getPointPrice() * num);
        if ( remainingPoints < 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(),"剩余积分不够");
        }
        if(item.getStock() < num){
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(),"商品库存不够");
        }

        try {
            // 购买逻辑

            //修改用户剩余积分
            user.setPoints(remainingPoints);
            userService.updateById(user);

            //修改商品剩余数量
            // 使用自定义xml获取最新的商品数据进行更新
            Item latestItem = itemMapper.selectById(itemId);
            if (latestItem == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "商品不存在");
            }
            // 再次检查库存，确保并发安全
            if (latestItem.getStock() < num) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "商品库存不够");
            }
            // 更新库存
            latestItem.setStock(latestItem.getStock() - num);
            // 更新订单数
            Long currentOrderCount = latestItem.getOrderCount();
//            System.out.println("最新的商品信息："+latestItem);
//            System.out.println("当前已经下单数 ： " + currentOrderCount);
            if (currentOrderCount == null) {
                currentOrderCount = 0L;
            }
            latestItem.setOrderCount(currentOrderCount + 1);
            // 保存更新
            itemService.updateById(latestItem);

            //添加购买记录
            PurchaseRecord purchaseRecord = new PurchaseRecord();
            purchaseRecord.setItemId(itemId);
            purchaseRecord.setUserId(userId);
            purchaseRecord.setItemName(item.getName());
            purchaseRecord.setNum(num);
            //生成订单号
            String orderNo = OrderNoUtils.generateOrderNo();
            purchaseRecord.setOrderNumber(orderNo);

            Boolean res = this.save(purchaseRecord);
            if (!res) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR.getCode(), "添加购买记录保存失败");
            }
            //积分流水记录表
            PointTransaction pointTransaction = new PointTransaction();
            pointTransaction.setPoints((item.getPointPrice() * num));
            //积分变动类型 (1: 签到奖励, 2: 兑换商品, 3: 补签扣除等)
            pointTransaction.setType(2);
            pointTransaction.setUserId(user.getId());
            //获取购买记录表对象的ID
            pointTransaction.setBusinessId(purchaseRecord.getId());
            String msg =  "兑换了 "+num+" 个编号是 "+ + itemId + " 的商品，共用了 " +(item.getPointPrice() * num) + "积分。" ;
            pointTransaction.setDescription(msg);
            res = pointTransactionService.save(pointTransaction);
            if (!res) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR.getCode(), "积分流水记录表保存失败");
            }


        }  catch (Exception e) {
            // 系统异常记录日志并抛出
            log.error("购买商品时发生系统错误，itemId={}, userId={}, num={}", itemId, userId, num, e);
        }
        return ResultUtils.success(true);
    }

    /**
     * 分页查询当前用户是的所有下单记录
     * @param purchaseRecordQueryRequest 已下单查询请求体
     */
    @Override
    public PageResult<PurchaseRecordListVO> listAllRecord(PurchaseRecordQueryRequest purchaseRecordQueryRequest, HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "请求参数错误");
        }

        // 验证分页参数
        if (purchaseRecordQueryRequest.getCurrent() <= 0) {
            purchaseRecordQueryRequest.setCurrent(1);
        }
        if (purchaseRecordQueryRequest.getPageSize() <= 0 || purchaseRecordQueryRequest.getPageSize() > 100) {
            purchaseRecordQueryRequest.setPageSize(10);
        }

        // 获取当前登录用户
        UserLoginVO loginUser = userService.getLoginUser(request);
        if (loginUser == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "必须登录才可以查询");
        }

        // 构造查询条件
        QueryWrapper<PurchaseRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("isDelete", 0);

        // 添加排序规则，确保分页结果的一致性
        queryWrapper.orderByDesc("createTime"); // 按创建时间倒序
        queryWrapper.eq("userId", loginUser.getId());
        
        // 构建动态查询条件
        if (StringUtils.isNotBlank(purchaseRecordQueryRequest.getItemName())) {
            queryWrapper.like("itemName", purchaseRecordQueryRequest.getItemName());
        }

        if (StringUtils.isNotBlank(purchaseRecordQueryRequest.getOrderNumber())) {
            queryWrapper.like("orderNumber", purchaseRecordQueryRequest.getOrderNumber());
        }
        
        // 分页查询
        Page<PurchaseRecord> page = new Page<>(purchaseRecordQueryRequest.getCurrent(), purchaseRecordQueryRequest.getPageSize());
        this.page(page, queryWrapper);
        
        // 提取所有itemId
        List<Long> itemIds = page.getRecords().stream()
                .map(PurchaseRecord::getItemId)
                .distinct()
                .collect(Collectors.toList());

        // 批量查询Item信息
        Map<Long, Item> itemMap = new HashMap<>();
        if (itemIds != null && !itemIds.isEmpty()) {
            List<Item> itemList = itemService.listByIds(itemIds);
            itemMap = itemList.stream().collect(Collectors.toMap(Item::getId, item -> item));
        }

        // 构造分页结果
        PageResult<PurchaseRecordListVO> pageResult = new PageResult<>();
        // 转换实体对象为 VO 对象
        List<PurchaseRecordListVO> voList = new ArrayList<>();
        for (PurchaseRecord record : page.getRecords()) {
            PurchaseRecordListVO vo = new PurchaseRecordListVO();
            BeanUtils.copyProperties(record, vo);
            // 从map中获取Item信息，避免多次数据库查询
            vo.setItem(itemMap.get(record.getItemId()));
            voList.add(vo);
        }

        pageResult.setRecords(voList);
        pageResult.setTotal(page.getTotal());
        pageResult.setCurrent(page.getCurrent());
        pageResult.setSize(page.getSize());

        return pageResult;
    }


}