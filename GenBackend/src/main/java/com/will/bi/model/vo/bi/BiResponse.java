package com.will.bi.model.vo.bi;

import lombok.Data;

/**
 * @program: GenBI
 * @description: Ai接口生成数据的格式化接收Vo
 * @author: Mr.Zhang
 * @create: 2025-04-12 10:19
 **/

@Data
public class BiResponse {
    private String genChart;
    private String genConclusion;
    private Long chartId;
}
