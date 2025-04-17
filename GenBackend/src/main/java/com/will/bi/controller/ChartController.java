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
import com.will.bi.exception.utils.UpdateChartFailed;
import com.will.bi.manager.AiManager;
import com.will.bi.manager.OssManager;
import com.will.bi.model.dto.chart.ChartAddRequest;
import com.will.bi.model.dto.chart.ChartEditRequest;
import com.will.bi.model.dto.chart.ChartQueryRequest;
import com.will.bi.model.dto.chart.ChartUpdateRequest;
import com.will.bi.model.dto.chart.ChartDeleteRequest;
import com.will.bi.model.dto.chart.GenChatByAiRequest;
import com.will.bi.model.enums.ChartStatusEnum;
import com.will.bi.model.enums.FileUploadBizEnum;
import com.will.bi.model.pojo.Chart;
import com.will.bi.model.pojo.User;
import com.will.bi.model.vo.bi.BiResponse;
import com.will.bi.service.ChartService;
import com.will.bi.service.UserService;
import com.will.bi.utils.ExcelUtils;
import com.will.bi.utils.JsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

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
    private AiManager aiManager;

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * ai分析生成图表 (同步)
     *
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
    public Result<BiResponse> genChatByAi(@RequestPart("file") MultipartFile multipartFile,
                                          GenChatByAiRequest genChatByAiRequest, HttpServletRequest request){
        // 1. 参数校验
        String chartType = genChatByAiRequest.getChartType();
        String chartName = genChatByAiRequest.getChartName();
        String goal = genChatByAiRequest.getGoal();
        ThrowUtils.throwIf(StringUtils.isBlank(goal),ResultCodeEnum.PARAMS_ERROR, "请输入分析目标");
        ThrowUtils.throwIf(StringUtils.isNotBlank(chartName) && chartName.length() > 80,ResultCodeEnum.PARAMS_ERROR, "名称过长");
        // 文件大小不超过 10M
        Long MAX_FILE_SIZE = 10 * 1024 * 1024 * 1024L;
        long size = multipartFile.getSize();
        ThrowUtils.throwIf(size > MAX_FILE_SIZE, ResultCodeEnum.PARAMS_ERROR, "文件超出10M");
        // 文件格式规定为.xlsx
        String originalFilename = multipartFile.getOriginalFilename();
        String suffix = FileUtil.getSuffix(originalFilename);
        List<String> validFileSuffixList = Arrays.asList("xlsx", "xls");
        ThrowUtils.throwIf(!validFileSuffixList.contains(suffix),ResultCodeEnum.PARAMS_ERROR, "文件后缀非法");
        // 2. 数据预处理
        StringBuilder userInput = new StringBuilder();
        userInput.append("分析目标:").append(goal).append(", ");
        if(StringUtils.isNotBlank(chartType)){
            userInput.append("请使用").append(chartType).append("进行数据展示.").append("\n");
        }
        String csv = null;
        try {
            csv = ExcelUtils.excelToCsv(multipartFile);
        } catch (IOException e) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR, "文件转换错误");
        }
        userInput.append("数据:").append(csv).append("\n");
        String json = aiManager.doChat(userInput.toString());
        JsonUtils jsonUtils = new JsonUtils();
        String content = jsonUtils.getContent(json);
        // 3. 格式化返回数据
        String[] split = content.split("\\$\\$\\$");
        if(split.length < 3) throw new BusinessException(ResultCodeEnum.PARAMS_ERROR, "AI生成错误");
        String genChart = split[1];
        String genConclusion = split[2];
        // 4. 持久化到数据库
        Chart chart = new Chart();
        chart.setChartType(chartType);
        chart.setChartName(chartName);
        chart.setGoal(goal);
        chart.setGenChart(genChart);
        chart.setGenResult(genConclusion);
        chart.setUserId(userService.getLoginUser(request).getId());
        boolean save = chartService.save(chart);
        try {
            chartService.createDataTable(multipartFile,chart.getId());
        } catch (IOException e) {
            throw new BusinessException(ResultCodeEnum.OPERATION_ERROR,"表数据保存失败");
        }
        ThrowUtils.throwIf(!save, ResultCodeEnum.OPERATION_ERROR,"表信息保存失败");
        BiResponse biResponse = new BiResponse();
        biResponse.setGenChart(genChart);
        biResponse.setGenConclusion(genConclusion);
        biResponse.setChartId(chart.getId());
        return Result.ok(biResponse);
    }

    /**
     * ai分析生成图表 (异步)
     *
     * @param genChatByAiRequest 请求参数
     * @param request 请求对话
     * @return
     *
     * 1. 校验参数
     * 2. 数据预处理
     * 3. 调用AI接口
     * 4. 封装返回结果
     */
    @PostMapping("/gen/async")
    public Result<BiResponse> genChatByAiAsync(@RequestPart("file") MultipartFile multipartFile,
            GenChatByAiRequest genChatByAiRequest, HttpServletRequest request){
        // 1. 参数校验
        String chartType = genChatByAiRequest.getChartType();
        String chartName = genChatByAiRequest.getChartName();
        String goal = genChatByAiRequest.getGoal();
        ThrowUtils.throwIf(StringUtils.isBlank(goal),ResultCodeEnum.PARAMS_ERROR, "请输入分析目标");
        ThrowUtils.throwIf(StringUtils.isNotBlank(chartName) && chartName.length() > 80,ResultCodeEnum.PARAMS_ERROR, "名称过长");
        // 文件大小不超过 10M
        Long MAX_FILE_SIZE = 10 * 1024 * 1024 * 1024L;
        long size = multipartFile.getSize();
        ThrowUtils.throwIf(size > MAX_FILE_SIZE, ResultCodeEnum.PARAMS_ERROR, "文件超出10M");
        // 文件格式规定为.xlsx
        String originalFilename = multipartFile.getOriginalFilename();
        String suffix = FileUtil.getSuffix(originalFilename);
        List<String> validFileSuffixList = Arrays.asList("xlsx", "xls");
        ThrowUtils.throwIf(!validFileSuffixList.contains(suffix),ResultCodeEnum.PARAMS_ERROR, "文件后缀非法");
        // 3. 提交任务，持久化到数据库
        Chart chart = new Chart();
        chart.setStatus(ChartStatusEnum.WAITING.getValue());
        chart.setChartType(chartType);
        chart.setChartName(chartName);
        chart.setGoal(goal);
        chart.setUserId(userService.getLoginUser(request).getId());
        chartService.save(chart);
        // 2. 数据预处理
        StringBuilder userInput = new StringBuilder();
        userInput.append("分析目标:").append(goal).append(", ");
        if(StringUtils.isNotBlank(chartType)){
            userInput.append("请使用").append(chartType).append("进行数据展示.").append("\n");
        }
        String csv = "";
        try(InputStream inputStream = multipartFile.getInputStream()) {
            chartService.createDataTable(multipartFile, chart.getId());
            csv = ExcelUtils.excelToCsv(multipartFile);
        } catch (IOException e) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR, "数据创建表错误");
        }
        userInput.append("数据:").append(csv).append("\n");
        // 4. 调用业务，更新执行状态
        CompletableFuture.runAsync(() -> {
            // 4.0 更新执行状态
            Chart updateChart = new Chart();
            updateChart.setId(chart.getId());
            updateChart.setStatus(ChartStatusEnum.EXECUTING.getValue());
            chartService.updateById(updateChart);
            // 4.1 调用AiManager处理业务
            String json = aiManager.doChat(userInput.toString());
            JsonUtils jsonUtils = new JsonUtils();
            String content = jsonUtils.getContent(json);
            // 4.2. 格式化返回数据
            String[] split = content.split("\\$\\$\\$");
            if(split.length < 3){
                // 4.3.1 更新失败状态
                String errorInfo = "AI生成错误";
                UpdateChartFailed.updateChartFailed(chart.getId(), errorInfo, chartService);
                throw new BusinessException(ResultCodeEnum.PARAMS_ERROR, errorInfo);
            }
            String genChart = split[1];
            String genConclusion = split[2];
            // 4.3.2 更新成功状态
            Chart updateSuccessChart = new Chart();
            updateSuccessChart.setId(chart.getId());
            updateSuccessChart.setStatus(ChartStatusEnum.SUCCESS.getValue());
            updateSuccessChart.setGenChart(genChart);
            updateSuccessChart.setGenResult(genConclusion);

            // 4.3.3 处理错误
            boolean saveChart = chartService.updateById(updateSuccessChart);
            ThrowUtils.throwIf(!saveChart, ResultCodeEnum.OPERATION_ERROR, "信息提交错误");
        }, threadPoolExecutor);

        Chart successChart = chartService.getById(chart.getId());
        BiResponse biResponse = new BiResponse();
        biResponse.setGenChart(successChart.getGenChart());
        biResponse.setGenConclusion(successChart.getGenResult());
        biResponse.setChartId(successChart.getId());
        return Result.ok(biResponse);
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
     * @param request
     */
    @PostMapping("/my/list/page")
    public Result<Page<Chart>> listMyChartByPage(@RequestBody ChartQueryRequest chartQueryRequest,
                                                         HttpServletRequest request) {
        if (chartQueryRequest == null) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        chartQueryRequest.setUserId(loginUser.getId());
        long current = chartQueryRequest.getCurrent();
        long size = chartQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ResultCodeEnum.PARAMS_ERROR);
        Page<Chart> chartPage = chartService.page(new Page<>(current, size),
                chartService.getQueryWrapper(chartQueryRequest));
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
