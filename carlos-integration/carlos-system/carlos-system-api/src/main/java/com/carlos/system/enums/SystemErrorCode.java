package com.carlos.system.enums;

import com.carlos.core.exception.BusinessException;
import com.carlos.core.response.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 系统管理模块错误码
 * <p>
 * 模块编码：09（系统管理服务）
 * <p>
 * 错误码格式：A-BB-CC
 * <ul>
 *   <li>A - 错误级别：1客户端错误，2业务错误，3第三方错误，5系统错误</li>
 *   <li>BB - 模块编码：09系统管理</li>
 *   <li>CC - 具体错误序号</li>
 * </ul>
 *
 * @author carlos
 * @since 3.0.0
 */
@Getter
@RequiredArgsConstructor
public enum SystemErrorCode implements ErrorCode {

    // ==================== 客户端错误 (1-09-xx) ====================
    SYS_PARAM_DICT_TYPE_EMPTY("10901", "字典类型不能为空", 400),
    SYS_PARAM_DICT_CODE_EMPTY("10902", "字典编码不能为空", 400),
    SYS_PARAM_CONFIG_KEY_EMPTY("10903", "配置键不能为空", 400),
    SYS_PARAM_FILE_EMPTY("10904", "上传文件不能为空", 400),
    SYS_PARAM_FILE_NAME_EMPTY("10905", "文件名不能为空", 400),

    // ==================== 业务错误 (2-09-xx) ====================
    // 字典相关
    SYS_DICT_NOT_FOUND("20901", "字典不存在", 404),
    SYS_DICT_ALREADY_EXISTS("20902", "字典已存在", 409),
    SYS_DICT_TYPE_NOT_FOUND("20903", "字典类型不存在", 404),
    SYS_DICT_TYPE_ALREADY_EXISTS("20904", "字典类型已存在", 409),
    SYS_DICT_ITEM_NOT_FOUND("20905", "字典项不存在", 404),
    SYS_DICT_ITEM_CODE_ALREADY_EXISTS("20906", "字典项编码已存在", 409),
    SYS_DICT_SYSTEM_CANNOT_DELETE("20907", "系统内置字典不能删除", 403),

    // 配置相关
    SYS_CONFIG_NOT_FOUND("20911", "系统配置不存在", 404),
    SYS_CONFIG_KEY_ALREADY_EXISTS("20912", "配置键已存在", 409),
    SYS_CONFIG_VALUE_TYPE_INVALID("20913", "配置值类型无效", 400),
    SYS_CONFIG_SYSTEM_CANNOT_DELETE("20914", "系统内置配置不能删除", 403),

    // 文件相关
    SYS_FILE_NOT_FOUND("20921", "文件不存在", 404),
    SYS_FILE_UPLOAD_FAILED("20922", "文件上传失败", 500),
    SYS_FILE_DOWNLOAD_FAILED("20923", "文件下载失败", 500),
    SYS_FILE_DELETE_FAILED("20924", "文件删除失败", 500),
    SYS_FILE_TYPE_NOT_ALLOWED("20925", "文件类型不允许", 400),
    SYS_FILE_SIZE_EXCEEDED("20926", "文件大小超出限制", 400),
    SYS_FILE_NAME_TOO_LONG("20927", "文件名过长", 400),
    SYS_FILE_CONTENT_EMPTY("20928", "文件内容为空", 400),

    // 菜单相关
    SYS_MENU_NOT_FOUND("20931", "菜单不存在", 404),
    SYS_MENU_ALREADY_EXISTS("20932", "菜单已存在", 409),
    SYS_MENU_CODE_ALREADY_EXISTS("20933", "菜单编码已存在", 409),
    SYS_MENU_HAS_CHILDREN("20934", "该菜单存在子菜单，不能删除", 400),
    SYS_MENU_PARENT_NOT_FOUND("20935", "上级菜单不存在", 404),
    SYS_MENU_PARENT_CANNOT_BE_SELF("20936", "上级菜单不能是自己", 400),
    SYS_MENU_SYSTEM_CANNOT_DELETE("20937", "系统内置菜单不能删除", 403),

    // 资源相关
    SYS_RESOURCE_NOT_FOUND("20941", "资源不存在", 404),
    SYS_RESOURCE_ALREADY_EXISTS("20942", "资源已存在", 409),
    SYS_RESOURCE_CODE_ALREADY_EXISTS("20943", "资源编码已存在", 409),
    SYS_RESOURCE_HAS_DEPENDENCIES("20944", "资源存在依赖，不能删除", 400),

    // 地区相关
    SYS_REGION_NOT_FOUND("20951", "地区不存在", 404),
    SYS_REGION_CODE_ALREADY_EXISTS("20952", "地区编码已存在", 409),
    SYS_REGION_HAS_CHILDREN("20953", "该地区存在子地区，不能删除", 400),

    // ==================== 第三方错误 (3-09-xx) ====================
    SYS_STORAGE_OSS_ERROR("30901", "对象存储服务异常", 500),
    SYS_STORAGE_MINIO_ERROR("30902", "MinIO 存储服务异常", 500),
    SYS_STORAGE_COS_ERROR("30903", "腾讯云存储服务异常", 500),
    SYS_STORAGE_OBS_ERROR("30904", "华为云存储服务异常", 500),

    // ==================== 系统错误 (5-09-xx) ====================
    SYS_SYSTEM_ERROR("50901", "系统管理内部错误", 500),
    SYS_CACHE_ERROR("50902", "系统缓存异常", 500),
    SYS_LOCK_ERROR("50903", "分布式锁异常", 500),
    SYS_RATE_LIMIT_ERROR("50904", "限流异常", 500);

    private final String code;
    private final String message;
    private final int httpStatus;

    /**
     * 创建业务异常（使用默认消息）
     *
     * @return BusinessException
     */
    @Override
    public BusinessException exception() {
        return new BusinessException(this);
    }

    /**
     * 创建业务异常（自定义消息）
     *
     * @param customMessage 自定义消息
     * @return BusinessException
     */
    @Override
    public BusinessException exception(String customMessage) {
        return new BusinessException(this, customMessage);
    }

    /**
     * 创建业务异常（格式化消息）
     *
     * @param format 格式字符串
     * @param args   参数
     * @return BusinessException
     */
    @Override
    public BusinessException exception(String format, Object... args) {
        return new BusinessException(this, String.format(format, args));
    }
}
