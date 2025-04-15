package com.will.bi.manager;

import com.tencentcloudapi.common.AbstractModel;
import com.tencentcloudapi.common.SSEResponseModel;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.hunyuan.v20230901.HunyuanClient;
import com.tencentcloudapi.hunyuan.v20230901.models.ChatCompletionsRequest;
import com.tencentcloudapi.hunyuan.v20230901.models.ChatCompletionsResponse;
import com.tencentcloudapi.hunyuan.v20230901.models.Content;
import com.tencentcloudapi.hunyuan.v20230901.models.Message;
import com.will.bi.common.ResultCodeEnum;
import com.will.bi.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: GenBI
 * @description: AI 调用接口类
 * @author: Mr.Zhang
 * @create: 2025-04-11 23:20
 **/

@Service
public class AiManager {

    @Autowired
    private HunyuanClient hunyuanclient;

    final String prompt = "你的职责是数据分析师和前端分析专家，接下来我会按照以下固定格式给你提供内容:\n"+
            "分析需求: \n"+
            "{数据分析的需求或者目标,以及可能提到json代码生成图表的类型}\n"+
            "原始数据: \n"+
            "{csv格式的原始数据，用,作为分隔符} \n"+
            "请根据这两部分内容，按照以下格式生成内容（此外不要输出任何开头、结尾、注释）\n"+
            "$$$\n"+
            "{前端 Echarts V5 的 option配置对象json代码，合理地对数据进行可视化，一定要生成用户定义的图表类型，不要生成任何多余的内容}\n"+
            "$$$\n"+
            "{明确的分析结论，越详细越好，不要生成注释}";

    public String doChat(String message) {
        ChatCompletionsRequest req = new ChatCompletionsRequest();
        req.setModel("hunyuan-vision");

        Message[] messages1 = new Message[1];
        Message message1 = new Message();
        message1.setRole("user");

        Content[] contents1 = new Content[1];
        Content content1 = new Content();
        content1.setText(prompt + message);
        contents1[0] = content1;

        message1.setContents(contents1);

        messages1[0] = message1;

        req.setMessages(messages1);

        // 返回的resp是一个ChatCompletionsResponse的实例，与请求对象对应
        ChatCompletionsResponse resp = null;
        try {
            resp = hunyuanclient.ChatCompletions(req);
        } catch (TencentCloudSDKException e) {
            throw new RuntimeException(e);
        }
        // 输出json格式的字符串回包
        return AbstractModel.toJsonString(resp);
    }

}
