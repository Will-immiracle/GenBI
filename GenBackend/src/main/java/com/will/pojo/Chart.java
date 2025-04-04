package com.will.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName chart
 */
@TableName(value ="chart")
@Data
public class Chart {
    private Long id;

    private String goal;

    private String chartdata;

    private String charttype;

    private String genchart;

    private String genresult;

    private Date createtime;

    private Date updatetime;

    private Integer isdelete;
}