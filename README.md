# Gen-BI Framework

## 项目简介
    可视化图表生成与数据分析平台。区别于传统BI，用户导入原始数据集、并输入分析诉求，即可生成可视化图表及分析结论.

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
1.1. 测试插入性能（300万条）
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
        VALUES ('生成用户饼图', NULL, '饼图', '\n{\n    \"title\": {\n        \"text\": \"每日人数分布\",\n        \"subtext\": \"数据来源：示例数据\",\n        \"left\": \"center\"\n    },\n    \"tooltip\": {\n        \"trigger\": \"item\"\n    },\n    \"legend\": {\n        \"orient\": \"vertical\",\n        \"left\": \"left\"\n    },\n    \"series\": [\n        {\n            \"name\": \"人数\",\n            \"type\": \"pie\",\n            \"radius\": \"50%\",\n            \"data\": [\n                { \"value\": 10, \"name\": \"1日\" },\n                { \"value\": 20, \"name\": \"2日\" },\n                { \"value\": 90, \"name\": \"3日\" },\n                { \"value\": 59, \"name\": \"4日\" },\n                { \"value\": 50, \"name\": \"5日\" },\n                { \"value\": 60, \"name\": \"6日\" }\n            ],\n            \"emphasis\": {\n                \"itemStyle\": {\n                    \"shadowBlur\": 10,\n                    \"shadowOffsetX\": 0,\n                    \"shadowColor\": \"rgba(0, 0, 0, 0.5)\"\n                }\n            }\n        }\n    ]\n}\n', '\n\n分析结论: 根据提供的数据，生成了一个饼图来展示每日的人数分布。从图表中可以看出，3日的人数最多，达到了90人，占据了总人数的最大比例。其次是6日和5日，人数分别为60人和50人。1日的人数最少，只有10人。通过这个饼图，可以直观地看出每日人数的相对多少，有助于快速了解数据的分布情况。', '2025-04-16 19:52:30', '2025-04-17 09:25:04', 0, Var, '饼图', 1, NULL);
        SET ID = ID + 1;
        SET Var = Var + 1;
    END WHILE;
    COMMIT;
END$$;

delimiter ;  -- 界定符复原为默认的分号
CALL BatchInsert(1, 3000000);  -- 调用存储过程

```
1.2 测试查询性能（300万条） 
开启慢查询：  
```sql
# 是否开启，这边为开启，默认情况下是off
set global slow_query_log=on;

# 设置慢查询阈值，单位是 s，默认为10s，这边的意思是查询耗时超过0.5s，便会记录到慢查询日志里面
set global long_query_time=0.5;

# 确定慢查询日志的文件名和路径
mysql> show global variables like 'slow_query_log_file';
+---------------------+-------------------------------------------------------+
| Variable_name       | Value                                                 |
+---------------------+-------------------------------------------------------+
| slow_query_log_file | /usr/local/mysql/data/MacintoshdeMacBook-Pro-slow.log |
+---------------------+-------------------------------------------------------+
1 row in set (0.00 sec)

# 检查慢查询的详细指标，可以看到下面 slow_query_log = ON，long_query_time = 0.5 ，都是因为我们调整过的
mysql> show global variables like '%quer%';
+----------------------------------------+-------------------------------------------------------+
| Variable_name                          | Value                                                 |
+----------------------------------------+-------------------------------------------------------+
| binlog_rows_query_log_events           | OFF                                                   |
| ft_query_expansion_limit               | 20                                                    |
| have_query_cache                       | NO                                                    |
| log_queries_not_using_indexes          | OFF                                                   |
| log_throttle_queries_not_using_indexes | 0                                                     |
| long_query_time                        | 0.500000                                             |
| query_alloc_block_size                 | 8192                                                  |
| query_prealloc_size                    | 8192                                                  |
| slow_query_log                         | ON                                                   |
| slow_query_log_file                    | /usr/local/mysql/data/MacintoshdeMacBook-Pro-slow.log |
+----------------------------------------+-------------------------------------------------------+
10 rows in set (0.01 sec)
```
测试查询性能：  

```sql
# 获取一条id为1860000的数据
SELECT * 
FROM chart_test
WHERE user_id=1860000;
```

**2.2. 测试结果**

**插入结果：**
<div align=center>
    <img src="https://github.com/Will-immiracle/GenBI/blob/main/images/sql_test1.png" width="800" height="440">
</div>

**查询结果：**
<div align=center>
    <img src="https://github.com/Will-immiracle/GenBI/blob/main/images/sql_test2.png" width="800" height="600">
</div>

**优化后单表的插入、查询性能**



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
