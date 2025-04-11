package com.will.bi.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.idev.excel.FastExcel;
import cn.idev.excel.support.ExcelTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: GenBI
 * @description: Excel工具类
 * @author: Mr.Zhang
 * @create: 2025-04-11 09:39
 **/

@Slf4j
public class ExcelUtils {
    public static String excelToCsv(MultipartFile multipartFile){
        List<Map<Integer, String>> list;
        try {
            list = FastExcel.read(multipartFile.getInputStream())
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet()
                    .headRowNumber(0)
                    .doReadSync();
        } catch (IOException e) {
            log.error("excelToCsv error",e);
            throw new RuntimeException("excelToCsv error");
        }
        if(CollUtil.isEmpty(list)){
            return "";
        }
        // 返回CSV文件
        StringBuilder stringBuilder = new StringBuilder();
        // 读取表头
        LinkedHashMap<Integer,String> headerMap = (LinkedHashMap)list.get(0);
        List<String> headerList = headerMap.values().stream().filter(ObjectUtil::isNotEmpty).collect(Collectors.toList());
        stringBuilder.append(StringUtils.join(headerList,",")).append("\n");
        // 读取数据
        for (int i = 1; i < list.size(); i++) {
            LinkedHashMap<Integer,String> dataMap = (LinkedHashMap)list.get(i);
            List<String> dataList = dataMap.values().stream().filter(ObjectUtil::isNotEmpty).collect(Collectors.toList());
            stringBuilder.append(StringUtils.join(dataList,",")).append("\n");
        }
        return stringBuilder.toString();
    }
}
