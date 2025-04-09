package com.will.bi.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


import com.will.bi.annotation.AuthCheck;
import com.will.bi.common.Result;
import com.will.bi.common.ResultCodeEnum;
import com.will.bi.constant.UserConstant;
import com.will.bi.exception.BusinessException;
import com.will.bi.exception.utils.ThrowUtils;
import com.will.bi.model.dto.chart.ChartAddRequest;
import com.will.bi.model.dto.chart.ChartEditRequest;
import com.will.bi.model.dto.chart.ChartQueryRequest;
import com.will.bi.model.dto.chart.ChartUpdateRequest;
import com.will.bi.model.dto.chart.ChartDeleteRequest;
import com.will.bi.model.pojo.Chart;
import com.will.bi.model.pojo.User;
import com.will.bi.service.ChartService;
import com.will.bi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: GenBI
 * @description: ChartController类
 * @author: Mr.Zhang
 * @create: 2025-04-04 22:43
 **/
@RestController
@RequestMapping("/chart")
@Slf4j
public class ChartController {

    @Autowired
    private ChartService chartService;

    @Autowired
    private UserService userService;

    /**
     * 创建图标信息表
     *
     * 1. 检验参数
     * 2. 记录建表信息
     */
    @PostMapping("/add")
    public Result<Long> addChart(@RequestBody ChartAddRequest chartAddRequest, HttpServletRequest request) {
        if (chartAddRequest == null) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR);
        }
        Chart chart = new Chart();
        BeanUtils.copyProperties(chartAddRequest, chart);
        User loginUser = userService.getLoginUser(request);
        chart.setUserId(loginUser.getId());
        boolean result = chartService.save(chart);
        ThrowUtils.throwIf(!result, ResultCodeEnum.OPERATION_ERROR);
        long newChartId = chart.getId();
        return Result.ok(newChartId);
    }

    /**
     * 删除信息图表
     *
     * 1. 参数校验
     */
    @PostMapping("/delete")
    public Result<Boolean> deleteChart(@RequestBody ChartDeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Chart oldChart = chartService.getById(id);
        ThrowUtils.throwIf(oldChart == null, ResultCodeEnum.PARAMS_ERROR,"信息表不存在");
        // 仅本人或管理员可以删除
        if (!oldChart.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ResultCodeEnum.ROLE_ERROR,"仅本人或管理员可以删除");
        }
        boolean b = chartService.removeById(id);
        return Result.ok(b);
    }

    /**
     * 更新（仅管理员）
     *
     * 1. 参数校验
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result<Boolean> updateChart(@RequestBody ChartUpdateRequest chartUpdateRequest) {
        if (chartUpdateRequest == null || chartUpdateRequest.getId() <= 0) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR);
        }
        Chart Chart = new Chart();
        BeanUtils.copyProperties(chartUpdateRequest, Chart);
        long id = chartUpdateRequest.getId();
        // 判断是否存在
        Chart oldChart = chartService.getById(id);
        ThrowUtils.throwIf(oldChart == null, ResultCodeEnum.PARAMS_ERROR,"信息表不存在");
        boolean result = chartService.updateById(Chart);
        return Result.ok(result);
    }

    /**
     * 根据 id 获取
     *
     * 1. 校验参数
     */ 
    @GetMapping("/get")
    public Result<Chart> getChartVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR);
        }
        Chart chart = chartService.getById(id);
        if (chart == null) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR,"信息表不存在");
        }
        return Result.ok(chart);
    }

    /**
     * 分页获取列表（仅管理员）
     *
     */
    @PostMapping("/list/admin/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result<Page<Chart>> listChartByPage(@RequestBody ChartQueryRequest ChartQueryRequest) {
        long current = ChartQueryRequest.getCurrent();
        long size = ChartQueryRequest.getPageSize();
        Page<Chart> chartPage = chartService.page(new Page<>(current, size),
                chartService.getQueryWrapper(ChartQueryRequest));
        return Result.ok(chartPage);
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param request
     */
    @PostMapping("/list/page")
    public Result<Page<Chart>> listChartByPage(@RequestBody ChartQueryRequest chartQueryRequest,
                                                       HttpServletRequest request) {
        long current = chartQueryRequest.getCurrent();
        long size = chartQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ResultCodeEnum.PARAMS_ERROR);
        Page<Chart> chartPage = chartService.page(new Page<>(current, size),
                chartService.getQueryWrapper(chartQueryRequest));
        return Result.ok(chartPage);
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param ChartQueryRequest
     * @param request
     */
    @PostMapping("/my/list/page")
    public Result<Page<Chart>> listMyChartByPage(@RequestBody ChartQueryRequest ChartQueryRequest,
                                                         HttpServletRequest request) {
        if (ChartQueryRequest == null) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        ChartQueryRequest.setUserId(loginUser.getId());
        long current = ChartQueryRequest.getCurrent();
        long size = ChartQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ResultCodeEnum.PARAMS_ERROR);
        Page<Chart> chartPage = chartService.page(new Page<>(current, size),
                chartService.getQueryWrapper(ChartQueryRequest));
        return Result.ok(chartPage);
    }

    /**
     * 编辑（用户）
     *
     * @param ChartEditRequest
     * @param request
     */
    @PostMapping("/edit")
    public Result<Boolean> editChart(@RequestBody ChartEditRequest ChartEditRequest, HttpServletRequest request) {
        if (ChartEditRequest == null || ChartEditRequest.getId() <= 0) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR);
        }
        Chart Chart = new Chart();
        BeanUtils.copyProperties(ChartEditRequest, Chart);
        User loginUser = userService.getLoginUser(request);
        long id = ChartEditRequest.getId();
        // 判断是否存在
        Chart oldChart = chartService.getById(id);
        ThrowUtils.throwIf(oldChart == null, ResultCodeEnum.NOT_FOUND);
        // 仅本人或管理员可编辑
        if (!oldChart.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ResultCodeEnum.ROLE_ERROR);
        }
        boolean result = chartService.updateById(Chart);
        return Result.ok(result);
    }

}
