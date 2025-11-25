package com.lei.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lei.mall.common.ErrorCode;
import com.lei.mall.common.PageResult;
import com.lei.mall.exception.BusinessException;
import com.lei.mall.mapper.UserMapper;
import com.lei.mall.model.entity.Category;
import com.lei.mall.model.entity.User;
import com.lei.mall.model.request.CategoryAddRequest;
import com.lei.mall.model.request.CategoryQueryRequest;
import com.lei.mall.model.request.CategoryUpdateRequest;
import com.lei.mall.model.vo.UserLoginVO;
import com.lei.mall.service.CategoryService;
import com.lei.mall.mapper.CategoryMapper;
import com.lei.mall.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
* @author lei
* @description 针对表【category(商品类别表)】的数据库操作Service实现
* @createDate 2025-11-22 17:00:14
*/
@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{


    @Resource
    private UserService userService;


    @Resource
    private UserMapper userMapper;

    /**
     * 添加商品类别
     * @param categoryAddRequest 商品类别增加请求参数
     * @return 商品类别ID
     */
    @Override
    public long addCategory(CategoryAddRequest categoryAddRequest,HttpServletRequest request) {

        //校验权限
        checkAdminPermission(request);

        //验证数据
        if (StringUtils.isAnyBlank(categoryAddRequest.getName())) {
            throw new BusinessException("商品类别名称不能为空");
        }

        Category category = new Category();
        category.setName(categoryAddRequest.getName());
        category.setDescription(categoryAddRequest.getDescription());

        boolean saveResult = this.save(category);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR.getCode(), "添加失败，数据库错误");
        }
        return category.getId();
    }

    /**
     * 商品类别信息修改
     */
    @Override
    public Category updateCategory(CategoryUpdateRequest categoryUpdateRequest, HttpServletRequest request) {
        //校验权限
        checkAdminPermission(request);
        //验证数据
        if (StringUtils.isAnyBlank(categoryUpdateRequest.getName(),categoryUpdateRequest.getId().toString())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(),"请求参数异常。");
        }
        Category category = new Category();
        category.setId(categoryUpdateRequest.getId());
        category.setName(categoryUpdateRequest.getName());
        if (StringUtils.isNotBlank(categoryUpdateRequest.getDescription())){
            category.setDescription(categoryUpdateRequest.getDescription());
        }
        int updateById = this.baseMapper.updateById(category);
        if (updateById == 0){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR.getCode(), "修改失败，异常错误");
        }
        return category;
    }

    /**
     * 管理员根据ID查询商品类别完整信息
     * @param id 商品类别ID
     * @param request HTTP请求
     * @return 商品类别完整信息
     */
    @Override
    public Category getCategoryById(long id, HttpServletRequest request) {
        //校验权限
        checkAdminPermission(request);
        return this.baseMapper.selectById(id);
    }

    /**
     * 管理员更新商品类别状态（启用/禁用）
     * @param id 商品类别ID
     * @param status 商品类别状态 0-正常 1-禁用
     * @param request HTTP请求
     * @return 是否更新成功
     */
    @Override
    public boolean updateCategoryStatus(long id, int status, HttpServletRequest request) {
        //校验权限
        checkAdminPermission(request);
        Category category = new Category();
        category.setId(id);
        category.setStatus(status);
        int updateById = this.baseMapper.updateById(category);
        if (updateById == 0){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR.getCode(), "修改失败，异常错误");
        }
        return true;
    }

    /**
     * 管理员逻辑删除商品类别
     * @param id 商品类别ID
     * @param request HTTP请求
     * @return 是否删除成功
     */
    @Override
    public boolean deleteCategory(long id, HttpServletRequest request) {
        //校验权限
        checkAdminPermission(request);

        Category category = this.getById(id);
        if (category == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR.getCode(), "类别不存在");
        }
        // 使用MyBatis-Plus的removeById方法执行逻辑删除
        return this.removeById(id);
    }

    /**
     * 管理员分页查询商品类别列表
     * @param categoryQueryRequest 查询条件
     * @param request HTTP请求
     * @return 用户列表分页结果
     */
    @Override
    public PageResult<Category> listCategoryByPage(CategoryQueryRequest categoryQueryRequest, HttpServletRequest request) {
        //校验权限
        checkAdminPermission(request);

        // 验证分页参数
        if (categoryQueryRequest.getCurrent() <= 0) {
            categoryQueryRequest.setCurrent(1);
        }
        if (categoryQueryRequest.getPageSize() <= 0 || categoryQueryRequest.getPageSize() > 100) {
            categoryQueryRequest.setPageSize(10);
        }
        // 构造查询条件
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();

        // 只查询未删除的类别
        queryWrapper.eq("isDelete", 0);

        queryWrapper.orderByDesc("createTime");

        // 构建动态查询条件
        if (StringUtils.isNotBlank(categoryQueryRequest.getName())) {
            queryWrapper.like("name", categoryQueryRequest.getName());
        }

        if (categoryQueryRequest.getStatus() != null) {
            queryWrapper.eq("status", categoryQueryRequest.getStatus());
        }

        // 分页查询
        Page<Category> page = new Page<>(categoryQueryRequest.getCurrent(), categoryQueryRequest.getPageSize());
        this.page(page, queryWrapper);

        // 构造分页结果
        PageResult<Category> pageResult = new PageResult<>();
        pageResult.setRecords(page.getRecords());
        pageResult.setTotal(page.getTotal());
        pageResult.setCurrent(page.getCurrent());
        pageResult.setSize(page.getSize());

        return pageResult;
    }


    /**
     * 检查用户是否有管理员权限
     *
     * @param request HTTP请求
     */
    private void checkAdminPermission(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "请求参数错误");
        }

        // 获取当前登录用户
        UserLoginVO loginUser = userService.getLoginUser(request);
        User user = userMapper.selectById(loginUser.getId());

        // 检查权限：只有管理员可以获取完整用户信息
        if (user.getUserRole() != 1) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR.getCode(), "无权限");
        }

    }
}




