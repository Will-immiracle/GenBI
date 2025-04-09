package com.zz.bi.model.dto.chart;

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
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}