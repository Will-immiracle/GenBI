# Gen-Bi Framework


## 项目简介
基于Spring Boot+MQ+AIGC+React的智能数据分析平台。区别于传统BI，用户只需要导入原始数据集、并输入分析诉求，就能自动生成可视化图表及分析结论，实现数据分析的降本增效。


## 核心功能
Gen-Bi实现了以下几点核心功能：
1. 用户注册登录、信息上传
2. AI+数据分析生成图表和分析结论

---
## 模块解析



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
