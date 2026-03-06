package com.carlos.audit.pojo.dto;


import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 审计日志配置（动态TTL与采样策略） 数据传输对象，service和manager向外传输对象
 * </p>
 *
 * @author Carlos
 * @date 2026年3月6日 下午9:31:12
 */
public class AuditLogConfigDTO {
    /** 主键 */
    private Long id;
    /** 日志类型，如：USER_LOGIN */
    private String logType;
    /** 保留天数 */
    private Integer retentionDays;
    /** 采样率 0.00-1.00，1.00为全量 */
    private BigDecimal samplingRate;
    /** 是否异步写入：0-同步/1-异步 */
    private Boolean asyncWrite;
    /** 是否存储数据变更：0-否/1-是 */
    private Boolean storeDataChange;
    /** 是否存储技术上下文：0-否/1-是 */
    private Boolean storeTechnical;
    /** 租户ID，0表示系统级配置 */
    private String tenantId;
    /** 创建者编号 */
    private Long createBy;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新者编号 */
    private Long updateBy;
    /** 更新时间 */
    private LocalDateTime updateTime;

    // Getter and Setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public Integer getRetentionDays() {
        return retentionDays;
    }

    public void setRetentionDays(Integer retentionDays) {
        this.retentionDays = retentionDays;
    }

    public BigDecimal getSamplingRate() {
        return samplingRate;
    }

    public void setSamplingRate(BigDecimal samplingRate) {
        this.samplingRate = samplingRate;
    }

    public Boolean getAsyncWrite() {
        return asyncWrite;
    }

    public void setAsyncWrite(Boolean asyncWrite) {
        this.asyncWrite = asyncWrite;
    }

    public Boolean getStoreDataChange() {
        return storeDataChange;
    }

    public void setStoreDataChange(Boolean storeDataChange) {
        this.storeDataChange = storeDataChange;
    }

    public Boolean getStoreTechnical() {
        return storeTechnical;
    }

    public void setStoreTechnical(Boolean storeTechnical) {
        this.storeTechnical = storeTechnical;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
