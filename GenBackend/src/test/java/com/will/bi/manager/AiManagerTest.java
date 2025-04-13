package com.will.bi.manager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AiManagerTest {
    @Autowired
    private AiManager aiManager;

    @Test
    void test(){
        String message = "分析需求：用户增长趋势\n原始数据：\n表头，天，人数\n1，10\n2，20\n3，30";
        String message0 = "$$$\n// 前端 Echarts V5 的 option配置对象js代码\nvar option = {\n    baseOption: {\n        timeline: {\n            axisType: 'category',\n            autoPlay: true,\n            playInterval: 1000,\n            data: ['日线', '周线', '月线', '分钟线'],\n            controlStyle: {\n                showNextBtn: true,\n                showPrevBtn: true\n            }\n        },\n        series: [{\n            type: 'candlestick',\n            data: []\n        }]\n    },\n    options: [{\n        title: { text: '日线数据' },\n        series: [{\n            data: [\n                [new Date('2020-07-20').getTime(), 9.51, 9.71, 9.76, 9.46],\n                [new Date('2020-07-21').getTime(), 9.78, 9.73, 9.8, 9.59],\n                [new Date('2020-07-22').getTime(), 9.74, 9.81, 9.94, 9.69],\n                [new Date('2020-07-23').getTime(), 9.78, 9.63, 9.78, 9.4]\n            ]\n        }]\n    }, {\n        title: { text: '周线数据' },\n        series: [{\n            data: [\n                [new Date('2020-07-20').getTime(), 9.51, 9.71, 9.76, 9.46],\n                // 周线数据需要更多天聚合，这里简化处理\n            ]\n        }]\n    }, {\n        title: { text: '月线数据' },\n        series: [{\n            data: [\n                [new Date('2020-07-20').getTime(), 9.51, 9.71, 9.76, 9.46],\n                // 月线数据需要更多天聚合，这里简化处理\n            ]\n        }]\n    }, {\n        title: { text: '分钟线数据' },\n        series: [{\n            data: [\n                [new Date('2020-07-20T09:30:00').getTime(), 9.51, 9.55, 9.58, 9.50],\n                // 分钟线数据需要更多分钟聚合，这里简化处理\n            ]\n        }]\n    }]\n};\n$$$\n\n数据分析结论:\n1. **日线数据**：从提供的数据来看，股票在2020年7月20日至2020年7月23日期间，整体呈现上涨趋势。开盘价从9.51元逐渐上升至9.78元，收盘价也从9.71元上升至9.63元（尽管在最后一天有所回落）。最高价达到9.94元，最低价为9.4元，显示出一定的波动性。\n\n2. **周线数据**：由于提供的数据天数较少，无法准确反映周线趋势。通常情况下，周线数据需要更多天的聚合来观察长期趋势。\n\n3. **月线数据**：同样由于数据天数有限，月线数据也无法准确反映长期趋势。月线数据通常用于观察一个月内的整体表现和趋势。\n\n4. **分钟线数据**：提供的数据中没有具体的分钟线数据，但可以推测，分钟线数据会显示出更频繁的价格波动。分钟线数据通常用于短期交易决策，观察短时间内的价格变化和交易量。\n\n总体而言，日线数据提供了短期内的价格波动和趋势，而周线和月线数据则更适合观察长期趋势。分钟线数据则用于更短期的交易决策。由于提供的数据量有限，无法全面反映各个时间尺度上的数据影响。";
        String[] split = message0.split("\\$\\$\\$");
        System.out.println(split[1]);
        System.out.println(split[2]);
    }
}