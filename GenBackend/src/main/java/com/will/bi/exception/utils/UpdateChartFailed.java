package com.will.bi.exception.utils;

import com.will.bi.model.enums.ChartStatusEnum;
import com.will.bi.model.pojo.Chart;
import com.will.bi.service.ChartService;

/**
 * @program: GenBI
 * @description: 更新 “已失败” 状态
 * @author: Mr.Zhang
 * @create: 2025-04-17 11:32
 **/

public class UpdateChartFailed {
    public static void updateChartFailed(Long chartId, String info, ChartService chartService) {
        Chart updateFailedChart = new Chart();
        updateFailedChart.setId(chartId);
        updateFailedChart.setStatus(ChartStatusEnum.FAILED.getValue());
        updateFailedChart.setInfo(chartId + info);
        chartService.updateById(updateFailedChart);
    }
    public static void updateChartFailed(Long chartId, ChartService chartService) {
        Chart updateFailedChart = new Chart();
        updateFailedChart.setId(chartId);
        updateFailedChart.setStatus(ChartStatusEnum.FAILED.getValue());
        updateFailedChart.setInfo(chartId + "执行错误");
        chartService.updateById(updateFailedChart);
    }
}
