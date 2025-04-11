package com.will.bi.api;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.tencentcloudapi.common.AbstractModel;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.SSEResponseModel;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.hunyuan.v20230901.HunyuanClient;
import com.tencentcloudapi.hunyuan.v20230901.models.ChatCompletionsRequest;
import com.tencentcloudapi.hunyuan.v20230901.models.ChatCompletionsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: GenBI
 * @description: OpenAi Api接口
 * @author: Mr.Zhang
 * @create: 2025-04-11 20:06
 **/

@ConfigurationProperties(prefix = "tencent-cloud.services.hunyuan")
@Configuration
public class OpenAiApi {

    private String secretId;

    private String secretKey;

    private String endpoint;

    private String apiKey;

    private String baseUrl;

    public String useApi(){
        Map<String,Object> hashMap = new HashMap<>();
        hashMap.put("message","用户传入消息");
        String json = JSONUtil.toJsonStr(hashMap);
        String result = HttpRequest.post(baseUrl)
                .header("Authorization", "Bearer " + apiKey)
                .body(json)
                .execute()
                .body();
        return result;
    }

    public static void main(String [] args) {
        try{
            // 实例化一个认证对象，入参需要传入腾讯云账户 SecretId 和 SecretKey，此处还需注意密钥对的保密
            // 代码泄露可能会导致 SecretId 和 SecretKey 泄露，并威胁账号下所有资源的安全性
            // 以下代码示例仅供参考，建议采用更安全的方式来使用密钥
            // 请参见：https://cloud.tencent.com/document/product/1278/85305
            // 密钥可前往官网控制台 https://console.cloud.tencent.com/cam/capi 进行获取
            Credential cred = new Credential("SecretId", "SecretKey");
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("hunyuan.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            HunyuanClient client = new HunyuanClient(cred, "", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            ChatCompletionsRequest req = new ChatCompletionsRequest();

            // 返回的resp是一个ChatCompletionsResponse的实例，与请求对象对应
            ChatCompletionsResponse resp = client.ChatCompletions(req);
            // 输出json格式的字符串回包
            if (resp.isStream()) { // 流式响应
                for (SSEResponseModel.SSE e : resp) {
                    System.out.println(e.Data);
                }
            } else { // 非流式响应
                System.out.println(AbstractModel.toJsonString(resp));
            }
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
    }

}
