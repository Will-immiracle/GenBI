package com.will.bi.mapper;

import com.will.bi.model.pojo.Chart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author zhangzan
* @description 针对表【chart(图表信息表)】的数据库操作Mapper
* @createDate 2025-04-04 22:13:24
* @Entity generator.pojo.Chart
*/
public interface ChartMapper extends BaseMapper<Chart> {

    Boolean createDataTable(@Param("createSql") String createSql);

    Boolean insertChartData(@Param("insertSql") String insertSql);
}




