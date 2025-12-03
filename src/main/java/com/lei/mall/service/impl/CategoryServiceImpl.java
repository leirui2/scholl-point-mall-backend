package com.lei.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lei.mall.common.ErrorCode;
import com.lei.mall.common.PageResult;
import com.lei.mall.exception.BusinessException;
import com.lei.mall.mapper.UserMapper;
import com.lei.mall.model.entity.Category;
import com.lei.mall.model.entity.Item;
import com.lei.mall.model.entity.User;
import com.lei.mall.model.request.CategoryAddRequest;
import com.lei.mall.model.request.CategoryQueryRequest;
import com.lei.mall.model.request.CategoryUpdateRequest;
import com.lei.mall.model.request.HotCategoryQueryRequest;
import com.lei.mall.model.vo.CategoryVO;
import com.lei.mall.model.vo.ItemVO;
import com.lei.mall.model.vo.UserLoginVO;
import com.lei.mall.service.CategoryService;
import com.lei.mall.mapper.CategoryMapper;
import com.lei.mall.service.ItemService;
import com.lei.mall.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

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

    @Resource
    private ItemService itemService;

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

        //判断是否有同样名称
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",category.getName());
        long cnt = this.count(queryWrapper);
        if(cnt > 0){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR.getCode(), "添加失败，已有该名称的商品类别！");
        }

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

        QueryWrapper<Item> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("categoryId",id);
        if (itemService.count(queryWrapper) > 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR.getCode(), "该类别下有商品，请先删除商品");
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
     * 热门商品类别列表
     * @param hotCategoryQueryRequest 查询条件
     * @param request HTTP请求
     * @return 用户列表分页结果
     */
    @Override
    public PageResult<CategoryVO> hotCategoryByPage(HotCategoryQueryRequest hotCategoryQueryRequest, HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "请求参数错误");
        }
        // 验证分页参数
        if (hotCategoryQueryRequest.getCurrent() <= 0) {
            hotCategoryQueryRequest.setCurrent(1);
        }
        if (hotCategoryQueryRequest.getPageSize() <= 0 || hotCategoryQueryRequest.getPageSize() > 100) {
            hotCategoryQueryRequest.setPageSize(6);
        }
        // 使用自定义SQL查询，按类别内商品销量排序
        Page<CategoryVO> page = new Page<>(hotCategoryQueryRequest.getCurrent(), hotCategoryQueryRequest.getPageSize());
        IPage<CategoryVO> resultPage = this.baseMapper.selectHotCategoryByPage(page, hotCategoryQueryRequest);

        // 构造分页结果
        PageResult<CategoryVO> pageResult = new PageResult<>();
        pageResult.setRecords(resultPage.getRecords());
        pageResult.setTotal(resultPage.getTotal());
        pageResult.setCurrent(resultPage.getCurrent());
        pageResult.setSize(resultPage.getSize());

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




