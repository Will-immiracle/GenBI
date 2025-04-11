package com.will.bi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.will.bi.common.ResultCodeEnum;
import com.will.bi.constant.CommonConstant;
import com.will.bi.exception.BusinessException;
import com.will.bi.model.dto.chart.ChartQueryRequest;
import com.will.bi.model.pojo.Chart;
import com.will.bi.service.ChartService;
import com.will.bi.mapper.ChartMapper;
import com.will.bi.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* @author zhangzan
* @description 针对表【chart(图表信息表)】的数据库操作Service实现
* @createDate 2025-04-04 22:13:24
*/
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart>
    implements ChartService {

    public QueryWrapper<Chart> getQueryWrapper(ChartQueryRequest chartQueryRequest) {
        if (chartQueryRequest == null) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR, "请求参数为空");
        }
        Long userId = chartQueryRequest.getUserId();
        Long id = chartQueryRequest.getId();
        String goal = chartQueryRequest.getGoal();
        String chartName = chartQueryRequest.getChartName();
        String chartType = chartQueryRequest.getChartType();
        String sortOrder = chartQueryRequest.getSortOrder();
        String sortField = chartQueryRequest.getSortField();
        QueryWrapper<Chart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(userId != null, "userId", userId);
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.like(StringUtils.isNotBlank(chartName), "chartName", chartName);
        queryWrapper.eq(StringUtils.isNotBlank(goal), "goal", goal);
        queryWrapper.eq(StringUtils.isNotBlank(chartType), "chartType", chartType);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

}




