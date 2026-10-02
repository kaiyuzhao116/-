# demo

Java 后端学习项目，基于 Spring Boot 搭建，用于练习**设计模式**、**多线程与并发**、**Netty WebSocket 实时通信**以及 **Spring Boot 配置绑定 / MyBatis 持久层**等知识点。每个知识点独立成一个包，互不干扰，可单独运行 `main` 方法验证。

## 技术栈

| 分类 | 选型 | 说明 |
| --- | --- | --- |
| 语言 / JDK | Java 17 | 需 JDK 17，用 JDK 21 会触发旧版 Lombok 编译崩溃 |
| 基础框架 | Spring Boot 3.0.2 | Web 容器端口 `8080` |
| Web 层 | Spring MVC + WebFlux | 同时支持阻塞与响应式 |
| 网络通信 | Netty（netty-all） | WebSocket 服务监听 `8090` |
| 持久层 | MyBatis 3.0.3 + MySQL | Mapper 接口 + XML 写 SQL |
| 序列化 | Protobuf 3.21.9 | `src/main/proto/user.proto` |
| 注解辅助 | Lombok | 简化实体类 |
| 前端 | Vue 3 + Vite | 见 `netty-vue/`，对接 WebSocket |

## 目录结构

```
src/main/java/com/example/
├── demo/                       # Spring Boot 主程序与各功能模块
│   ├── DemoApplication.java    # 启动类
│   ├── Config/                 # 线程池配置（双线程池分离业务与推送任务）
│   ├── concurrent/             # ThreadLocal 实验（UserContext + 拦截器清理）
│   ├── netty_websocket/        # Netty WebSocket 服务端（核心模块）
│   ├── netty_user/             # 用户登录 + token 认证（Controller/Service/Mapper）
│   ├── netty_helloword/        # Netty 入门 demo
│   ├── designpatterns/         # 设计原则（OCP 等）
│   ├── factory/                # 工厂模式系列
│   └── DemoProPertes2/         # @ConfigurationProperties 绑定练习
├── demo3/ ~ demo6/             # 建造者模式手写练习（含链式 Builder）
└── deme3/, demo1/, demo2/      # 其他零散练习

netty-vue/                      # Vue3 前端，WebSocket 实时聊天演示
src/main/resources/
├── application.yml             # 端口、数据源、MyBatis、自定义配置
├── bean.properties             # 反射加载 Bean 的配置
└── static/                     # index.html、netty_ws.html 测试页
```

## 快速开始

### 环境要求

- JDK 17
- Maven 3.6+
- MySQL（本地 `localhost:3306`）
- Node.js（运行前端时需要）

### 1. 准备数据库

创建库 `netty_user`，并将 `application.yml` 中 `spring.datasource.username / password` 改成你本地的数据库账号：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/netty_user?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true
```

### 2. 启动后端

```bash
mvn clean compile        # 首次会执行 protobuf 代码生成
mvn spring-boot:run
```

或直接运行主类 `com.example.demo.DemoApplication`。启动后同时监听：

- `http://localhost:8080` —— Spring Boot REST 接口
- `ws://localhost:8090/ws` —— Netty WebSocket 服务（随 Spring 生命周期启停）

### 3. 启动前端（可选）

```bash
cd netty-vue
npm install
npm run dev
```

### 4. 设计模式练习单独运行

各包互不依赖，可直接跑 `main`：

```bash
javac -encoding UTF-8 -d out src/main/java/com/example/demo6/*.java
java -cp out com.example.demo6.test
```

## 主要功能与接口

### 用户认证（REST）

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | `/netty_user/login` | 登录并返回 token |
| GET | `/netty_user/info` | 查询用户信息 |
| GET | `/netty_user/count` | 统计用户数量 |

### WebSocket 实时通信

连接地址：`ws://localhost:8090/ws?token=<登录返回的token>`

服务端流水线为五层结构：

1. `HttpServerCodec` —— 处理握手阶段的 HTTP 编解码
2. `ChunkedWriteHandler` —— 支持大数据块写入
3. `HttpObjectAggregator(64KB)` —— 聚合分段 HTTP 消息，防内存溢出
4. `WebSocketServerProtocolHandler("/ws")` —— 自动握手升级、处理 Close/Ping/Pong 控制帧
5. 业务 Handler —— 处理 `TextWebSocketFrame`，登记-分发-注销

`HttpHeadersHandler` 在握手阶段提取客户端 IP 与 token 完成身份认证，随后自我移除。前端需实现心跳与自动重连。

### 线程池

`ThreadPoolConfig` 采用双线程池分离：一个执行业务逻辑，一个专负责任务推送，避免相互阻塞；配合 `MyThreadFactory` 与 `GlobalUncaughtExceptionHandler` 做全局异常兜底。

## 已实现的练习专题

- **建造者模式**：`demo3`（链式 Builder）、`demo4` / `demo5`（Director + 抽象 Builder）、`demo6`（内部静态 Builder）
- **工厂模式**：简单工厂、工厂方法、抽象工厂（`demo/factory`）
- **设计原则**：开闭原则（`demo/designpatterns/principles/ocp`）
- **ThreadLocal**：脏数据问题与拦截器统一清理（`demo/concurrent/threadlocal`）
- **配置绑定**：`@ConfigurationProperties` 四种绑定方式与占位符（`demo/DemoProPertes2`）
- **Protobuf**：`src/main/proto/user.proto` 消息编解码

## 注意事项

- **JDK 版本**：命令行 `mvn` 必须使用 JDK 17，JDK 21 会因旧版 Lombok 编译器崩溃。
- **配置文件**：使用 YAML 时不要保留同名 `.properties` 文件，否则会被覆盖。
- **Netty 启动失败**：`bind` 失败时务必关闭 `EventLoopGroup`，否则线程残留导致 JVM 僵尸进程。
- **WebSocket 路径**：`WebSocketServerProtocolHandler` 是精确路径匹配，前端连接路径必须与服务端一致。
- **前后端联调**：前端连接后端使用绝对地址，不要依赖相对路径。

## License

仅用于个人学习练习。
