# Gen-BI Framework


## 项目简介
    可视化图表生成与数据分析平台。区别于传统BI，用户导入原始数据集、并输入分析诉求，即可生成可视化图表及分析结论.


## 核心功能
### Gen-BI 网站的基本功能：
- [x] 用户输入分析需求、原始数据。得到**数据图表、分析结论**。
- [x] 查看历史**所有**可视化图表、结论。附带**关键词**查询功能。
- [ ]  原始数据存入服务器数据库并**建表**。用户可以查看、编辑原始数据。
### 流程图展示

### 扩展优化：
- 分库分表
  - apache poi库详见： https://blog.51cto.com/u_16175466/12885984
- 限流服务
  - redisson令牌桶限流
- 系统异步化

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

### 模块四、 系统异步化
**业务场景**

**业务流程**
1. 当用户要进行耗时很长的操作时，点击提交后，不需要在界面傻等，而是应该把这个任务保存到数据库中记录下来 
2. 用户要执行新任务时：
- a. 任务提交成功：
    -  i 如果有多余线程，立即去做这个任务 
    -  ii 如果所有线程都忙，无法继续处理，那就放到等待队列里  
- b. 任务提交失败：比如线程都忙，且**任务队列**满了 
    -  i 拒绝掉这个任务，再也不去执行 
    -  ii 通过数据库记录提交失败的任务，并且在程序闲的时候，再去执行 
3. 程序（线程）从任务队列中取出任务依次执行，每完成一件事情要修改一下任务的状态。 
4. 用户可以查询任务的执行状态，或者在任务执行成功或失败时都能得到通知（发邮件、系统提示音、短信），从而优化体验 
5. 如果要执行的任务非常复杂，包含很多环节，在每一个小任务完成时，要在程序（数据库中）记录一下任务的执行状态（进度） 

  



## 项目结构
````
gen-bi
```
src/main/java/com/minispring/
.
├── pom.xml
├── sql
│   └── create_table.sql
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── will
│   │   │           └── bi
│   │   │               ├── MainApplication.java
│   │   │               ├── annotation
│   │   │               │   └── AuthCheck.java
│   │   │               ├── common
│   │   │               │   ├── PageRequest.java
│   │   │               │   ├── Result.java
│   │   │               │   └── ResultCodeEnum.java
│   │   │               ├── config
│   │   │               │   ├── CorsConfig.java
│   │   │               │   └── MyBatisPlusConfig.java
│   │   │               ├── constant
│   │   │               │   ├── CommonConstant.java
│   │   │               │   └── UserConstant.java
│   │   │               ├── controller
│   │   │               │   ├── ChartController.java
│   │   │               │   └── UserController.java
│   │   │               ├── exception
│   │   │               │   ├── BusinessException.java
│   │   │               │   ├── handler
│   │   │               │   │   └── GlobalExceptionHandler.java
│   │   │               │   └── utils
│   │   │               │       └── ThrowUtils.java
│   │   │               ├── mapper
│   │   │               │   ├── ChartMapper.java
│   │   │               │   └── UserMapper.java
│   │   │               ├── model
│   │   │               │   ├── dto
│   │   │               │   │   ├── chart
│   │   │               │   │   │   ├── ChartAddRequest.java
│   │   │               │   │   │   ├── ChartDeleteRequest.java
│   │   │               │   │   │   ├── ChartEditRequest.java
│   │   │               │   │   │   ├── ChartQueryRequest.java
│   │   │               │   │   │   └── ChartUpdateRequest.java
│   │   │               │   │   └── user
│   │   │               │   │       ├── UserAddRequest.java
│   │   │               │   │       ├── UserDeleteRequest.java
│   │   │               │   │       ├── UserLoginDTO.java
│   │   │               │   │       ├── UserQueryRequest.java
│   │   │               │   │       ├── UserRegisterDTO.java
│   │   │               │   │       ├── UserUpdateMyRequest.java
│   │   │               │   │       └── UserUpdateRequest.java
│   │   │               │   ├── enums
│   │   │               │   │   └── UserRoleEnum.java
│   │   │               │   ├── pojo
│   │   │               │   │   ├── Chart.java
│   │   │               │   │   └── User.java
│   │   │               │   └── vo
│   │   │               │       └── user
│   │   │               │           ├── UserLoginVO.java
│   │   │               │           └── UserVO.java
│   │   │               ├── service
│   │   │               │   ├── ChartService.java
│   │   │               │   ├── UserService.java
│   │   │               │   └── impl
│   │   │               │       ├── ChartServiceImpl.java
│   │   │               │       └── UserServiceImpl.java
│   │   │               └── utils
│   │   │                   ├── JwtHelper.java
│   │   │                   ├── MD5Util.java
│   │   │                   └── SqlUtils.java
│   │   └── resources
│   │       ├── application.yaml
│   │       ├── mapper
│   │       │   ├── ChartMapper.xml
│   │       │   └── UserMapper.xml
│   │       └── templates
│   │           ├── TemplateController.java.ftl
│   │           ├── TemplateService.java.ftl
│   │           ├── TemplateServiceImpl.java.ftl
│   │           └── model
│   │               ├── TemplateAddRequest.java.ftl
│   │               ├── TemplateEditRequest.java.ftl
│   │               ├── TemplateQueryRequest.java.ftl
│   │               ├── TemplateUpdateRequest.java.ftl
│   │               └── TemplateVO.java.ftl
│   └── test
│       └── java
└── target
    ├── GenBackend-1.0-SNAPSHOT.jar
    ├── GenBackend-1.0-SNAPSHOT.jar.original
    ├── classes
    │   ├── application.yaml
    │   ├── com
    │   │   └── will
    │   │       └── bi
    │   │           ├── MainApplication.class
    │   │           ├── annotation
    │   │           │   └── AuthCheck.class
    │   │           ├── common
    │   │           │   ├── PageRequest.class
    │   │           │   ├── Result.class
    │   │           │   └── ResultCodeEnum.class
    │   │           ├── config
    │   │           │   ├── CorsConfig.class
    │   │           │   └── MyBatisPlusConfig.class
    │   │           ├── constant
    │   │           │   ├── CommonConstant.class
    │   │           │   └── UserConstant.class
    │   │           ├── controller
    │   │           │   ├── ChartController.class
    │   │           │   └── UserController.class
    │   │           ├── exception
    │   │           │   ├── BusinessException.class
    │   │           │   ├── handler
    │   │           │   │   └── GlobalExceptionHandler.class
    │   │           │   └── utils
    │   │           │       └── ThrowUtils.class
    │   │           ├── mapper
    │   │           │   ├── ChartMapper.class
    │   │           │   └── UserMapper.class
    │   │           ├── model
    │   │           │   ├── dto
    │   │           │   │   ├── chart
    │   │           │   │   │   ├── ChartAddRequest.class
    │   │           │   │   │   ├── ChartDeleteRequest.class
    │   │           │   │   │   ├── ChartEditRequest.class
    │   │           │   │   │   ├── ChartQueryRequest.class
    │   │           │   │   │   └── ChartUpdateRequest.class
    │   │           │   │   └── user
    │   │           │   │       ├── UserAddRequest.class
    │   │           │   │       ├── UserDeleteRequest.class
    │   │           │   │       ├── UserLoginDTO.class
    │   │           │   │       ├── UserQueryRequest.class
    │   │           │   │       ├── UserRegisterDTO.class
    │   │           │   │       ├── UserUpdateMyRequest.class
    │   │           │   │       └── UserUpdateRequest.class
    │   │           │   ├── enums
    │   │           │   │   └── UserRoleEnum.class
    │   │           │   ├── pojo
    │   │           │   │   ├── Chart.class
    │   │           │   │   └── User.class
    │   │           │   └── vo
    │   │           │       └── user
    │   │           │           ├── UserLoginVO.class
    │   │           │           └── UserVO.class
    │   │           ├── service
    │   │           │   ├── ChartService.class
    │   │           │   ├── UserService.class
    │   │           │   └── impl
    │   │           │       ├── ChartServiceImpl.class
    │   │           │       └── UserServiceImpl.class
    │   │           └── utils
    │   │               ├── JwtHelper.class
    │   │               ├── MD5Util.class
    │   │               └── SqlUtils.class
    │   ├── mapper
    │   │   ├── ChartMapper.xml
    │   │   └── UserMapper.xml
    │   └── templates
    │       ├── TemplateController.java.ftl
    │       ├── TemplateService.java.ftl
    │       ├── TemplateServiceImpl.java.ftl
    │       └── model
    │           ├── TemplateAddRequest.java.ftl
    │           ├── TemplateEditRequest.java.ftl
    │           ├── TemplateQueryRequest.java.ftl
    │           ├── TemplateUpdateRequest.java.ftl
    │           └── TemplateVO.java.ftl
    ├── generated-sources
    │   └── annotations
    ├── generated-test-sources
    │   └── test-annotations
    ├── maven-archiver
    │   └── pom.properties
    ├── maven-status
    │   └── maven-compiler-plugin
    │       ├── compile
    │       │   └── default-compile
    │       │       ├── createdFiles.lst
    │       │       └── inputFiles.lst
    │       └── testCompile
    │           └── default-testCompile
    │               ├── createdFiles.lst
    │               └── inputFiles.lst
    └── test-classes
```
````

### 开源协议
本项目采用 MIT 协议开源，详见 [LICENSE](LICENSE) 文件。 
