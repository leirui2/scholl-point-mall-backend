package com.lei.mall.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 订单号生成工具类
 * @author lei
 */
public class OrderNoUtils {
    
    // 使用原子整数保证线程安全
    private static final AtomicInteger sequence = new AtomicInteger(0);
    
    // 随机数生成器
    private static final Random random = new Random();
    
    // 雪花算法ID生成器实例 (使用机器ID 1)
    private static final SnowflakeIdGenerator snowflake = new SnowflakeIdGenerator(1);

    /**
     * 生成订单号 (基于雪花算法)
     * @return 订单号
     */
    public static String generateOrderNoWithSnowflake() {
        long id = snowflake.nextId();
        return String.valueOf(id);
    }

    /**
     * 生成订单号 (基于时间戳+随机数)
     * 格式：YYYYMMDD + 6位随机数字 (如：20251202000001)
     * @return 订单号
     */
    public static String generateOrderNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String datePart = sdf.format(new Date());
        // 生成6位随机数
        String sequencePart = String.format("%06d", random.nextInt(1000000));
        return datePart + sequencePart;
    }
}