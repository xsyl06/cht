<h1>Cht</h1>

[![license](https://img.shields.io/github/license/pure-admin/vue-pure-admin.svg)](LICENSE)

**中文**

## 介绍
配套前端项目地址[cht-admin-vue](https://github.com/xsyl06/cht-admin-vue)
前端基于pure-admin-thin二次开发，后端基于`springboot3`、`mybatis-plus`、`mybatis-plus-join`、`sa-token`、`redis`、`mysql8`开发的后台管理系统

该项目包含后台管理的用户、角色、菜单权限以及登录鉴权等基础功能，适合作为后台管理的脚手架进行使用。可自行添加其他功能模块。

非常感谢大佬开源的[pure-admin](https://yiming_chang.gitee.io/pure-admin-doc/)项目，让我学到很多前端知识。

演示地址：http://124.221.46.250:21001/cht-admin/#/login

## 特性
- 使用mybatis-plus减轻sql开发工作
- 使用sa-token进行登录、鉴权，支持接口级别的鉴权校验
- 前后端使用sm2方式进行加密通信，保证通信安全
- 支持水平扩展

##
## 快速开始
### 环境及准备
#### JDK
从`springboot3`开始，`spring`框架需要使用`JDK17`版本，因此需要使用JDK17的开发及运行环境。
#### MAVEN
`maven`使用`3.6.3`版本或以上
#### Redis
项目使用`redis`作为缓存，需要提前安装redis并启动redis服务。`redis`版本不低于`6.2.8`
#### Mysql
`mysql`版本不低于`8.0.31`

### 安装
#### 从gitee上拉取代码
```shell
git clone https://gitee.com/xsyl06/cht.git
```
#### 从github上拉取代码
```shell
git clone https://github.com/xsyl06/cht.git
```
### 配置
#### 配置数据库
```yaml
spring:
  jackson:
    default-property-inclusion: non_null
  datasource:
    #将username修改为自己数据库的用户名
    username: username
    #将password修改为自己数据库的密码
    password: password
    driver-class-name: com.mysql.cj.jdbc.Driver
    #将database修改为自己的数据库名
    url: jdbc:mysql://ip:port/database?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&useSSL=false
    type: com.alibaba.druid.pool.DruidDataSource
```
#### 配置redis
```yaml
spring:
  redis:
    #将ip替换为redis的地址
    host: ip
    #将port替换为redis的端口
    port: port
    #如果有密码，将password替换为redis的密码，如果没有则可以注释
    password: password
    database: 0
```
#### 配置sa-token独立缓存redis
平台默认将将会话缓存和业务缓存分离，需要修改以下配置中关于host、port、password的配置，将redis配置为独立的redis，用于存放sa-token的会话缓存。
```yaml
  alone-redis:
    # Redis数据库索引（默认为0）
    database: 2
    # Redis服务器地址
    host: ip
    # Redis服务器连接端口
    port: port
    # Redis服务器连接密码（默认为空）
    password: password
```
#### 生成自己的前后端密钥及密码
<span style="color:red">平台前后端使用sm2方式进行加密通信，为保证安全，请使用自己生成的公私钥和盐，平台默认的公私钥存在泄露风险</span>
1. 使用ide打开项目后，进入`com.cht.utils.SMUtils.java`文件
2. 修改`com.cht.utils.SMUtils.java`文件第72行pwdStr的值为admin的初始化密码，执行main方法，获取自己的密钥及admin人员的初始加密密码。
3. 可将方法执行两次，分别获取前端公私钥和后端公私钥
4. 打开sql目录下的`init_DML.sql`文件，修改最后一行插入`system_dict`的语句中`dict_value2`的值为后端的私钥，`dict_value3`的值为后端的公钥，`dict_value4`的值为前端公钥
5. 修改`init_DML.sql`文件的倒数第三行，将`password`的值修改为admin的初始化密码
#### 执行sql脚本
在mysql数据库中执行sql文件夹下的脚本，先执行`init_DDL.sql`，后执行`init_DML.sql`

#### 启动后端项目
idea中启动直接点击启动按钮即可，默认环境为dev环境

打包后的可使用shell目录下的`auto-deploy-server.sh`脚本进行启动，其中需要修改`cht.ini`内的各项信息

##### 备份脚本
```shell
sh auto-deploy-server.sh backup
```

##### 启动脚本
```shell
sh auto-deploy-server.sh start
```
##### 停止脚本
```shell
sh auto-deploy-server.sh stop
```

##### 发布脚本
```shell
sh auto-deploy-server.sh deploy
```
##### 健康检查脚本
```shell
sh auto-deploy-server.sh check
```

### 开发
自有的业务需求可在com.cht包下新建包编写，或者新建model进行编写。
### 常见问题解答
#### common用户无法登录
在[生成自己的前后端密钥及密码](#生成自己的前后端密钥及密码)中只生成了admin的密码，因此common因使用的公私钥不同可能无法登录。

1. 可登录admin将common的密码重置。
2. 可在[生成自己的前后端密钥及密码](#生成自己的前后端密钥及密码)中生成common的密码，然后修改数据库中`system_user_info`表中数据或直接修改脚本中common用户的密码后再执行sql。
#### 密码重置后的密码是多少
平台默认的密码生成策略为`Cht@登录名`,可修改配置文件中`env.pwdPre`和`env.default-pwd-policy`。出于安全考虑也建议进行修改。
```yaml
env:
  # 默认密码的生成策略，1-(pwdPre+userName) 2-(pwdPre+phone后四位)
  pwdPre: "Cht@"
  default-pwd-policy: 1
```
如果想自己实现默认密码的逻辑，修改或重写`com.cht.admin.service.BaseService.getDefaultPwd(UserInfoVo vo)`方法即可。
#### 如何进行接口的权限校验
以MenuController.deleteMenu()为例，在方法上添加注解`@SaCheckPermission(对应权限名称)`即可，同时需要在页面上添加对应角色的权限。具体可参考[sa-token"注解鉴权"章节](https://sa-token.cc/doc.html#/use/at-check)
```java
@SaCheckPermission(Constants.Permission.MENU_DELETE)
public R deleteMenu(@PathVariable("id") Long id) {
    //业务代码
}
```
#### 我的接口不希望登录后才能访问，可以排除登录校验吗
可以，在`com.cht.config.SaTokenConfigure.java`中的属性`excludePathPatterns`里添加你的路径，注意路径不必携带配置文件中的`server.servlet.context-path`的值
```java
package com.cht.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

    private static final String[] excludePathPatterns = {
            "/api/login",
            "/api/refreshToken"
    };
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handler -> StpUtil.checkLogin()))
                .addPathPatterns("/api/**")
                .excludePathPatterns(excludePathPatterns);
    }
}
```

## 碎碎念
这个项目的构想源于我在工作过程中发现，后台管理系统（后管）几乎是每个项目必不可少的一部分。有些系统在初期可能没有设计后管，但随着业务的发展，逐渐意识到需要一个管理界面来进行配置人员信息的管理。在工作中，每当开始一个新项目，我都需要编写一套用户管理、角色管理、权限控制以及登录认证等功能，这显得非常繁琐。

此外，虽然有许多优秀的开源后管项目，例如ruoyi等，但在实际使用中，它们与我们的业务流程并不是完全吻合，因此需要对功能进行删减甚至是修改才能适应我们的需求。

基于以上原因，我产生了创建一个类似脚手架的项目的想法。这个项目只包含最基本的后管功能，方便开发人员快速上手，并在此基础上添加适合自己业务的功能。这样，我们可以节省开发时间，提高开发效率，同时也能确保后管系统与我们的业务需求高度匹配。

项目的前端使用了开源可商用的[pure-admin](https://yiming_chang.gitee.io/pure-admin-doc/)，感谢大佬的开源精神，让我这个后端菜鸡在对前端一窍不通的情况下搞定了公司的项目。这个前端框架也是公司一个项目目前在用的。

如果这个项目有帮到你一点点，我都非常荣幸！
## 许可证
完全开源免费，可免费商用
[MIT © 2024-present, cht-admin](./LICENSE)
