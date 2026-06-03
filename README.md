# Flora Pic - 云图库平台

> 一个功能丰富的云图库管理平台，支持图片上传、空间管理、以图搜图、AI 扩图、实时协作编辑等功能。

<!-- TODO: 添加项目截图 -->
<!-- ![首页预览](./screenshot/home.png) -->

## ✨ 功能特性

### 🖼️ 图片管理
- **多方式上传**：支持文件上传、URL 上传、批量抓取上传
- **图片编辑**：基于 vue-cropper 的在线裁剪、旋转
- **元数据提取**：自动识别图片尺寸、格式、大小、主色调
- **图片审核**：管理员可对图片进行审核（待审核 / 通过 / 拒绝）
- **批量操作**：批量上传、批量编辑图片信息

### 🔍 智能搜索
- **以图搜图**：基于图片内容的反向搜索
- **颜色搜索**：按主色调相似度查找图片
- **关键词搜索**：支持按名称、分类、标签搜索

### 🤖 AI 能力
- **AI 扩图**：基于阿里云 AI 的图片边界扩展（Out-Painting）

### 📁 空间系统
- **私人空间**：个人图片存储空间
- **团队空间**：多人协作的共享空间
- **分级管理**：普通版 / 专业版 / 旗舰版，不同容量和数量限制
- **权限控制**：空间内三级角色（viewer / editor / admin）

### 🤝 实时协作
- **WebSocket 协同编辑**：多人实时查看图片编辑过程
- **高性能队列**：基于 LMAX Disruptor 的无锁环形缓冲区处理 WebSocket 事件

### 📊 数据分析
- **空间分析面板**：6 种可视化图表
  - 空间使用概览
  - 分类分布
  - 标签词云
  - 图片尺寸分布
  - 用户上传行为分析
  - 空间使用排行

### 👑 会员体系
- **VIP 兑换码**：通过兑换码激活 VIP 会员

## 🛠️ 技术栈

### 后端

| 技术 | 版本 | 说明 |
|---|---|---|
| Java | 8 | 编程语言 |
| Spring Boot | 2.7.6 | 应用框架 |
| MyBatis-Plus | 3.5.9 | ORM 框架 |
| MySQL | - | 关系型数据库 |
| Redis | - | 分布式缓存 / 会话存储 |
| Caffeine | - | 本地缓存（二级缓存架构） |
| Sa-Token | 1.39.0 | 权限认证框架 |
| 腾讯云 COS | 5.6.227 | 对象存储服务 |
| 阿里云 AI | - | AI 扩图能力 |
| ShardingSphere | 5.2.0 | 数据库分库分表 |
| LMAX Disruptor | 3.4.2 | 高性能无锁队列 |
| Spring WebSocket | - | 实时通信 |
| Knife4j | - | API 接口文档 |
| Jsoup | 1.15.3 | HTML 解析（批量抓取） |
| Hutool | 5.8.26 | Java 工具库 |
| Lombok | - | 代码简化 |

### 前端

| 技术 | 版本 | 说明 |
|---|---|---|
| Vue | 3.5.13 | 前端框架（Composition API） |
| TypeScript | 5.6.3 | 类型安全的 JavaScript |
| Vite | 6.0.1 | 构建工具 |
| Ant Design Vue | 4.2.6 | UI 组件库 |
| Pinia | 2.2.6 | 状态管理 |
| Vue Router | 4.4.5 | 路由管理 |
| Axios | 1.7.9 | HTTP 客户端 |
| ECharts | 5.5.1 | 数据可视化 |
| vue-cropper | 1.1.4 | 图片裁剪组件 |
| vue3-colorpicker | 2.3.0 | 颜色选择器 |
| @umijs/openapi | - | API 代码自动生成 |

## 📐 项目架构

本项目同时提供了 **标准分层架构** 和 **DDD（领域驱动设计）架构** 两种后端实现：

### 标准分层架构（flora-picture-backend）

```
Controller → Service → Mapper → Database
    └── Manager（跨切面：文件管理、认证、分片等）
```

### DDD 架构（flora-picture-backend-ddd）

```
interfaces/          # 接口层：Controller、DTO、VO、Assembler
application/service/ # 应用层：应用服务（业务编排）
domain/              # 领域层：实体、领域服务、仓储接口
  ├── picture/       # 图片限界上下文
  ├── space/         # 空间限界上下文
  └── user/          # 用户限界上下文
infrastructure/      # 基础设施层：仓储实现、配置、外部 API
shared/              # 共享层：认证、分片、WebSocket
```

## 🗄️ 数据库设计

数据库名：`yu_picture`（MySQL，utf8mb4_unicode_ci）

### user（用户表）

| 字段 | 类型 | 说明 |
|---|---|---|
| id | bigint | 主键 |
| userAccount | varchar | 账号（唯一） |
| userPassword | varchar | 密码（MD5 加密） |
| userName | varchar | 用户名 |
| userAvatar | varchar | 头像 URL |
| userProfile | varchar | 个人简介 |
| userRole | varchar | 角色（user / admin） |
| vipExpireTime | datetime | VIP 过期时间 |
| vipCode | varchar | VIP 兑换码 |
| vipNumber | bigint | VIP 编号 |

### picture（图片表）

| 字段 | 类型 | 说明 |
|---|---|---|
| id | bigint | 主键 |
| url | varchar | 图片 URL |
| name | varchar | 图片名称 |
| introduction | varchar | 简介 |
| category | varchar | 分类 |
| tags | varchar | 标签（JSON 数组） |
| picSize / picWidth / picHeight | bigint | 图片元数据 |
| picScale | double | 宽高比 |
| picFormat | varchar | 格式 |
| picColor | varchar | 主色调 |
| thumbnailUrl | varchar | 缩略图 URL |
| userId | bigint | 上传者 |
| spaceId | bigint | 所属空间（null = 公共图库） |
| reviewStatus | int | 审核状态（0 待审核 / 1 通过 / 2 拒绝） |

### space（空间表）

| 字段 | 类型 | 说明 |
|---|---|---|
| id | bigint | 主键 |
| spaceName | varchar | 空间名称 |
| spaceLevel | int | 等级（0 普通 / 1 专业 / 2 旗舰） |
| spaceType | int | 类型（0 私人 / 1 团队） |
| maxSize / maxCount | bigint | 容量上限 |
| totalSize / totalCount | bigint | 当前使用量 |
| userId | bigint | 创建者 |

### space_user（空间成员表）

| 字段 | 类型 | 说明 |
|---|---|---|
| id | bigint | 主键 |
| spaceId | bigint | 空间 ID |
| userId | bigint | 用户 ID |
| spaceRole | varchar | 角色（viewer / editor / admin） |

### 空间等级限制

| 等级 | 图片数量 | 存储容量 |
|---|---|---|
| 普通版 | 100 张 | 100 MB |
| 专业版 | 1,000 张 | 1 GB |
| 旗舰版 | 10,000 张 | 10 GB |

## 🚀 快速开始

### 环境要求

- **JDK** 8+
- **Node.js** 16+
- **MySQL** 5.7+
- **Redis** 6.0+
- **Maven** 3.6+

### 1. 克隆项目

```bash
git clone https://github.com/your-username/FloraPic.git
cd FloraPic
```

### 2. 初始化数据库

```bash
# 创建数据库并执行建表脚本
mysql -u root -p < flora-picture-backend/sql/create_table.sql
```

### 3. 启动后端

```bash
cd flora-picture-backend

# 修改数据库和 Redis 配置
# vim src/main/resources/application.yml

# 编译并启动
mvn clean package -DskipTests
java -jar target/flora-picture-backend-0.0.1-SNAPSHOT.jar
```

后端默认运行在 `http://localhost:8123/api`

### 4. 启动前端

```bash
cd flora-picture-frontend

# 安装依赖
npm install

# 开发模式启动
npm run dev

# 生产构建
npm run build
```

前端默认运行在 `http://localhost:5173`

## 📁 目录结构

```
FloraPic/
├── flora-picture-frontend/          # 前端项目
│   ├── src/
│   │   ├── api/                     # 自动生成的 API 客户端
│   │   ├── components/              # 公共组件
│   │   │   └── analyze/             # 数据分析图表组件
│   │   ├── constants/               # 常量定义
│   │   ├── layouts/                 # 页面布局
│   │   ├── pages/                   # 页面视图（16 个路由页面）
│   │   ├── router/                  # 路由配置
│   │   ├── stores/                  # Pinia 状态管理
│   │   └── main.ts                  # 入口文件
│   ├── package.json
│   └── vite.config.ts
│
├── flora-picture-backend/           # 后端项目（标准架构）
│   ├── src/main/java/com/flora/florapicturebackend/
│   │   ├── controller/              # 控制器层
│   │   ├── service/                 # 业务逻辑层
│   │   ├── mapper/                  # 数据访问层
│   │   ├── model/                   # 数据模型（DTO / VO / Entity / Enum）
│   │   ├── manager/                 # 管理层（文件、认证、分片等）
│   │   ├── config/                  # 配置类
│   │   ├── annotation/              # 自定义注解
│   │   ├── aop/                     # 切面
│   │   ├── exception/               # 异常处理
│   │   └── utils/                   # 工具类
│   ├── src/main/resources/
│   │   ├── application.yml          # 应用配置
│   │   ├── mapper/                  # MyBatis XML
│   │   └── biz/                     # 业务配置（权限、VIP 码等）
│   └── sql/                         # 数据库脚本
│
├── flora-picture-backend-ddd/       # 后端项目（DDD 架构）
│   └── src/main/java/com/flora/florapicture/
│       ├── interfaces/              # 接口层
│       ├── application/             # 应用层
│       ├── domain/                  # 领域层
│       ├── infrastructure/          # 基础设施层
│       └── shared/                  # 共享层
│
└── README.md                        # 项目说明文档
```

## 📖 API 文档

启动后端后，访问 Knife4j 自动生成的 API 文档：

```
http://localhost:8123/api/doc.html
```

### 主要接口模块

| 模块 | 路径前缀 | 说明 |
|---|---|---|
| 用户 | `/user` | 注册、登录、登出、用户管理 |
| 图片 | `/picture` | 上传、编辑、搜索、审核、AI 扩图 |
| 空间 | `/space` | 空间 CRUD、等级列表 |
| 空间用户 | `/spaceUser` | 空间成员管理 |
| 空间分析 | `/spaceAnalyze` | 数据分析接口 |
| 文件 | `/file` | 通用文件上传 |

## 🔐 权限设计

### 系统级权限

| 角色 | 说明 |
|---|---|
| user | 普通用户 |
| admin | 管理员（可管理用户、图片审核、空间管理） |

### 空间级权限

| 角色 | 权限 |
|---|---|
| viewer | 查看图片 |
| editor | 查看 / 上传 / 编辑 / 删除图片 |
| admin | 所有权限 + 管理空间成员 |

## ⚙️ 核心配置

### 后端配置（application.yml）

```yaml
server:
  port: 8123
  servlet:
    context-path: /api

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/yu_picture
    username: root
    password: your_password

  redis:
    host: localhost
    port: 6379
```

### 需要配置的第三方服务

1. **腾讯云 COS**：对象存储，用于图片存储
2. **阿里云 AI**：用于 AI 扩图功能
3. **Redis**：缓存和会话存储

## 📝 开发说明

### API 代码自动生成

前端使用 `@umijs/openapi` 从后端 OpenAPI 规范自动生成 TypeScript API 客户端：

```bash
cd flora-picture-frontend
npm run openapi
```

### 二级缓存架构

项目采用 Caffeine（本地）+ Redis（分布式）的二级缓存：

- **L1 Caffeine**：进程内缓存，毫秒级响应
- **L2 Redis**：分布式缓存，跨实例共享
- **防雪崩**：随机 TTL 偏移量避免缓存同时失效

### 数据库分片

基于 ShardingSphere 的动态分表，按 `spaceId` 对 `picture` 表进行分片，支持大空间的海量图片存储。

## 📄 许可证

本项目仅供学习交流使用。

## 👤 作者

**Flora**
