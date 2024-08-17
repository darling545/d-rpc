## D-RPC一款轻量级的RPC框架🎄

<hr/>

### 项目介绍🌸
一款基于Vert.x+Etcd实现的轻量级Java RPC框架。

提供服务注册，发现，负载均衡。是一个理解RPC工作原理的示例。

根据 Dubbo 服务框架编码设计的简易版RPC框架。

<img src="C:\Users\1\Desktop\RPC时序图.png"/>
<hr/>

### 功能&设计🚀
#### 目录结构

d-rpc-master框架

├─d-rpc-core	--rpc核心实现类

├─example-common	--示例代码的公共依赖，包括接口、Model 等

├─dongs-rpc-spring-boot-starter	--spring-boot启动器

├─example-springboot-consumer	--[示例]服务消费者（注解实现）

├─example-springboot-provider	--[示例]服务提供者（注解实现）

├─example-consumer	--[示例]服务消费者

└─example-provider	--[示例]服务提供者

<hr/>

#### 核心模块结构

├── registry                       -> 注册中心相关功能

├── model                          -> 模型数据存放

├── server                         -> 服务启动配置

├── proxy                          -> 服务代理模块

├── loadbalancer                   -> 服务负载均衡

├── fault                          -> 重试、容错机制

└── serialize                      -> 序列化与反序列化

<hr/>

### 功能：
基于Vert.x实现长连接通信，包括心跳检测、续期机制、解决粘包半包等<br/>
基于Etcd实现分布式服务注册与发现<br/>
实现了SPI机制，便于扩展<br/>
实现了轮询、随机、加权随机等负载均衡算法<br/>
支持jdk的动态代理方式<br/>
支持fastJson、hessian、kryo、jdk的序列化方式<br/>
支持简易扩展点，泛化调用等功能<br/>
### 设计：
crpc框架调用流程：
<img src="C:\Users\1\Desktop\RPC流程图.png"/>
1.代理层：负责对底层调用细节的封装；<br/>
2.路由(请求处理器)层：负责在集群目标服务中的调用筛选策略；<br/>
3.协议层：负责请求数据的转码封装等作用；<br/>
4.注册中心：关注服务的上下线，以及一些权重，配置动态调整等功能；<br/>
5.容错层：当服务调用出现失败之后需要有容错层的兜底辅助；<br/>
