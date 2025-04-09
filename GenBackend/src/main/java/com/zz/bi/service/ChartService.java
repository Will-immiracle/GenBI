package com.zz.bi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zz.bi.model.dto.chart.ChartQueryRequest;
import com.zz.bi.model.pojo.Chart;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author zhangzan
* @description 针对表【chart(图表信息表)】的数据库操作Service
* @createDate 2025-04-04 22:13:24
*/
public interface ChartService extends IService<Chart> {
    QueryWrapper<Chart> getQueryWrapper(ChartQueryRequest chartQueryRequest);

}
