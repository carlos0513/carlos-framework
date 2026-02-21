package com.carlos.system.news.service;

import com.carlos.core.response.Result;
import com.carlos.system.news.pojo.dto.SysViewRecordDTO;
import java.util.Set;

/**
 * <p>
 * 浏览记录 业务接口
 * </p>
 *
 * @author yunjin
 * @date 2023-1-13 16:31:50
 */
public interface SysViewRecordService {

    /**
     * 新增浏览记录
     *
     * @param dto 浏览记录数据
     * @author yunjin
     * @date 2023-1-13 16:31:50
     */
    void addSysViewRecord(SysViewRecordDTO dto);

    /**
     * 删除浏览记录
     *
     * @param ids 浏览记录id
     * @author yunjin
     * @date 2023-1-13 16:31:50
     */
    void deleteSysViewRecord(Set<String> ids);

    /**
     * 修改浏览记录信息
     *
     * @param dto 对象信息
     * @author yunjin
     * @date 2023-1-13 16:31:50
     */
    void updateSysViewRecord(SysViewRecordDTO dto);

    Result<Long> getCount(int type);
}
