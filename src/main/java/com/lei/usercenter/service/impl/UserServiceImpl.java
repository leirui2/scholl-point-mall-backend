package com.lei.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lei.usercenter.common.ErrorCode;
import com.lei.usercenter.common.PageResult;
import com.lei.usercenter.exception.BusinessException;
import com.lei.usercenter.mapper.LoginLogMapper;
import com.lei.usercenter.mapper.UserActivityMapper;
import com.lei.usercenter.model.entity.LoginLog;
import com.lei.usercenter.model.entity.User;
import com.lei.usercenter.model.entity.UserActivity;
import com.lei.usercenter.model.user.*;
import com.lei.usercenter.model.vo.UserLoginVO;
import com.lei.usercenter.model.vo.UserUpdateVO;
import com.lei.usercenter.service.UserService;
import com.lei.usercenter.mapper.UserMapper;
import com.lei.usercenter.utils.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.lei.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author lei
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2025-11-08 14:53:59
*/
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    /**
     * 盐值，混淆密码
     */
    public static final String SALT = "lei";

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private LoginLogMapper loginLogMapper;

    @Resource
    private UserActivityMapper userActivityMapper;

    /**
     * 用户注册
     * @param user
     * @return
     */
    @Override
    public long userRegister(UserRegisterRequest user) {
        // 使用Redisson分布式锁
        RLock lock = redissonClient.getLock("register:" + user.getUserAccount());
        try {
            if (lock.tryLock(10, 10, TimeUnit.SECONDS)) {
                // 执行注册逻辑
                return doRegister(user);
            } else {
                throw new BusinessException("注册请求过于频繁，请稍后再试");
            }
        } catch (InterruptedException e) {
            throw new BusinessException("注册失败");
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    private long doRegister(UserRegisterRequest user) {
        //1、校验
        if (user.getUserAccount() == null || user.getUserPassword() == null){
            throw new BusinessException("用户账号或密码不能为空");
        }
        if (user.getUserAccount().length() < 6 || user.getUserAccount().length() > 20) {
            throw new BusinessException("用户账号不能小于6位，不能超过20位");
        }
        if (user.getUserPassword().length() < 8 ||user.getUserPassword().length() > 30) {
            throw new BusinessException("用户密码不能小于8位，不能超过30位");
        }
        if (user.getCheckPassword() != null && !user.getCheckPassword().equals(user.getUserPassword())) {
            throw new BusinessException("两次输入的密码不一致");
        }

        //账户不包含特殊字符
        // 定义一个正则表达式模式，用于验证字符串是否只包含字母、数字和下划线
        // ^ 表示字符串开始
        // [a-zA-Z0-9_] 表示允许的字符范围：小写字母、大写字母、数字、下划线
        // + 表示前面的字符集可以出现一次或多次
        // $ 表示字符串结束
        String validPattern = "^[a-zA-Z0-9_]+$";
        if (!user.getUserAccount().matches(validPattern)) {
            throw new BusinessException("用户账号不能包含特殊字符");
        }

        //账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", user.getUserAccount());
        long cnt = this.baseMapper.selectCount(queryWrapper);
        if (cnt > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "账号重复");
        }

        User newUser = new User();
        // 2. 加密
        String userPassword = user.getUserPassword();
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        newUser.setUserAccount(user.getUserAccount());
        newUser.setUserPassword(encryptPassword);
        newUser.setUserName(user.getUserAccount());
        boolean saveResult = this.save(newUser);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR.getCode(), "注册失败，数据库错误");
        }
        
        // 初始化用户活跃度记录
        UserActivity userActivity = new UserActivity();
        userActivity.setUserId(newUser.getId());
        userActivity.setLoginCount(0);
        userActivity.setActivityScore(0);
        userActivityMapper.insert(userActivity);
        
        return newUser.getId();
    }


    /**
     * 登录
     * @param userLoginRequest
     * @param request
     * @return
     */
    @Override
    public UserLoginVO userlogin(UserLoginRequest userLoginRequest, HttpServletRequest request) {

//        // 如果已经登录且账号匹配，直接返回用户信息
//        Object sessionObj = request.getSession().getAttribute(USER_LOGIN_STATE);
//        if (sessionObj != null) {
//            if (sessionObj instanceof User) {
//                User sessionUser = (User) sessionObj;
//                if (userLoginRequest.getUserAccount().equals(sessionUser.getUserAccount())) {
//                    log.info("已经登录，直接返回：{}",sessionUser);
//                    // 更新Redis缓存
//                    String redisKey = "user:" + sessionUser.getId();
//                    redisTemplate.opsForValue().set(redisKey, sessionUser, 30, TimeUnit.MINUTES);
//                    // 返回脱敏用户信息
//                    return this.getLoginUserVO(sessionUser);
//                }
//            }
//        }

        //1、校验
        String account = userLoginRequest.getUserAccount();
        String password = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(account,password)){
            // 记录登录日志
            LoginLog loginLog = new LoginLog();
            loginLog.setIp(IPUtil.getRealIP(request));
            loginLog.setUserAgent(request.getHeader("User-Agent"));
            loginLog.setLoginTime(new Date());
            loginLog.setCreateTime(new Date());
            loginLog.setStatus(1); // 失败
            loginLog.setUserId(0L); // 设置默认用户ID，避免数据库约束错误
            // 限制errorMsg长度，避免超出数据库字段限制
            String errorMsg = "用户账号或密码不能为空";
            if (errorMsg.length() > 1000) {
                errorMsg = errorMsg.substring(0, 1000);
            }
            loginLog.setErrorMsg(errorMsg);
            loginLogMapper.insert(loginLog);
            throw new BusinessException("用户账号或密码不能为空");
        }
        
        // 记录登录日志
        LoginLog loginLog = new LoginLog();
        loginLog.setIp(IPUtil.getRealIP(request));
        loginLog.setUserAgent(request.getHeader("User-Agent"));
        loginLog.setLoginTime(new Date());
        loginLog.setCreateTime(new Date());
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        QueryWrapper< User> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("userAccount", account);
        queryWrapper.eq("userPassword",encryptPassword);

        User user = this.baseMapper.selectOne(queryWrapper);
        if(user == null){
            log.info("登录失败，用户名或密码错误。");
            loginLog.setStatus(1); // 失败
            loginLog.setUserId(0L); // 设置默认用户ID，避免数据库约束错误
            // 限制errorMsg长度，避免超出数据库字段限制
            String errorMsg = "用户不存在或密码错误";
            if (errorMsg.length() > 1000) {
                errorMsg = errorMsg.substring(0, 1000);
            }
            loginLog.setErrorMsg(errorMsg);
            loginLogMapper.insert(loginLog);
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "用户不存在或密码错误");
        }
        if(user.getUserStatus() == 1){
            loginLog.setStatus(1); // 失败
            loginLog.setUserId(user.getId()); // 用户存在但被禁用
            // 限制errorMsg长度，避免超出数据库字段限制
            String errorMsg = "用户被禁用";
            if (errorMsg.length() > 1000) {
                errorMsg = errorMsg.substring(0, 1000);
            }
            loginLog.setErrorMsg(errorMsg);
            loginLogMapper.insert(loginLog);
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR.getCode(), "用户被禁用");
        }
        
        // 登录成功记录
        loginLog.setUserId(user.getId());
        loginLog.setStatus(0); // 成功
        loginLogMapper.insert(loginLog);
        
        // 更新用户活跃度
        updateUserActivityOnLogin(user.getId());
        
        //3、记录登录状态
        request.getSession().setAttribute(USER_LOGIN_STATE,user);

        //4、将用户信息存入Redis缓存
        String cacheKey = "user:" + user.getId();
        redisTemplate.opsForValue().set(cacheKey, user, 30, TimeUnit.MINUTES);

        //5、脱敏返回
        return this.getLoginUserVO(user);
    }

    /**
     * 用户登录时更新活跃度
     * @param userId 用户ID
     */
    private void updateUserActivityOnLogin(Long userId) {
        // 先尝试更新现有记录
        UpdateWrapper<UserActivity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("userId", userId)
                .setSql("loginCount = loginCount + 1")
                .set("lastLoginTime", new Date())
                .set("lastActiveTime", new Date())
                .setSql("activityScore = activityScore + 10"); // 每次登录增加10分活跃度
        
        int updated = userActivityMapper.update(null, updateWrapper);
        
        // 如果没有更新任何记录，说明该用户还没有活跃度记录，需要插入一条
        if (updated == 0) {
            UserActivity userActivity = new UserActivity();
            userActivity.setUserId(userId);
            userActivity.setLoginCount(1);
            userActivity.setLastLoginTime(new Date());
            userActivity.setLastActiveTime(new Date());
            userActivity.setActivityScore(10);
            userActivityMapper.insert(userActivity);
        }
    }

    /**
     * 获取当前脱敏用户信息
     * @param user
     * @return
     */
    public UserLoginVO getLoginUserVO(User user) {
        if (user==null){
            return null;
        }
        UserLoginVO userLoginVO = new UserLoginVO();
        BeanUtils.copyProperties(user,userLoginVO);
        return userLoginVO;
    }


    /**
     * 获取当前登录用户
     * @param request
     * @return
     */
    @Override
    public UserLoginVO getLoginUser(HttpServletRequest request){
        if(request==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(),"请求参数错误");
        }
        //判断是否已经登录（基于Session）
        Object obj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) obj;
        if (user == null || user.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR.getCode(), "未登录");
        }

        // 先从Redis缓存获取
        String cacheKey = "user:" + user.getId();
        User cachedUser = (User) redisTemplate.opsForValue().get(cacheKey);
        if (cachedUser != null) {
            return getLoginUserVO(cachedUser);
        }

        //缓存中没有，再从数据查询
        user = this.getById(user.getId());
        if(user==null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR.getCode(),"未登录");
        }

        // 将用户信息存入缓存
        redisTemplate.opsForValue().set(cacheKey, user, 30, TimeUnit.MINUTES);
        return getLoginUserVO(user);
    }

    /**
     * 退出登录
     * @param request
     * @return
     */
    @Override
    public boolean logout(HttpServletRequest request) {
        if(request==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(),"请求参数错误");
        }
        //判断是否已经登录（基于Session）
        Object obj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User)obj;
        if(user==null || user.getId() == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR.getCode(),"未登录，无法退出登录");
        }
        // 清除Session中的登录状态
        String cacheKey = "user:" + user.getId();
        request.getSession().removeAttribute(USER_LOGIN_STATE);

        // 清除redis中缓存
        redisTemplate.delete(cacheKey);

        return true;
    }

    /**
     * 修改用户信息
     * @param userUpdateRequest
     * @param request
     * @return
     */
    @Override
    public UserUpdateVO updateUser(UserUpdateRequest userUpdateRequest, HttpServletRequest request) {
        if(request==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(),"请求参数错误");
        }
        
        // 获取现有用户信息
        User existingUser = this.getById(getLoginUser(request).getId());
        
        // 只更新有值的字段
        if (StringUtils.isNotBlank(userUpdateRequest.getUserName() )) {
            existingUser.setUserName(userUpdateRequest.getUserName());
        }
        // 修改密码更新逻辑，确保密码不为null且不为空字符串
        if (StringUtils.isNotBlank(userUpdateRequest.getUserPassword())) {
            // 加密密码
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userUpdateRequest.getUserPassword()).getBytes());
            existingUser.setUserPassword(encryptPassword);
        }
        if (userUpdateRequest.getGender() != null) {
            existingUser.setGender(userUpdateRequest.getGender());
        }
        if (StringUtils.isNotBlank(userUpdateRequest.getPhone())) {
            existingUser.setPhone(userUpdateRequest.getPhone());
        }
        if (StringUtils.isNotBlank(userUpdateRequest.getEmail())) {
            existingUser.setEmail(userUpdateRequest.getEmail());
        }
        if (StringUtils.isNotBlank(userUpdateRequest.getUserAvatar())) {
            existingUser.setUserAvatar(userUpdateRequest.getUserAvatar());
        }
        if (StringUtils.isNotBlank(userUpdateRequest.getUserProfile() )) {
            existingUser.setUserProfile(userUpdateRequest.getUserProfile());
        }

        // 更新数据库
        this.baseMapper.updateById(existingUser);
        
        // 修改redis缓存
        String cacheKey = "user:" + existingUser.getId();
        redisTemplate.opsForValue().set(cacheKey, existingUser, 30, TimeUnit.MINUTES);
        
        // 修改session
        request.getSession().setAttribute(USER_LOGIN_STATE,existingUser);
        log.info("修改成功: {}",existingUser);
        
        // 返回结果
        UserUpdateVO userUpdateVO = new UserUpdateVO();
        BeanUtils.copyProperties(existingUser,userUpdateVO);
        return userUpdateVO;
    }

    /**
     * 管理员根据ID获取用户完整信息
     * @param id 用户ID
     * @param request HTTP请求
     * @return 用户完整信息
     */
    @Override
    public User getUserById(long id, HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "请求参数错误");
        }
        
        // 获取当前登录用户
        UserLoginVO loginUser = this.getLoginUser(request);
        User user = this.baseMapper.selectById(loginUser.getId());
        
        // 检查权限：只有管理员可以获取完整用户信息
        if (user.getUserRole() != 1) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR.getCode(), "无权限查看用户完整信息");
        }
        
        // 查询指定ID的用户
        User targetUser = this.getById(id);
        if (targetUser == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR.getCode(), "用户不存在");
        }
        return targetUser;
    }

    /**
     * 普通用户根据ID获取脱敏用户信息
     * @param id 用户ID
     * @param request HTTP请求
     * @return 脱敏用户信息
     */
    @Override
    public UserLoginVO getPublicUserInfoById(long id, HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "请求参数错误");
        }
        
        // 获取当前登录用户
        UserLoginVO loginUser = this.getLoginUser(request);
        User user = this.baseMapper.selectById(loginUser.getId());
        
        // 检查用户是否登录
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR.getCode(), "未登录");
        }
        
        // 查询指定ID的用户
        User targetUser = this.getById(id);
        if (targetUser == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR.getCode(), "用户不存在");
        }
        
        // 返回脱敏信息
        return this.getLoginUserVO(targetUser);
    }


    /**
     * 分页查询用户列表
     * @param userQueryRequest 查询条件
     * @param request HTTP请求
     * @return 分页结果
     */
    @Override
    public PageResult<User> listUsersByPage(UserQueryRequest userQueryRequest, HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "请求参数错误");
        }

        // 验证分页参数
        if (userQueryRequest.getCurrent() <= 0) {
            userQueryRequest.setCurrent(1);
        }
        if (userQueryRequest.getPageSize() <= 0 || userQueryRequest.getPageSize() > 100) {
            userQueryRequest.setPageSize(10);
        }

        // 获取当前登录用户
        UserLoginVO loginUser = this.getLoginUser(request);
        User user = this.baseMapper.selectById(loginUser.getId());
        // 检查权限：只有管理员可以查询用户列表
        if (user.getUserRole() != 1) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR.getCode(), "无权限查询用户列表");
        }

        // 构造查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("isDelete", 0); // 只查询未删除的用户

        // 添加排序规则，确保分页结果的一致性
        queryWrapper.orderByDesc("createTime"); // 按创建时间倒序

        // 构建动态查询条件
        if (StringUtils.isNotBlank(userQueryRequest.getUserAccount())) {
            queryWrapper.like("userAccount", userQueryRequest.getUserAccount());
        }

        if (StringUtils.isNotBlank(userQueryRequest.getUserName())) {
            queryWrapper.like("userName", userQueryRequest.getUserName());
        }

        if (userQueryRequest.getGender() != null) {
            queryWrapper.eq("gender", userQueryRequest.getGender());
        }

        if (userQueryRequest.getUserStatus() != null) {
            queryWrapper.eq("userStatus", userQueryRequest.getUserStatus());
        }

        if (userQueryRequest.getUserRole() != null) {
            queryWrapper.eq("userRole", userQueryRequest.getUserRole());
        }

        // 分页查询
        Page<User> page = new Page<>(userQueryRequest.getCurrent(), userQueryRequest.getPageSize());
        this.page(page, queryWrapper);

        // 构造分页结果
        PageResult<User> pageResult = new PageResult<>();
        pageResult.setRecords(page.getRecords());
        pageResult.setTotal(page.getTotal());
        pageResult.setCurrent(page.getCurrent());
        pageResult.setSize(page.getSize());

        return pageResult;
    }


    /**
     * 更新用户状态（启用/禁用）
     * @param id 用户ID
     * @param status 用户状态 0-正常 1-禁用
     * @param request HTTP请求
     * @return 是否更新成功
     */
    @Override
    public boolean updateUserStatus(long id, int status, HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "请求参数错误");
        }
        
        // 获取当前登录用户
        UserLoginVO loginUser = this.getLoginUser(request);
        User currentUser = this.baseMapper.selectById(loginUser.getId());
        
        // 检查权限：只有管理员可以更新用户状态
        if (currentUser.getUserRole() != 1) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR.getCode(), "无权限更新用户状态");
        }
        
        // 不能更新自己的状态
        if (id == currentUser.getId()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "不能更新自己的状态");
        }
        
        // 查询目标用户
        User targetUser = this.getById(id);
        if (targetUser == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR.getCode(), "用户不存在");
        }
        
        // 更新状态
        targetUser.setUserStatus(status);
        boolean result = this.updateById(targetUser);
        
        // 更新缓存
        if (result) {
            String cacheKey = "user:" + id;
            redisTemplate.opsForValue().set(cacheKey, targetUser, 30, TimeUnit.MINUTES);
        }
        
        return result;
    }

    /**
     * 更新用户角色（普通用户/管理员）
     * @param id 用户ID
     * @param role 用户角色 0-普通用户 1-管理员
     * @param request HTTP请求
     * @return 是否更新成功
     */
    @Override
    public boolean updateUserRole(long id, int role, HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "请求参数错误");
        }
        
        // 获取当前登录用户
        UserLoginVO loginUser = this.getLoginUser(request);
        User currentUser = this.baseMapper.selectById(loginUser.getId());
        
        // 检查权限：只有管理员可以更新用户角色
        if (currentUser.getUserRole() != 1) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR.getCode(), "无权限更新用户角色");
        }
        
        // 不能更新自己的角色
        if (id == currentUser.getId()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "不能更新自己的角色");
        }
        
        // 查询目标用户
        User targetUser = this.getById(id);
        if (targetUser == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR.getCode(), "用户不存在");
        }
        
        // 更新角色
        targetUser.setUserRole(role);
        boolean result = this.updateById(targetUser);
        
        // 更新缓存
        if (result) {
            String cacheKey = "user:" + id;
            redisTemplate.opsForValue().set(cacheKey, targetUser, 30, TimeUnit.MINUTES);
        }
        
        return result;
    }

    /**
     * 逻辑删除用户
     * @param id 用户ID
     * @param request HTTP请求
     * @return 是否删除成功
     */
    @Override
    public boolean deleteUser(long id, HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "请求参数错误");
        }
        
        // 获取当前登录用户
        UserLoginVO loginUser = this.getLoginUser(request);
        User currentUser = this.baseMapper.selectById(loginUser.getId());

        // 检查权限：只有管理员可以删除用户
        if (currentUser.getUserRole() != 1) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR.getCode(), "无权限删除用户");
        }
        
        // 不能删除自己
        if (id == currentUser.getId()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "不能删除自己");
        }
        
        // 查询目标用户
        User targetUser = this.getById(id);
        if (targetUser == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR.getCode(), "用户不存在");
        }

        // 使用MyBatis-Plus的removeById方法执行逻辑删除
        boolean result = this.removeById(id);

        // 清除缓存
        if (result) {
            String cacheKey = "user:" + id;
            redisTemplate.delete(cacheKey);
        }
        
        return result;
    }


    /**
     * 修改密码
     * @param updateUserPsswordRequest
     * @param request
     * @return
     */
    @Override
    public Boolean updateUserPassword(UpdateUserPsswordRequest updateUserPsswordRequest, HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "请求参数错误");
        }

        // 获取当前登录用户
        UserLoginVO loginUser = this.getLoginUser(request);
        User currentUser = this.baseMapper.selectById(loginUser.getId());
        
        // 验证旧密码是否正确
        String oldPassword = DigestUtils.md5DigestAsHex((SALT + updateUserPsswordRequest.getOldPassword()).getBytes());
        if (!Objects.equals(currentUser.getUserPassword(), oldPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "旧密码不正确");
        }
        
        // 验证新密码和确认密码是否一致
        if (!Objects.equals(updateUserPsswordRequest.getUserPassword(), updateUserPsswordRequest.getConfirmPassword())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "两次输入的密码不一致");
        }
        
        // 校验新密码强度
        if (updateUserPsswordRequest.getUserPassword().length() < 8 || updateUserPsswordRequest.getUserPassword().length() > 30) {
            throw new BusinessException("用户密码不能小于8位，不能超过30位");
        }
        
        // 更新密码
        String newPassword = DigestUtils.md5DigestAsHex((SALT + updateUserPsswordRequest.getUserPassword()).getBytes());
        currentUser.setUserPassword(newPassword);
        
        // 更新数据库
        boolean updateResult = this.updateById(currentUser);
        if (!updateResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR.getCode(), "密码更新失败");
        }

        // 更新session
        request.getSession().setAttribute(USER_LOGIN_STATE, currentUser);

        // 更新Redis缓存信息
        String cacheKey = "user:" + currentUser.getId();
        redisTemplate.opsForValue().set(cacheKey, currentUser, 30, TimeUnit.MINUTES);
        return true;
    }

    /**
     * 管理员重置用户密码
     * @param id 用户ID
     * @param request HTTP请求
     * @return 是否更新成功
     */
    @Override
    public boolean resetPassword(long id, HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "请求参数错误");
        }

        // 获取当前登录用户
        UserLoginVO loginUser = this.getLoginUser(request);
        User currentUser = this.baseMapper.selectById(loginUser.getId());

        // 检查权限：只有管理员可以删除用户
        if (currentUser.getUserRole() != 1) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR.getCode(), "无权限重置用户密码");
        }

        // 查询目标用户
        User targetUser = this.getById(id);
        if (targetUser == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR.getCode(), "用户不存在");
        }

        String newPassword = DigestUtils.md5DigestAsHex((SALT + "12345678").getBytes());
        targetUser.setUserPassword(newPassword);

        this.updateById(targetUser);

        return true;
    }

    /**
     * 管理员修改用户信息
     */
    @Override
    public UserUpdateVO updateUserByAdmin(UserUpdateRequest userUpdateRequest, HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "请求参数错误");
        }

        // 获取当前登录用户
        UserLoginVO loginUser = this.getLoginUser(request);
        User currentUser = this.baseMapper.selectById(loginUser.getId());

        // 检查权限：只有管理员可以删除用户
        if (currentUser.getUserRole() != 1) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR.getCode(), "无权限修改用户信息");
        }

        // 查询目标用户
        User targetUser = this.getById(userUpdateRequest.getId());
        if (targetUser == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR.getCode(), "用户不存在");
        }

        // 只更新有值的字段
        if (StringUtils.isNotBlank(userUpdateRequest.getUserName() )) {
            targetUser.setUserName(userUpdateRequest.getUserName());
        }
        // 修改密码更新逻辑，确保密码不为null且不为空字符串
        if (StringUtils.isNotBlank(userUpdateRequest.getUserPassword())) {
            // 加密密码
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userUpdateRequest.getUserPassword()).getBytes());
            targetUser.setUserPassword(encryptPassword);
        }
        if (userUpdateRequest.getGender() != null) {
            targetUser.setGender(userUpdateRequest.getGender());
        }
        if (StringUtils.isNotBlank(userUpdateRequest.getPhone())) {
            targetUser.setPhone(userUpdateRequest.getPhone());
        }
        if (StringUtils.isNotBlank(userUpdateRequest.getEmail())) {
            targetUser.setEmail(userUpdateRequest.getEmail());
        }
        if (StringUtils.isNotBlank(userUpdateRequest.getUserAvatar())) {
            targetUser.setUserAvatar(userUpdateRequest.getUserAvatar());
        }
        if (StringUtils.isNotBlank(userUpdateRequest.getUserProfile() )) {
            targetUser.setUserProfile(userUpdateRequest.getUserProfile());
        }

        targetUser.setUpdateTime(new Date());
        // 更新数据库
        this.baseMapper.updateById(targetUser);

        // 返回结果
        UserUpdateVO userUpdateVO = new UserUpdateVO();
        BeanUtils.copyProperties(targetUser,userUpdateVO);
        return userUpdateVO;
    }

}