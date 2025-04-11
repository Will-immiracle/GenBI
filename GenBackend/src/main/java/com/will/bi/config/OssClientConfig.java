package com.will.bi.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: GenBI
 * @description: 阿里云对象存储客户端
 * @author: Mr.Zhang
 * @create: 2025-04-10 23:00
 **/

@Configuration
@ConfigurationProperties(prefix = "ali-cloud.services.oss")
@Data
public class OssClientConfig {

    /**
    * 域名地址
    * */
    private String endpoint;

    /**
     * accessKey
     * */
    private String accessKeyId;

    /**
     * secretKey
     * */
    private String accessKeySecret;

    /**
     * 桶名
     * */
    private String bucketName;

    @Bean
    public OSS ossClient() {
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }

}
