# 电子商城控制台应用

## 项目简介
这是一个基于控制台的电子商城应用，用户可以注册、登录、浏览商品、购买商品等。管理员可以管理商品和用户。

## 项目结构
```
.vscode/
    settings.json
bin/
    ...
lib/
    mysql-connector-j-9.1.0.jar
README.md
src/
    App.java
    DBManager.java
    InputManager.java
    Mall.java
    ProductData.java
    UserData.java
    UserManager.java
```

## 主要功能
- 用户注册和登录
- 浏览商品列表
- 购买商品
- 管理员登录
- 管理商品（添加、修改、删除）
- 管理用户（添加、修改、删除）

## 依赖
- MySQL Connector/J 9.1.0

## 数据库配置
在 `DBManager.java` 文件中配置数据库连接信息：
```java
public static final String url = "jdbc:mysql://localhost:3306/";
public static final String username = "root";
public static final String password = "root";
```

## 贡献
欢迎提交问题和贡献代码。