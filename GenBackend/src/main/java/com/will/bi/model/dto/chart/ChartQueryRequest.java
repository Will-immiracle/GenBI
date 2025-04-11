package com.will.bi.model.dto.chart;

import com.will.bi.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @program: GenBI
 * @description: 图表信息查询请求
 * @author: Mr.Zhang
 * @create: 2025-04-04 22:44
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class ChartQueryRequest extends PageRequest implements Serializable {

    /**
    * 建表用户id
    */
    private Long userId;

    /**
     * 图表名称
     */
    private String chartName;

    /**
    * 图表id
    */
    private Long id;

    /**
    * 分析目标
    */
    private String goal;

    /**
    * 图表类型
    */
    private String chartType;

    private static final long serialVersionUID = 1L;
}