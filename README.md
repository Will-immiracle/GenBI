# Gen-BI Framework

## 项目简介
    可视化图表生成与数据分析平台。区别于传统BI，用户导入原始数据集、并输入分析诉求，即可生成可视化图表及分析结论.

### 预览
<div align=center>
    <img src="https://github.com/Will-immiracle/GenBI/blob/main/images/login.png" width="800" height="430">
</div>
<div/>
<div align=center>
    <img src="https://github.com/Will-immiracle/GenBI/blob/main/images/page1.png" width="800" height="430">
</div>
<div align=center>
    <img src="https://github.com/Will-immiracle/GenBI/blob/main/images/page3.png" width="800" height="430">
</div>

## 核心功能
### Gen-BI 网站的基本功能：
- [x] 用户输入分析需求、原始数据。得到**数据图表、分析结论**。
- [x] 查看历史**所有**可视化图表、结论。附带**关键词**查询功能。
- [ ]  原始数据存入服务器数据库并**建表**。用户可以查看、编辑原始数据。



### 流程图
<div align=center>
    <img src="https://github.com/Will-immiracle/GenBI/blob/main/images/流程图.drawio.svg" width="530" height="460">
</div>
  
### 扩展优化：
- 分库分表
  - apache poi库详见： https://blog.51cto.com/u_16175466/12885984
- 限流服务
  - redisson令牌桶限流
- 系统异步化
- 分布式消息队列

### 扩展流程图
<div align=center>
    <img src="https://github.com/Will-immiracle/GenBI/blob/main/images/异步流程图1.drawio.svg" width="530" height="460">
</div>
  
---
  
## 模块解析

### 智能分析业务开发
### 模块一、 可视化图表与分析结论
**业务场景**

**业务流程**
1. 用户输入
- a. 分析目标
- b. 上传原始数据（excel）
- c. 更精细化地控制图表：比如图表类型、图表名称等
2. 后端校验
- a. 校验用户的输入是否合法（比如长度）
- b. 成本控制（次数统计和校验、鉴权等）
3. 把处理好的数据输入给AI模型（调用AI接口），让AI模型给我们提供图表信息、结论文本
4. 图表信息、结论文本在前端进行展示
### 模块二、 历史图表展示与分页查询

### 模块三、 原始数据分库分表
**测试百万级数据下单表的插入、查询性能**
1. 创建测试表
```sql
use test_db;
DROP TABLE IF EXISTS `chart_test`;
CREATE TABLE `chart_test` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `goal` text COLLATE utf8mb4_unicode_ci COMMENT '分析目标',
  `chart_data` text COLLATE utf8mb4_unicode_ci COMMENT '图表数据',
  `chart_type` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '图表类型',
  `gen_chart` text COLLATE utf8mb4_unicode_ci COMMENT '生成的图表数据',
  `gen_result` text COLLATE utf8mb4_unicode_ci COMMENT '生成的分析结果',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  `user_id` bigint NOT NULL COMMENT '创建表用户的id',
  `chart_name` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT 'default',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '0-待执行，1-已执行，2-执行中，3-已失败',
  `info` text COLLATE utf8mb4_unicode_ci COMMENT '执行信息',
  PRIMARY KEY (`id`,`update_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1913062471690395651 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='图表信息表';

```
2.1. 测试插入性能（300万条）
```sql
use test_db;

DROP PROCEDURE if EXISTS BatchInsert;
delimiter $$
CREATE PROCEDURE BatchInsert(IN initId INT, IN loop_counts INT)
BEGIN
    DECLARE Var INT;
    DECLARE ID INT;
    SET Var = 0;
    SET ID = initId;
    set autocommit=0; -- 关闭自动提交事务，提高插入效率
    WHILE Var < loop_counts DO
        INSERT INTO `chart_test` (`goal`, `chart_data`, `chart_type`, `gen_chart`, `gen_result`, `create_time`, `update_time`, `is_delete`, `user_id`, `chart_name`, `status`, `info`)
        VALUES ('生成用户饼图', NULL, '饼图', '\n{\n    \"title\": {\n        \"text\": \"每日人数分布\",\n        \"subtext\": \"数据来源：示例数据\",\n        \"left\": \"center\"\n    },\n    \"tooltip\": {\n        \"trigger\": \"item\"\n    },\n    \"legend\": {\n        \"orient\": \"vertical\",\n        \"left\": \"left\"\n    },\n    \"series\": [\n        {\n            \"name\": \"人数\",\n            \"type\": \"pie\",\n            \"radius\": \"50%\",\n            \"data\": [\n                { \"value\": 10, \"name\": \"1日\" },\n                { \"value\": 20, \"name\": \"2日\" },\n                { \"value\": 90, \"name\": \"3日\" },\n                { \"value\": 59, \"name\": \"4日\" },\n                { \"value\": 50, \"name\": \"5日\" },\n                { \"value\": 60, \"name\": \"6日\" }\n            ],\n            \"emphasis\": {\n                \"itemStyle\": {\n                    \"shadowBlur\": 10,\n                    \"shadowOffsetX\": 0,\n                    \"shadowColor\": \"rgba(0, 0, 0, 0.5)\"\n                }\n            }\n        }\n    ]\n}\n', '\n\n分析结论: 根据提供的数据，生成了一个饼图来展示每日的人数分布。从图表中可以看出，3日的人数最多，达到了90人，占据了总人数的最大比例。其次是6日和5日，人数分别为60人和50人。1日的人数最少，只有10人。通过这个饼图，可以直观地看出每日人数的相对多少，有助于快速了解数据的分布情况。', '2025-04-16 19:52:30', '2025-04-17 09:25:04', 0, 1910305532111773697, '饼图', 1, NULL);
        SET ID = ID + 1;
        SET Var = Var + 1;
    END WHILE;
    COMMIT;
END$$;

delimiter ;  -- 界定符复原为默认的分号
CALL BatchInsert(1, 5000000);  -- 调用存储过程

```
2.2. 测试结果  




### 模块四、 系统异步化
**业务场景**

**业务流程**
1. 用户操作耗时很长时，点击提交后，把任务保存到数据库中记录下来 
2. 要执行新任务时：
- a. 提交成功：

    -  i 有多余线程，立即去做这个任务 
    -  ii 所有线程都忙，就放到等待队列里  
- b. 提交失败（线程都忙，任务队列已满 ）：

    -  i 拒绝任务，不再执行 
    -  ii 通过数据库记录提交失败的任务，在程序闲的时候，再去执行 
3. 从任务队列中取出任务依次执行，每完成一件任务修改一次任务执行状态。 
4. 用户可以查询任务的执行状态，或在执行结束时得到通知（邮件、提示音、短信），从而优化体验 
5. 如果任务非常复杂，则子任务完成时，即刻更新状态

**线程池**  
  
在Java中，可以使用JUC并发包中的ThreadPoolExecutor，来实现灵活的自定义线程池。  
```
public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue) {
```
怎么确定线程池参数呢？结合实际情况（实际业务场景和系统资源）来测试调整，不断优化。  

回归到我们的业务，首先考虑系统最脆弱的环节（瓶颈）在哪里？  
腾讯混元模型默认在文本生成场景下允许5个线程去执行。


  



## 项目结构
````

````

### 开源协议
本项目采用 MIT 协议开源，详见 [LICENSE](LICENSE) 文件。 
