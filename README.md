# Gen-Bi Framework


## 项目简介
基于Spring Boot+MQ+AIGC+React的智能数据分析平台。区别于传统BI，用户只需要导入原始数据集、并输入分析诉求，就能自动生成可视化图表及分析结论，实现数据分析的降本增效。


## 核心功能
Gen-Bi实现的基本功能、扩展优化：
1. 用户输入分析需求、原始数据。后端对接AIGC（腾讯混元模型），得到echarts结构的数据。前端通过echarts库输出数据图表、分析结论。
2. 用户输入的原始数据存储上，从单库单表，优化为分表分库。同时新增了用户查看、编辑原始数据功能。

---
## 模块解析

### 智能分析业务开发
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

## 环境要求
- JDK 17+
- Maven 3.8.1+
- IDE（推荐IntelliJ IDEA）
- Git

## 快速开始

### 1. 获取代码
```bash
# 克隆项目
git clone https://github.com/Will-immiracle/GenBI.git

# 进入项目目录
cd GenBI
```

### 2. 编译运行
```bash
# 编译项目
mvn clean install

# 运行测试
mvn test
```
### 开源协议
本项目采用 MIT 协议开源，详见 [LICENSE](LICENSE) 文件。 
