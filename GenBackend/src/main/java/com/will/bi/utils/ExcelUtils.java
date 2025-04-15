package com.will.bi.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.idev.excel.FastExcel;
import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.event.AnalysisEventListener;
import cn.idev.excel.support.ExcelTypeEnum;
import com.will.bi.common.ResultCodeEnum;
import com.will.bi.exception.utils.ThrowUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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

@Component
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

    public static List<String[]> excelToList(MultipartFile multipartFile) throws IOException {
        List<String[]> list = new ArrayList<>();
        Workbook sheets;
        InputStream inputStream;
        inputStream = multipartFile.getInputStream();
        sheets = new XSSFWorkbook(inputStream);
        Sheet sheet = sheets.getSheetAt(0);
        Row header = sheet.getRow(0);
        // 校验header是否为空
        ThrowUtils.throwIf(header == null, ResultCodeEnum.PARAMS_ERROR, "原始空值错误");
        // 获取表的大小，若考虑合并单元格，则遍历整表，获取列末索引+1的最大值
        int size = header.getLastCellNum() + 1;
        Row firstRow = sheet.getRow(1);
        // 只推断了一行数据，实际执行时，可能因为空值原因错误记录属性。可以通过遍历多行数据改进
        String type;
        for(int i = 0; i < size; i++){
            if(firstRow.getCell(i) != null){
                String[] map = new String[2];
                switch(firstRow.getCell(i).getCellType()){
                    case STRING :
                        type = "test";
                        break;
                    case NUMERIC :
                        type = "decimal(10,2)"; /// NUMERIC可能包含日期，暂时不处理
                        break;
                    case BOOLEAN :
                        type = "tinyint(1)";
                        break;
                    case BLANK :
                        type = "varchar(255)"; // 默认为varchar(255)，如遍历多行数据，此处跳过
                        break;
                    default:
                        type = "varchar(255)";
                        break;
                }
                map[0] = header.getCell(i).getStringCellValue();
                map[1] = type;
                list.add(map);
            }
        }
        sheets.close();
        inputStream.close();
        return list;
    }

    public static List<Map<Integer, String>> insertExcel(MultipartFile multipartFile) throws IOException {
        List<Map<Integer, String>> list;
        list = FastExcel.read(multipartFile.getInputStream())
                .excelType(ExcelTypeEnum.XLSX)
                .sheet()
                .headRowNumber(0)
                .doReadSync();
        return list;
    }

}
