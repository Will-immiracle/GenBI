package com.will.bi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.will.bi.common.ResultCodeEnum;
import com.will.bi.constant.CommonConstant;
import com.will.bi.exception.BusinessException;
import com.will.bi.exception.utils.ThrowUtils;
import com.will.bi.model.dto.chart.ChartQueryRequest;
import com.will.bi.model.pojo.Chart;
import com.will.bi.service.ChartService;
import com.will.bi.mapper.ChartMapper;
import com.will.bi.utils.ExcelUtils;
import com.will.bi.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
* @author zhangzan
* @description 针对表【chart(图表信息表)】的数据库操作Service实现
* @createDate 2025-04-04 22:13:24
*/
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart>
    implements ChartService {

    @Autowired
    ChartMapper chartMapper;

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
        queryWrapper.eq(userId != null, "user_id", userId);
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.like(StringUtils.isNotBlank(chartName), "chart_name", chartName);
        queryWrapper.eq(StringUtils.isNotBlank(goal), "goal", goal);
        queryWrapper.eq(StringUtils.isNotBlank(chartType), "chart_type", chartType);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public Boolean createDataTable(MultipartFile multipartFile, Long chartId) throws IOException {

        List<String[]> maps;
        maps = ExcelUtils.excelToList(multipartFile);
        String colum_content = "";
        for(int i = 0; i < maps.size(); i++){
            String[] map = maps.get(i);
            colum_content += map[0];
            colum_content += " ";
            colum_content += map[1];
            if(i == maps.size() - 1){
                colum_content += ")";
                break;
            }
            colum_content += ",";
        }
        String createSql = "CREATE TABLE IF NOT EXISTS chart_"+chartId+" (";
        createSql += colum_content;
        chartMapper.createDataTable(createSql);

        List<Map<Integer, String>> data = ExcelUtils.insertExcel(multipartFile);
         String insertData = "";
        for(int i = 1; i < data.size();i++){
            String insertSql = "INSERT INTO chart_"+chartId+" VALUES (";
            LinkedHashMap<Integer, String> integerStringMap = (LinkedHashMap)data.get(i);
            for(int j = 0; j < integerStringMap.size(); j++){
                insertSql += "'" + integerStringMap.get(j) + "'";
                if(j == integerStringMap.size() - 1){
                    insertSql += ")";
                    break;
                }
                insertSql += ",";
            }
            insertData += insertSql;
            if(i == data.size()-1){
                break;
            }
            insertData += ";\n";
        }
        chartMapper.insertChartData(insertData);

        return true;
    }

}




