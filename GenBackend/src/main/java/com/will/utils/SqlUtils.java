package com.will.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @program: GenBI
 * @description: SQL 工具
 * @author: Mr.Zhang
 * @create: 2025-04-04 22:43
 **/

public class SqlUtils {

    /**
     * 校验排序字段是否合法（防止 SQL 注入）
     *
     * @param sortField
     * @return
     */
    public static boolean validSortField(String sortField) {
        if (StringUtils.isEmpty(sortField)) {
            return false;
        }
        return !StringUtils.containsAny(sortField, "=", "(", ")", " ");
    }
}
