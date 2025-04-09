package com.zz.bi.model.dto.chart;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: GenBI
 * @description: 图表信息创建请求
 * @author: Mr.Zhang
 * @create: 2025-04-04 22:44
 **/
@Data
public class ChartAddRequest implements Serializable {

    /**
    * 分析目标
    */
    private String goal;

    /**
    * 图标数据
    */
    private String chartData;

    /**
    * 图标类型
    */
    private String chartType;

    private static final long serialVersionUID = 1L;
}