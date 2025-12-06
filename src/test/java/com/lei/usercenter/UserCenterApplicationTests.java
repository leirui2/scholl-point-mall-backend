package com.lei.usercenter;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lei.mall.model.entity.PurchaseRecord;
import com.lei.mall.service.PurchaseRecordService;
import com.lei.mall.mallApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootTest(classes = mallApplication.class)
class UserCenterApplicationTests {
    @Autowired
    private PurchaseRecordService purchaseRecordService;

    @Test
    void contextLoads() {
        // 获取今天的日期
        LocalDate today = LocalDate.now();
        // 获取昨天的日期
        LocalDate yesterday = today.minusDays(1);
        // 如果需要格式化输出
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        QueryWrapper<PurchaseRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("createTime", yesterday.format(formatter));
        List<PurchaseRecord> records = purchaseRecordService.list(queryWrapper);

        // 验证结果
        records.forEach(System.out::println);


    }

}