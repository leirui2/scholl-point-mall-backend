package com.lei.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lei.mall.common.ErrorCode;
import com.lei.mall.common.PageResult;
import com.lei.mall.exception.BusinessException;
import com.lei.mall.mapper.UserMapper;
import com.lei.mall.model.entity.Item;
import com.lei.mall.model.entity.User;
import com.lei.mall.model.request.*;
import com.lei.mall.model.vo.ItemCategoryVO;
import com.lei.mall.model.vo.ItemVO;
import com.lei.mall.model.vo.UserLoginVO;
import com.lei.mall.service.ItemService;
import com.lei.mall.mapper.ItemMapper;
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
 * @description 针对表【item(商品信息表)】的数据库操作Service实现
 * @createDate 2025-11-22 17:00:14
 */
@Service
@Slf4j
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item>
        implements ItemService{


    @Resource
    private UserService userService;

    @Resource
    private UserMapper userMapper;

    /**
     * 添加商品信息
     * @param itemAddRequest 商品信息增加请求参数
     * @return 商品信息ID
     */
    @Override
    public long addItem(ItemAddRequest itemAddRequest,HttpServletRequest request) {

        //校验权限
        checkAdminPermission(request);

        //验证数据
        if (StringUtils.isAnyBlank(itemAddRequest.getName())) {
            throw new BusinessException("商品名称不能为空");
        }
        if ((itemAddRequest.getCategoryid() == null)) {
            throw new BusinessException("商品所属类别不能为空");
        }
        if (itemAddRequest.getPrice() == null) {
            throw new BusinessException("商品价格不能为空");
        }

        Item item = new Item();
        item.setName(itemAddRequest.getName());
        item.setCategoryid(itemAddRequest.getCategoryid());
        item.setPrice(itemAddRequest.getPrice());
        item.setOrder_count(0L);

        if (StringUtils.isNotBlank(itemAddRequest.getDescription())) {
            item.setDescription(itemAddRequest.getDescription());
        }
        if (StringUtils.isNotBlank(itemAddRequest.getImageurl())){
            item.setImageurl(itemAddRequest.getImageurl());
        }
        if (StringUtils.isNotBlank(itemAddRequest.getUnit())){
            item.setUnit(itemAddRequest.getUnit());
        }
        if (itemAddRequest.getStock() != null){
            item.setStock(itemAddRequest.getStock());
        }

        boolean saveResult = this.save(item);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR.getCode(), "添加失败，数据库错误");
        }
        return item.getId();
    }

    /**
     * 商品信息信息修改
     */
    @Override
    public Item updateItem(ItemUpdateRequest itemUpdateRequest, HttpServletRequest request) {
        //校验权限
        checkAdminPermission(request);
        //验证数据
        if (itemUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(),"请求参数异常。");
        }
        Item item = new Item();
        item.setId(itemUpdateRequest.getId());
        if (StringUtils.isNotBlank(itemUpdateRequest.getName())){
            item.setName(itemUpdateRequest.getName());
        }
        if (StringUtils.isNotBlank(itemUpdateRequest.getDescription())){
            item.setDescription(itemUpdateRequest.getDescription());
        }
        if (StringUtils.isNotBlank(itemUpdateRequest.getImageurl())){
            item.setImageurl(itemUpdateRequest.getImageurl());
        }
        if (StringUtils.isNotBlank(itemUpdateRequest.getUnit())){
            item.setUnit(itemUpdateRequest.getUnit());
        }
        if (itemUpdateRequest.getPrice() != null){
            item.setPrice(itemUpdateRequest.getPrice());
        }
        if (itemUpdateRequest.getStock() != null){
            item.setStock(itemUpdateRequest.getStock());
        }
        if (itemUpdateRequest.getCategoryid() != null){
            item.setCategoryid(itemUpdateRequest.getCategoryid());
        }
        int updateById = this.baseMapper.updateById(item);
        if (updateById == 0){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR.getCode(), "修改失败，异常错误");
        }
        return item;
    }

    /**
     * 管理员根据ID查询商品信息完整信息
     * @param id 商品信息ID
     * @param request HTTP请求
     * @return 商品信息完整信息
     */
    @Override
    public ItemCategoryVO getItemById(long id, HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "请求参数错误");
        }
        return this.baseMapper.selectVoById(id);
    }

    /**
     * 管理员更新商品信息状态（启用/禁用）
     * @param id 商品信息ID
     * @param status 商品信息状态 0-正常 1-禁用
     * @param request HTTP请求
     * @return 是否更新成功
     */
    @Override
    public boolean updateItemStatus(long id, int status, HttpServletRequest request) {
        //校验权限
        checkAdminPermission(request);
        Item item = new Item();
        item.setId(id);
        item.setStatus(status);
        int updateById = this.baseMapper.updateById(item);
        if (updateById == 0){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR.getCode(), "修改失败，异常错误");
        }
        return true;
    }

    /**
     * 管理员逻辑删除商品信息
     * @param id 商品信息ID
     * @param request HTTP请求
     * @return 是否删除成功
     */
    @Override
    public boolean deleteItem(long id, HttpServletRequest request) {
        //校验权限
        checkAdminPermission(request);

        Item item = this.getById(id);
        if (item == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR.getCode(), "类别不存在");
        }
        // 使用MyBatis-Plus的removeById方法执行逻辑删除
        return this.removeById(id);
    }

    /**
     * 管理员分页查询商品信息列表
     * @param itemQueryRequest 查询条件
     * @param request HTTP请求
     * @return 用户列表分页结果
     */
    @Override
    public PageResult<Item> listItemByPage(ItemQueryRequest itemQueryRequest, HttpServletRequest request) {
        //校验权限
        checkAdminPermission(request);

        // 验证分页参数
        if (itemQueryRequest.getCurrent() <= 0) {
            itemQueryRequest.setCurrent(1);
        }
        if (itemQueryRequest.getPageSize() <= 0 || itemQueryRequest.getPageSize() > 100) {
            itemQueryRequest.setPageSize(10);
        }
        // 构造查询条件
        QueryWrapper<Item> queryWrapper = new QueryWrapper<>();

        // 只查询未删除的类别
        queryWrapper.eq("isDelete", 0);
        queryWrapper.orderByDesc("createTime");
        if (StringUtils.isNotBlank(itemQueryRequest.getName())) {
            queryWrapper.like("name", itemQueryRequest.getName());
        }
        if (StringUtils.isNotBlank(itemQueryRequest.getDescription())){
            queryWrapper.like("description", itemQueryRequest.getDescription());
        }
        // 价格区间查询 - 只有当值大于0时才进行查询
        if (itemQueryRequest.getMinPrice() != null && itemQueryRequest.getMinPrice() > 0) {
            if (itemQueryRequest.getMaxPrice() != null && itemQueryRequest.getMaxPrice() > 0) {
                // 如果最小价格和最大价格都大于0，则查询价格在区间内的商品
                queryWrapper.between("price", itemQueryRequest.getMinPrice(), itemQueryRequest.getMaxPrice());
            } else {
                // 如果只有最小价格大于0，则查询价格大于等于最小价格的商品
                queryWrapper.ge("price", itemQueryRequest.getMinPrice());
            }
        } else if (itemQueryRequest.getMaxPrice() != null && itemQueryRequest.getMaxPrice() > 0) {
            // 如果只有最大价格大于0，则查询价格小于等于最大价格的商品
            queryWrapper.le("price", itemQueryRequest.getMaxPrice());
        }
        
        //库存区间查询 - 只有当值大于0时才进行查询
        if (itemQueryRequest.getMinStock() != null && itemQueryRequest.getMinStock() > 0) {
            if (itemQueryRequest.getMaxStock() != null && itemQueryRequest.getMaxStock() > 0) {
                queryWrapper.between("stock", itemQueryRequest.getMinStock(), itemQueryRequest.getMaxStock());
            } else {
                queryWrapper.ge("stock", itemQueryRequest.getMinStock());
            }
        } else if (itemQueryRequest.getMaxStock() != null && itemQueryRequest.getMaxStock() > 0) {
            queryWrapper.le("stock", itemQueryRequest.getMaxStock());
        }

        // 如果有类别ID 就查询
        queryWrapper.eq(itemQueryRequest.getCategoryid() != null && itemQueryRequest.getCategoryid() > 0, "categoryId", itemQueryRequest.getCategoryid());
        

        // 分页查询
        Page<Item> page = new Page<>(itemQueryRequest.getCurrent(), itemQueryRequest.getPageSize());
        this.page(page, queryWrapper);

        // 构造分页结果
        PageResult<Item> pageResult = new PageResult<>();
        pageResult.setRecords(page.getRecords());
        pageResult.setTotal(page.getTotal());
        pageResult.setCurrent(page.getCurrent());
        pageResult.setSize(page.getSize());

        return pageResult;
    }

    /**
     * 管理员分页查询商品和类别信息列表
     * @param itemQueryRequest 查询条件
     * @param request HTTP请求
     * @return 商品和类别信息列表分页结果
     */
    @Override
    public PageResult<ItemCategoryVO> listItemCategoryByPage(ItemQueryRequest itemQueryRequest, HttpServletRequest request) {
        //校验权限
        checkAdminPermission(request);

        // 验证分页参数
        if (itemQueryRequest.getCurrent() <= 0) {
            itemQueryRequest.setCurrent(1);
        }
        if (itemQueryRequest.getPageSize() <= 0 || itemQueryRequest.getPageSize() > 100) {
            itemQueryRequest.setPageSize(10);
        }

        // 分页查询
        Page<ItemCategoryVO> page = new Page<>(itemQueryRequest.getCurrent(), itemQueryRequest.getPageSize());
        IPage<ItemCategoryVO> resultPage = this.baseMapper.selectItemCategoryVoByPage(page, itemQueryRequest);

        // 构造分页结果
        PageResult<ItemCategoryVO> pageResult = new PageResult<>();
        pageResult.setRecords(resultPage.getRecords());
        pageResult.setTotal(resultPage.getTotal());
        pageResult.setCurrent(resultPage.getCurrent());
        pageResult.setSize(resultPage.getSize());

        return pageResult;
    }


    /**
     * 普通用户分页查询商品信息列表
     * @param itemQueryRequest 查询条件
     * @param request HTTP请求
     * @return 商品信息列表分页结果
     */
    @Override
    public PageResult<ItemVO> listItemByPageUser(ItemQueryRequest itemQueryRequest, HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "请求参数错误");
        }
        // 验证分页参数
        if (itemQueryRequest.getCurrent() <= 0) {
            itemQueryRequest.setCurrent(1);
        }
        if (itemQueryRequest.getPageSize() <= 0 || itemQueryRequest.getPageSize() > 100) {
            itemQueryRequest.setPageSize(10);
        }
        // 构造查询条件
        QueryWrapper<Item> queryWrapper = new QueryWrapper<>();

        // 只查询未删除的类别
        queryWrapper.eq("isDelete", 0);
        queryWrapper.orderByDesc("createTime");
        if (StringUtils.isNotBlank(itemQueryRequest.getName())) {
            queryWrapper.like("name", itemQueryRequest.getName());
        }
        if (StringUtils.isNotBlank(itemQueryRequest.getDescription())){
            queryWrapper.like("description", itemQueryRequest.getDescription());
        }
        // 价格区间查询 - 只有当值大于0时才进行查询
        if (itemQueryRequest.getMinPrice() != null && itemQueryRequest.getMinPrice() > 0) {
            if (itemQueryRequest.getMaxPrice() != null && itemQueryRequest.getMaxPrice() > 0) {
                // 如果最小价格和最大价格都大于0，则查询价格在区间内的商品
                queryWrapper.between("price", itemQueryRequest.getMinPrice(), itemQueryRequest.getMaxPrice());
            } else {
                // 如果只有最小价格大于0，则查询价格大于等于最小价格的商品
                queryWrapper.ge("price", itemQueryRequest.getMinPrice());
            }
        } else if (itemQueryRequest.getMaxPrice() != null && itemQueryRequest.getMaxPrice() > 0) {
            // 如果只有最大价格大于0，则查询价格小于等于最大价格的商品
            queryWrapper.le("price", itemQueryRequest.getMaxPrice());
        }

        //库存区间查询 - 只有当值大于0时才进行查询
        if (itemQueryRequest.getMinStock() != null && itemQueryRequest.getMinStock() > 0) {
            if (itemQueryRequest.getMaxStock() != null && itemQueryRequest.getMaxStock() > 0) {
                queryWrapper.between("stock", itemQueryRequest.getMinStock(), itemQueryRequest.getMaxStock());
            } else {
                queryWrapper.ge("stock", itemQueryRequest.getMinStock());
            }
        } else if (itemQueryRequest.getMaxStock() != null && itemQueryRequest.getMaxStock() > 0) {
            queryWrapper.le("stock", itemQueryRequest.getMaxStock());
        }

        // 如果有类别ID 就查询
        queryWrapper.eq(itemQueryRequest.getCategoryid() != null && itemQueryRequest.getCategoryid() > 0, "categoryId", itemQueryRequest.getCategoryid());

        // 分页查询
        Page<Item> page = new Page<>(itemQueryRequest.getCurrent(), itemQueryRequest.getPageSize());
        this.page(page, queryWrapper);

        // 构造分页结果
        PageResult<ItemVO> pageResult = new PageResult<>();
        // 转换实体对象为 VO 对象
        List<ItemVO> voList = new ArrayList<>();
        for (Item item : page.getRecords()) {
            ItemVO vo = new ItemVO();
            BeanUtils.copyProperties(item, vo);
            voList.add(vo);
        }

        pageResult.setRecords(voList);
        pageResult.setTotal(page.getTotal());
        pageResult.setCurrent(page.getCurrent());
        pageResult.setSize(page.getSize());

        return pageResult;
    }

    /**
     * 获取热门商品列表推荐（前N名）
     * @param hotItemQueryRequest 查询条件
     * @param request HTTP请求
     * @return 商品信息列表分页结果
     */
    @Override
    public PageResult<ItemVO> hotListItemByPage(HotItemQueryRequest hotItemQueryRequest, HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "请求参数错误");
        }
        // 验证分页参数
        if (hotItemQueryRequest.getCurrent() <= 0) {
            hotItemQueryRequest.setCurrent(1);
        }
        if (hotItemQueryRequest.getPageSize() <= 0 || hotItemQueryRequest.getPageSize() > 100) {
            hotItemQueryRequest.setPageSize(6);
        }
        // 构造查询条件
        QueryWrapper<Item> queryWrapper = new QueryWrapper<>();

        // 只查询未删除的商品
        queryWrapper.eq("isDelete", 0);
        // 按销量降序排列（order_count字段）
        queryWrapper.orderByDesc("order_count");

        // 分页查询
        Page<Item> page = new Page<>(hotItemQueryRequest.getCurrent(), hotItemQueryRequest.getPageSize());
        this.page(page, queryWrapper);

        // 如果设置了num参数，则只取前num个商品
        List<Item> records = page.getRecords();
        if (hotItemQueryRequest.getNum() != null && hotItemQueryRequest.getNum() > 0 
                && hotItemQueryRequest.getNum() < records.size()) {
            records = records.subList(0, hotItemQueryRequest.getNum().intValue());
            page.setRecords(records);
        }

        // 构造分页结果
        PageResult<ItemVO> pageResult = new PageResult<>();
        // 转换实体对象为 VO 对象
        List<ItemVO> voList = new ArrayList<>();
        for (Item item : page.getRecords()) {
            ItemVO vo = new ItemVO();
            BeanUtils.copyProperties(item, vo);
            voList.add(vo);
        }

        pageResult.setRecords(voList);
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