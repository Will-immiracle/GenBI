package com.zz.bi.model.pojo;

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

    private String goal;

    private String chartData;

    private String chartType;

    private String genChart;

    private String genResult;

    private Date createTime;

    private Date updateTime;

    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}