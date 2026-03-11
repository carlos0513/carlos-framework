# Carlos Message 文档目录

> 版本: 2.0  
> 日期: 2026-03-11

---

## 文档清单

| 文件名                                                  | 说明                    | 状态      |
|------------------------------------------------------|-----------------------|---------|
| [DATABASE_SCHEMA.md](./DATABASE_SCHEMA.md)           | 数据库设计文档（表结构、索引、示例数据）  | ✅ 已评审优化 |
| [database_init.sql](./database_init.sql)             | 数据库初始化脚本（完整建表+初始数据）   | ✅ 已生成   |
| [database_upgrade_v2.sql](./database_upgrade_v2.sql) | 数据库升级脚本（V1.0 -> V2.0） | ⬜ 待生成   |

---

## 快速开始

### 1. 初始化数据库

```bash
# 方式1：使用mysql命令行
mysql -u root -p < database_init.sql

# 方式2：使用MySQL客户端工具执行
# 推荐使用 Navicat / DataGrip / MySQL Workbench 执行 database_init.sql
```

### 2. 验证安装

```sql
USE carlos_message;
SHOW TABLES;
-- 应该看到6张表：msg_channel, msg_receiver, msg_record, msg_send_log, msg_template, msg_type
```

### 3. 查看初始数据

```sql
-- 查看消息类型
SELECT * FROM msg_type;

-- 查看表结构
DESC msg_record;
DESC msg_receiver;
```

---

## 核心表关系

```
msg_type (消息类型)
    │
    └── msg_template (消息模板) ──┐
                                  │
msg_channel (渠道配置)             │
    │                              │
    └── msg_receiver (接收人) ◄────┘
            │
            ├── msg_record (消息记录)
            │
            └── msg_send_log (发送日志)
```

---

## 版本历史

### V2.0 (2026-03-11)

**优化内容：**

1. ✅ 修复 `msg_record` 表状态字段歧义问题（改为统计字段）
2. ✅ 统一所有表的审计字段（create_by/update_by/create_time/update_time）
3. ✅ 完善所有表的索引设计（添加复合索引优化高频查询）
4. ✅ 优化字段长度和必填属性（提高灵活性）
5. ✅ 增加 `msg_send_log.receiver_id` 字段（精确关联接收人）
6. ✅ 统一 `msg_type` 和 `msg_record` 的关联方式（都使用 type_code）

---

## 待办事项

- [ ] 生成 database_upgrade_v2.sql（从旧版本升级的脚本）
- [ ] 编写数据清理定时任务脚本
- [ ] 编写分库分表示例配置

---

**维护: AI Assistant**  
**最后更新: 2026-03-11**
