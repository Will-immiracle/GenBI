package com.will.bi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.will.bi.model.dto.chart.ChartQueryRequest;
import com.will.bi.model.pojo.Chart;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
* @author zhangzan
* @description 针对表【chart(图表信息表)】的数据库操作Service
* @createDate 2025-04-04 22:13:24
*/
public interface ChartService extends IService<Chart> {
    QueryWrapper<Chart> getQueryWrapper(ChartQueryRequest chartQueryRequest);

    Boolean createDataTable(MultipartFile multipartFile, Long chartId) throws IOException;
    
}
