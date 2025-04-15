package com.will.bi.model.dto.chart;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: GenBI
 * @description: 文件上传请求
 * @author: Mr.Zhang
 * @create: 2025-04-10 22:31
 **/

@Data
public class GenChatByAiRequest implements Serializable {

    /**
     * 图表名称
     */
    private String chartName;

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
