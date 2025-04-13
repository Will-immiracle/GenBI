package com.will.bi.utils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * @program: GenBI
 * @description: Json文件转换工具类
 * @author: Mr.Zhang
 * @create: 2025-04-12 12:53
 **/

@Slf4j
public class JsonUtils {
    public String getContent(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(json);
            String content = root.at("/Choices/0/Message/Content").asText();
            return content;
        } catch (Exception e) {
            log.error("解析失败：" + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
