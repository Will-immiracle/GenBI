package com.will.bi.model.dto.chart;

import java.io.Serializable;
import lombok.Data;

/**
 * @program: UserCenter
 * @description: 删除请求
 * @author: Mr.Zhang
 * @create: 2025-03-26 22:25
 **/
@Data
public class ChartDeleteRequest implements Serializable {

    /**
     * 图表名称
     */
    private String chartName;

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}