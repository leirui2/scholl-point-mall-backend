package com.lei.mall.config;

import com.alipay.easysdk.kernel.Config;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lei
 * @Description: 支付宝配置类
 * @Version: V1.0
 */
@Configuration
@Data
public class AlipayConfig {

    // 请求协议
    @Value("${alipay.easy.protocol}")
    private String protocol;

    // 支付宝网关地址
    @Value("${alipay.easy.gatewayHost}")
    private String gatewayHost;

    // 签名算法类型
    @Value("${alipay.easy.signType}")
    private String signType;

    // 应用ID
    @Value("${alipay.easy.appId}")
    private String appId;

    // 商户私钥
    @Value("${alipay.easy.merchantPrivateKey}")
    private String merchantPrivateKey;

    // 支付宝公钥
    @Value("${alipay.easy.alipayPublicKey}")
    private String alipayPublicKey;

    // 异步通知地址
    @Value("${alipay.easy.notifyUrl}")
    private String notifyUrl;

    @Bean
    public Config config() {
        Config config = new Config();
        config.protocol = this.protocol;
        config.gatewayHost = this.gatewayHost;
        config.signType = this.signType;
        config.appId = this.appId;
        config.merchantPrivateKey = this.merchantPrivateKey;
        config.alipayPublicKey = this.alipayPublicKey;
        //可设置异步通知接收服务地址（可选）
        config.notifyUrl = this.notifyUrl;
        config.encryptKey = "";
        return config;
    }
}