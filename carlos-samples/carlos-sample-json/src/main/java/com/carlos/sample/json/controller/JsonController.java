package com.carlos.sample.json.controller;

import com.carlos.json.jackson.JacksonUtil;
import com.carlos.sample.json.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * JSON 序列化演示 Controller
 * </p>
 *
 * <p>
 * 演示 endpoints:
 * - GET /api/json/user: 获取单个用户（演示基本序列化）
 * - GET /api/json/users: 获取用户列表（演示集合序列化）
 * - POST /api/json/user: 创建用户（演示反序列化）
 * - POST /api/json/parse: JSON 字符串解析（演示 JacksonUtil）
 * - GET /api/json/long-precision: Long 精度测试
 * </p>
 *
 * @author carlos
 * @date 2026/3/15
 */
@Slf4j
@RestController
@RequestMapping("/api/json")
public class JsonController {

    /**
     * 获取示例用户（演示基本序列化特性）
     */
    @GetMapping("/user")
    public User getUser() {
        User user = createSampleUser();
        log.info("序列化后的用户信息: {}", JacksonUtil.toJsonString(user));
        return user;
    }

    /**
     * 获取用户列表（演示集合序列化）
     */
    @GetMapping("/users")
    public List<User> getUsers() {
        List<User> users = Arrays.asList(
            createSampleUser(),
            createSampleUser2(),
            createSampleUser3()
        );
        log.info("返回用户数量: {}", users.size());
        return users;
    }

    /**
     * 创建用户（演示反序列化）
     * 请求体示例:
     * {
     *   "id": 9876543210123456789,
     *   "username": "zhangsan",
     *   "display_name": "张三",
     *   "password": "123456",
     *   "email": "zhangsan@example.com",
     *   "age": 25,
     *   "balance": 9999.99,
     *   "status": 1,
     *   "roles": ["USER", "ADMIN"],
     *   "points": 1234567890123456789
     * }
     */
    @PostMapping("/user")
    public User createUser(@RequestBody User user) {
        log.info("接收到的用户信息: {}", JacksonUtil.toJsonString(user));
        // 设置创建时间和更新时间
        user.setCreateTime(new Date());
        user.setUpdateTime(LocalDateTime.now());
        return user;
    }

    /**
     * 解析 JSON 字符串（演示 JacksonUtil 工具类）
     */
    @PostMapping("/parse")
    public Map<String, Object> parseJson(@RequestBody String jsonString) {
        log.info("接收到的 JSON 字符串: {}", jsonString);

        Map<String, Object> result = new HashMap<>();

        // 使用 JacksonUtil 解析
        User user = JacksonUtil.readValue(jsonString, User.class);
        result.put("parsedUser", user);

        // 转换为 Map
        Map<String, Object> map = JacksonUtil.readMap(jsonString, Object.class);
        result.put("asMap", map);

        // 验证 JSON 格式
        boolean valid = JacksonUtil.isValidJson(jsonString);
        result.put("isValid", valid);

        return result;
    }

    /**
     * Long 精度测试（演示 Long 自动转为 String）
     * 返回大 Long 值，前端不会出现精度丢失
     */
    @GetMapping("/long-precision")
    public Map<String, Object> longPrecisionTest() {
        Map<String, Object> result = new HashMap<>();

        // 大 Long 值（超过 2^53，JavaScript 会有精度问题）
        Long bigLong = 987654321012346789L;
        Long maxSafeInteger = 9007199254740991L; // JavaScript 最大安全整数

        result.put("bigLong", bigLong);
        result.put("maxSafeInteger", maxSafeInteger);
        result.put("description", "Long 类型会自动转为 String，避免前端精度丢失");

        // 用户ID测试
        User user = User.builder()
            .id(9223372036854775807L) // Long.MAX_VALUE
            .username("precision-test")
            .points(8888888888888888888L)
            .build();
        result.put("user", user);

        log.info("Long 精度测试结果: {}", JacksonUtil.toJsonString(result));
        return result;
    }

    /**
     * 日期时间格式化测试
     */
    @GetMapping("/date-time")
    public Map<String, Object> dateTimeTest() {
        Map<String, Object> result = new HashMap<>();

        User user = createSampleUser();

        result.put("user", user);
        result.put("currentDate", new Date());
        result.put("localDateTime", LocalDateTime.now());
        result.put("localDate", LocalDate.now());

        return result;
    }

    /**
     * 测试 null 值处理
     */
    @GetMapping("/null-handling")
    public User nullHandlingTest() {
        // 创建一个有很多 null 值的 user
        return User.builder()
            .id(1L)
            .username("minimal-user")
            .age(20)
            .status(1)
            .active(true)
            // 其他字段保持 null
            .build();
    }

    /**
     * JSON 工具类使用示例
     */
    @GetMapping("/util-demo")
    public Map<String, Object> utilDemo() {
        Map<String, Object> result = new HashMap<>();

        User user = createSampleUser();

        // 1. 对象转 JSON 字符串
        String json = JacksonUtil.toJsonString(user);
        result.put("toJsonString", json);

        // 2. 格式化输出
        String formattedJson = JacksonUtil.toJsonString(user, true);
        result.put("formattedJson", formattedJson);

        // 3. 包含 null 值的 JSON
        String jsonWithNull = JacksonUtil.toJsonStringNonNull(user);
        result.put("toJsonStringNonNull", jsonWithNull);

        // 4. JSON 转对象
        User parsedUser = JacksonUtil.readValue(json, User.class);
        result.put("readValue", parsedUser);

        // 5. JSON 转 List
        List<User> users = Arrays.asList(createSampleUser(), createSampleUser2());
        String usersJson = JacksonUtil.toJsonString(users);
        List<User> parsedUsers = JacksonUtil.readList(usersJson, User.class);
        result.put("readList", parsedUsers);

        // 6. JSON 转 Map
        Map<String, Object> map = JacksonUtil.readMap(json, Object.class);
        result.put("readMap", map);

        return result;
    }

    // ==================== 辅助方法 ====================

    private User createSampleUser() {
        return User.builder()
            .id(1234567890123456789L)
            .username("john_doe")
            .nickname("约翰·多伊")
            .password("secret_password_123")
            .email("john.doe@example.com")
            .phone("13800138000")
            .age(30)
            .balance(new BigDecimal("99999.99"))
            .status(1)
            .roles(Arrays.asList("USER", "ADMIN", "MANAGER"))
            .tags(Arrays.asList("vip", "active"))
            .createTime(new Date())
            .updateTime(LocalDateTime.now())
            .birthday(LocalDate.of(1990, 5, 15))
            .bio("这是一个示例用户简介")
            .avatarUrl("https://example.com/avatar.jpg")
            .active(true)
            .points(8888888888888888888L)
            .build();
    }

    private User createSampleUser2() {
        return User.builder()
            .id(987654321098765431L)
            .username("jane_smith")
            .nickname("简·史密斯")
            .password("another_secret")
            .email("jane.smith@example.com")
            .phone("13900139000")
            .age(25)
            .balance(new BigDecimal("50000.00"))
            .status(1)
            .roles(Arrays.asList("USER"))
            .tags(Collections.emptyList())
            .createTime(new Date())
            .updateTime(LocalDateTime.now())
            .birthday(LocalDate.of(1995, 8, 20))
            .bio(null)
            .avatarUrl(null)
            .active(true)
            .points(5555555555555555555L)
            .build();
    }

    private User createSampleUser3() {
        return User.builder()
            .id(1111111111111111111L)
            .username("bob_wilson")
            .nickname("鲍勃·威尔逊")
            .password("password123")
            .email(null) // 测试 null 值
            .phone(null)
            .age(35)
            .balance(BigDecimal.ZERO)
            .status(0)
            .roles(Arrays.asList("USER", "GUEST"))
            .tags(null)
            .createTime(new Date())
            .updateTime(LocalDateTime.now())
            .birthday(LocalDate.of(1988, 12, 10))
            .bio("")
            .avatarUrl("")
            .active(false)
            .points(0L)
            .build();
    }
}
