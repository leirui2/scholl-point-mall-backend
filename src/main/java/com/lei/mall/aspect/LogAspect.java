package com.lei.mall.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.lei.mall.common.ApiResponse;
import com.lei.mall.mapper.OperationLogMapper;
import com.lei.mall.model.entity.OperationLog;
import com.lei.mall.model.entity.User;
import com.lei.mall.model.vo.UserLoginVO;
import com.lei.mall.utils.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static com.lei.mall.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 日志切面类
 * @author lei
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    @Resource
    private OperationLogMapper operationLogMapper;

    /**
     * 环绕通知记录操作日志
     * @param joinPoint 连接点
     * @return 方法执行结果
     * @throws Throwable 异常
     */
    @Around("execution(* com.lei.mall.controller..*.*(..)) && (@annotation(org.springframework.web.bind.annotation.RequestMapping) || @annotation(org.springframework.web.bind.annotation.PostMapping) || @annotation(org.springframework.web.bind.annotation.GetMapping))")
    public Object logOperation(ProceedingJoinPoint joinPoint) throws Throwable {
        // 创建操作日志对象
        OperationLog operationLog = new OperationLog();
        
        // 获取当前HTTP请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            
            // 获取用户信息
            Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
            if (userObj != null) {
                // Session中存储的可能是User实体或UserLoginVO对象
                if (userObj instanceof UserLoginVO) {
                    UserLoginVO userLoginVO = (UserLoginVO) userObj;
                    operationLog.setUserId(userLoginVO.getId());
                } else if (userObj instanceof User) {
                    User user = (User) userObj;
                    operationLog.setUserId(user.getId());
                }
            } else {
                // 用户未登录时设置默认值
                operationLog.setUserId(0L);
            }

            // 记录请求信息
            operationLog.setMethod(request.getMethod());
            operationLog.setUri(request.getRequestURI());
            operationLog.setIp(IPUtil.getRealIP(request));
            operationLog.setUserAgent(request.getHeader("User-Agent"));
            
            // 记录请求参数
            Object[] args = joinPoint.getArgs();
            if (args.length > 0) {
                // 过滤掉无法序列化的对象（如HttpServletRequest等）
                Object[] filteredArgs = Arrays.stream(args)
                        .filter(arg -> !(arg instanceof HttpServletRequest))
                        .filter(arg -> !(arg instanceof HttpServletResponse))
                        .toArray();
                
                if (filteredArgs.length > 0) {
                    // 特殊处理MultipartFile类型的参数
                    Object[] processedArgs = Arrays.stream(filteredArgs)
                            .map(this::processArg)
                            .toArray();
                    
                    // 使用自定义属性过滤器排除MultipartFile等无法序列化的对象
                    operationLog.setRequestParams(JSON.toJSONString(processedArgs, SerializerFeature.WriteMapNullValue));
                }
            }
        }
        
        operationLog.setOperation(joinPoint.getSignature().getName());
        operationLog.setOperationTime(new Date());
        
        long startTime = System.currentTimeMillis();
        Object result;
        try {
            // 执行目标方法
            result = joinPoint.proceed();
            
            // 对于登录操作，需要重新获取用户信息
            if ("userLogin".equals(operationLog.getOperation()) && attributes != null) {
                Object userObj = attributes.getRequest().getSession().getAttribute(USER_LOGIN_STATE);
                if (userObj != null) {
                    // Session中存储的可能是User实体或UserLoginVO对象
                    if (userObj instanceof UserLoginVO) {
                        UserLoginVO userLoginVO = (UserLoginVO) userObj;
                        operationLog.setUserId(userLoginVO.getId());
                    } else if (userObj instanceof User) {
                        User user = (User) userObj;
                        operationLog.setUserId(user.getId());
                    }
                }
            }
            
            // 对于注册操作，需要从返回结果中获取新注册用户的ID
            if ("register".equals(operationLog.getOperation()) && result instanceof ApiResponse) {
                ApiResponse<?> apiResponse = (ApiResponse<?>) result;
                // 如果返回结果是成功的，且数据是Long类型（用户ID），则更新操作日志中的用户ID
                if (apiResponse.getCode() == 0 && apiResponse.getData() instanceof Long) {
                    operationLog.setUserId((Long) apiResponse.getData());
                }
            }
            
            // 记录响应结果
            try {
                operationLog.setResponseResult(JSON.toJSONString(result, SerializerFeature.WriteMapNullValue));
            } catch (Exception e) {
                // 忽略序列化异常
                log.warn("序列化响应结果失败", e);
            }
            operationLog.setStatus(0); // 成功
        } catch (Exception e) {
            operationLog.setStatus(1); // 失败
            // 限制errorMsg长度，避免超出数据库字段限制
            String errorMsg = e.getMessage();
            if (errorMsg != null && errorMsg.length() > 500) {
                errorMsg = errorMsg.substring(0, 500);
            }
            operationLog.setErrorMsg(errorMsg);
            throw e;
        } finally {
            long endTime = System.currentTimeMillis();
            operationLog.setCostTime(endTime - startTime);
            
            // 保存操作日志
            try {
                operationLogMapper.insert(operationLog);
            } catch (Exception e) {
                log.error("保存操作日志失败", e);
            }
        }
        
        return result;
    }
    
    /**
     * 处理参数，特别处理MultipartFile类型
     * @param arg 原始参数
     * @return 处理后的参数
     */
    private Object processArg(Object arg) {
        if (arg instanceof MultipartFile) {
            MultipartFile file = (MultipartFile) arg;
            return new FileInfo(file.getOriginalFilename(), file.getSize(), file.getContentType());
        } else if (arg instanceof MultipartFile[]) {
            MultipartFile[] files = (MultipartFile[]) arg;
            List<FileInfo> fileInfoList = new ArrayList<>();
            for (MultipartFile file : files) {
                fileInfoList.add(new FileInfo(file.getOriginalFilename(), file.getSize(), file.getContentType()));
            }
            return fileInfoList.toArray();
        }
        return arg;
    }
    
    /**
     * 文件信息包装类
     */
    private static class FileInfo {
        private String name;
        private long size;
        private String contentType;
        
        public FileInfo(String name, long size, String contentType) {
            this.name = name;
            this.size = size;
            this.contentType = contentType;
        }
        
        public String getName() {
            return name;
        }
        
        public long getSize() {
            return size;
        }
        
        public String getContentType() {
            return contentType;
        }
    }
}