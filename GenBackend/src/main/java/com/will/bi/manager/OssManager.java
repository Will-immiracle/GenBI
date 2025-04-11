package com.will.bi.manager;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.will.bi.config.OssClientConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @program: GenBI
 * @description: Cos 对象存储操作
 * @author: Mr.Zhang
 * @create: 2025-04-10 22:42
 **/

@Component
public class OssManager {

    @Autowired
    private OssClientConfig ossClientConfig;

    @Autowired
    private OSS ossClient;

    /**
     * 上传对象
     *
     * @param key 唯一键
     * @param localFilePath 本地文件路径
     * @return
     */
    public PutObjectResult putObject(String key, String localFilePath) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(ossClientConfig.getBucketName(), key,
                new File(localFilePath));
        return ossClient.putObject(putObjectRequest);
    }

    /**
     * 上传对象
     *
     * @param key 唯一键
     * @param file 文件
     * @return
     */
    public PutObjectResult putObject(String key, File file) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(ossClientConfig.getBucketName(), key,
                file);
        return ossClient.putObject(putObjectRequest);
    }

}
