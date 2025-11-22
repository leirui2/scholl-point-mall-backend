package com.lei.mall.config;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.multipart.MultipartFile;

/**
 * Spring MVC Json 配置
 *
 * @author lei
 */
@JsonComponent
@Configuration
public class JsonConfig {

    /**
     * 添加 Long 转 json 精度丢失的配置
     */
    @Bean
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Long.class, ToStringSerializer.instance);
        module.addSerializer(Long.TYPE, ToStringSerializer.instance);
        objectMapper.registerModule(module);
        return objectMapper;
    }

    /**
     * 配置FastJSON，解决MultipartFile序列化问题
     */
    @Bean
    public FastJsonConfig fastJsonConfig() {
        FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(SerializerFeature.WriteMapNullValue);
        
        // 添加ValueFilter来处理MultipartFile对象
        ValueFilter valueFilter = (object, name, value) -> {
            if (value instanceof MultipartFile) {
                // 当遇到MultipartFile类型时，返回文件名而不是尝试序列化整个对象
                return ((MultipartFile) value).getOriginalFilename();
            }
            return value;
        };
        
        config.setSerializeConfig(new SerializeConfig());
        config.setSerializeFilters(valueFilter);
        return config;
    }
}