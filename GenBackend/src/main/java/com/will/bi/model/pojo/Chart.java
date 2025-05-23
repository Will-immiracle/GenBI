package com.will.bi.model.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import lombok.Data;

/**
 * @TableName chart
 */
@TableName(value ="chart")
@Data
public class Chart {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long userId;

    private String chartName;

    private String goal;

    private String chartData;

    private String chartType;

    private Date createTime;

    private Date updateTime;

    private Integer status;

    private String info;

    private String genChart;

    private String genResult;

    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}