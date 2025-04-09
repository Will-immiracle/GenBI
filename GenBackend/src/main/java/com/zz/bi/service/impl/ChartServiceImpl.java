package com.zz.bi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zz.bi.common.ResultCodeEnum;
import com.zz.bi.constant.CommonConstant;
import com.zz.bi.exception.BusinessException;
import com.zz.bi.model.dto.chart.ChartQueryRequest;
import com.zz.bi.model.pojo.Chart;
import com.zz.bi.service.ChartService;
import com.zz.bi.mapper.ChartMapper;
import com.zz.bi.utils.SqlUtils;
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
        String chartType = chartQueryRequest.getChartType();
        String sortOrder = chartQueryRequest.getSortOrder();
        String sortField = chartQueryRequest.getSortField();
        QueryWrapper<Chart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(userId != null, "userId", userId);
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(StringUtils.isNotBlank(goal), "goal", goal);
        queryWrapper.eq(StringUtils.isNotBlank(chartType), "chartType", chartType);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

}




