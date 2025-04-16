package com.will.bi.api;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.hunyuan.v20230901.HunyuanClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: GenBI
 * @description: Ai配置类
 * @author: Mr.Zhang
 * @create: 2025-04-11 23:40
 **/

@ConfigurationProperties(prefix = "tencent-cloud.services.hunyuan")
@Configuration
@Data
public class OpenAiApi {

    private String secretId;

    private String secretKey;

    private String endpoint;

    @Bean
    public HunyuanClient hunyuanClient(){
        Credential cred = new Credential(secretId, secretKey);
        HttpProfile httpProfile = new HttpProfile();
        // 配置域名地址，默认域名地址为 https://hunyuan.tencentcloudapi.com
        httpProfile.setEndpoint(endpoint);
        // 实例化一个client选项，可选的，没有特殊需求可以跳过
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        // 实例化要请求产品的client对象,clientProfile是可选的
        HunyuanClient hunyuanclient = new HunyuanClient(cred, "", clientProfile);
        return hunyuanclient;
    }
}
