package com.carlos.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.carlos.auth.entity.TrustedDevice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 可信设备 Mapper 接口
 * </p>
 *
 * @author Carlos
 * @date 2026-02-26
 */
@Mapper
public interface TrustedDeviceMapper extends BaseMapper<TrustedDevice> {

    /**
     * 根据设备指纹查询
     *
     * @param userId 用户ID
     * @param deviceFingerprint 设备指纹
     * @return 可信设备信息
     */
    @Select("SELECT * FROM auth_trusted_device " +
            "WHERE user_id = #{userId} " +
            "AND device_fingerprint = #{deviceFingerprint} " +
            "AND deleted = 0")
    TrustedDevice selectByFingerprint(@Param("userId") Long userId,
                                      @Param("deviceFingerprint") String deviceFingerprint);

    /**
     * 查询用户的所有设备
     *
     * @param userId 用户ID
     * @return 设备列表
     */
    @Select("SELECT * FROM auth_trusted_device " +
            "WHERE user_id = #{userId} " +
            "AND deleted = 0 " +
            "ORDER BY last_used_time DESC")
    List<TrustedDevice> selectUserDevices(@Param("userId") Long userId);

    /**
     * 统计用户设备数量
     *
     * @param userId 用户ID
     * @return 设备数量
     */
    @Select("SELECT COUNT(*) FROM auth_trusted_device " +
            "WHERE user_id = #{userId} " +
            "AND deleted = 0")
    int countByUserId(@Param("userId") Long userId);

    /**
     * 删除用户的所有设备
     *
     * @param userId 用户ID
     * @return 删除数量
     */
    int deleteByUserId(@Param("userId") Long userId);
}
