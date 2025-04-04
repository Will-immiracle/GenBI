package com.will.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User {
    private Long id;

    private String useraccount;

    private String userpassword;

    private String username;

    private String useravatar;

    private String userrole;

    private Date createtime;

    private Date updatetime;

    private Integer isdelete;
}