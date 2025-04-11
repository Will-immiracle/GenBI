package com.will.bi.controller;


import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


import com.will.bi.annotation.AuthCheck;
import com.will.bi.common.Result;
import com.will.bi.common.ResultCodeEnum;
import com.will.bi.constant.FileConstant;
import com.will.bi.constant.UserConstant;
import com.will.bi.exception.BusinessException;
import com.will.bi.exception.utils.ThrowUtils;
import com.will.bi.manager.OssManager;
import com.will.bi.model.dto.chart.ChartAddRequest;
import com.will.bi.model.dto.chart.ChartEditRequest;
import com.will.bi.model.dto.chart.ChartQueryRequest;
import com.will.bi.model.dto.chart.ChartUpdateRequest;
import com.will.bi.model.dto.chart.ChartDeleteRequest;
import com.will.bi.model.dto.chart.GenChatByAiRequest;
import com.will.bi.model.enums.FileUploadBizEnum;
import com.will.bi.model.pojo.Chart;
import com.will.bi.model.pojo.User;
import com.will.bi.service.ChartService;
import com.will.bi.service.UserService;
import com.will.bi.utils.ExcelUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;

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

    @Autowired
    private OssManager ossManager;

    /**
     * ai分析生成图表
     *
     * @param multipartFile 数据
     * @param genChatByAiRequest 请求参数
     * @param request 请求对话
     * @return
     *
     * 1. 校验参数
     * 2. 数据预处理
     * 3. 调用AI接口
     * 4. 封装返回结果
     */
    @PostMapping("/gen")
    public Result<String> genChatByAi(@RequestPart("file") MultipartFile multipartFile,
                                      GenChatByAiRequest genChatByAiRequest, HttpServletRequest request) {
        // 1. 参数校验
        String chatType = genChatByAiRequest.getChatType();
        String chartName = genChatByAiRequest.getChartName();
        String goal = genChatByAiRequest.getGoal();
        ThrowUtils.throwIf(StringUtils.isBlank(goal),ResultCodeEnum.PARAMS_ERROR, "请输入分析目标");
        ThrowUtils.throwIf(StringUtils.isNotBlank(chartName) && chartName.length() > 80,ResultCodeEnum.PARAMS_ERROR, "名称过长");
        // 2. 数据预处理
        StringBuilder userInput = new StringBuilder();
        userInput.append("你的职责是数据分析师，接下来我会给你我的分析目标和原始数据，请告诉我分析结论。").append("\n");
        userInput.append("分析目标:").append(goal).append("\n");
        String csv = ExcelUtils.excelToCsv(multipartFile);
        userInput.append("数据:").append(csv).append("\n");
        return Result.ok(userInput.toString());


        User loginUser = userService.getLoginUser(request);
        // 文件目录：根据业务、用户来划分
        String uuid = RandomStringUtils.randomAlphanumeric(8);
        String filename = uuid + "-" + multipartFile.getOriginalFilename();
        String filepath = String.format("/%s/%s/%s", fileUploadBizEnum.getValue(), loginUser.getId(), filename);
        File file = null;
        try {
            // 上传文件
            file = File.createTempFile(filepath, null);
            multipartFile.transferTo(file);
            ossManager.putObject(filepath, file);
            // 返回可访问地址
            return Result.ok(FileConstant.COS_HOST + filepath);
        } catch (Exception e) {
            log.error("file upload error, filepath = " + filepath, e);
            throw new BusinessException(ResultCodeEnum.SYSTEM_ERROR, "上传失败");
        } finally {
            if (file != null) {
                // 删除临时文件
                boolean delete = file.delete();
                if (!delete) {
                    log.error("file delete error, filepath = {}", filepath);
                }
            }
        }
    }

    

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
