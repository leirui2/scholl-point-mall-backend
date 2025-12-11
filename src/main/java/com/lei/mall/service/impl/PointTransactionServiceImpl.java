package com.lei.mall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lei.mall.model.entity.TransactionRecord;
import com.lei.mall.service.PointTransactionService;
import com.lei.mall.mapper.PointTransactionMapper;
import org.springframework.stereotype.Service;

/**
* @author lei
* @description 针对表【point_transaction(积分流水表)】的数据库操作Service实现
* @createDate 2025-12-05 23:01:11
*/
@Service
public class PointTransactionServiceImpl extends ServiceImpl<PointTransactionMapper, TransactionRecord>
    implements PointTransactionService{

}




