package com.lei.mall.utils;

/**
 * 雪花算法ID生成器
 * Twitter的Snowflake算法Java实现
 * 64位ID结构: 1位符号位 + 41位时间戳 + 10位机器ID + 12位序列号
 */
public class SnowflakeIdGenerator {

    // 起始时间戳 (2025-01-01)
    private final static long EPOCH = 1735689600000L;

    // 各部分位数
    private final static long SEQUENCE_BITS = 12;
    private final static long WORKER_ID_BITS = 10;

    // 各部分最大值
    private final static long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    private final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);

    // 各部分偏移量
    private final static long WORKER_ID_SHIFT = SEQUENCE_BITS;
    private final static long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

    // 工作机器ID (0~1023)
    private final long workerId;
    
    // 序列号
    private long sequence = 0L;
    
    // 上次生成ID的时间戳
    private long lastTimestamp = -1L;

    /**
     * 构造函数
     * @param workerId 工作机器ID (0~1023)
     */
    public SnowflakeIdGenerator(long workerId) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", MAX_WORKER_ID));
        }
        this.workerId = workerId;
    }

    /**
     * 生成下一个ID
     * @return 唯一ID
     */
    public synchronized long nextId() {
        long timestamp = System.currentTimeMillis();

        // 如果当前时间小于上次生成ID的时间，说明系统时钟回退过，抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        // 如果是同一毫秒内生成的，则进行序列号递增
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            // 如果序列号溢出，等待下一毫秒
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            // 如果是下一毫秒，序列号重置
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        // 组装ID: 时间戳(41位) + 工作机器ID(10位) + 序列号(12位)
        return ((timestamp - EPOCH) << TIMESTAMP_LEFT_SHIFT) |
               (workerId << WORKER_ID_SHIFT) |
               sequence;
    }

    /**
     * 等待下一毫秒
     * @param lastTimestamp 上次生成ID的时间戳
     * @return 下一毫秒时间戳
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }
}