package com.will.bi.model.enums;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ChartStatusEnum {
    WAITING(0, "waiting"),  //待执行
    SUCCESS(1, "succeed"),  //已执行
    EXECUTING(2, "executing"),  //执行中
    FAILED(3, "failed");  //已失败

    private final Integer value;
    private final String text;

    ChartStatusEnum(Integer value, String text) {
        this.value = value;
        this.text = text;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static ChartStatusEnum getEnumByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (ChartStatusEnum anEnum : ChartStatusEnum.values()) {
            if (anEnum.value == value) {
                return anEnum;
            }
        }
        return null;
    }

    public Integer getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
