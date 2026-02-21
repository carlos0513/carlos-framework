package com.carlos.system.news.service.impl;

import com.carlos.boot.util.ExtendInfoUtil;
import com.carlos.core.response.Result;
import com.carlos.system.news.manager.SysNewsManager;
import com.carlos.system.news.manager.SysViewRecordManager;
import com.carlos.system.news.pojo.dto.SysViewRecordDTO;
import com.carlos.system.news.service.SysViewRecordService;
import java.io.Serializable;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 浏览记录 业务接口实现类
 * </p>
 *
 * @author yunjin
 * @date 2023-1-13 16:31:50
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysViewRecordServiceImpl implements SysViewRecordService {

    private final SysViewRecordManager viewRecordManager;

    private final SysNewsManager sysNewsManager;

    @Override
    public void addSysViewRecord(final SysViewRecordDTO dto) {
        final boolean success = viewRecordManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            return;
        }
        final Serializable id = dto.getId();
        // 保存完成的后续业务
    }

    @Override
    public void deleteSysViewRecord(final Set<String> ids) {
        for (final Serializable id : ids) {
            final boolean success = viewRecordManager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }
            // 删除成功的后续业务
        }
    }

    @Override
    public void updateSysViewRecord(final SysViewRecordDTO dto) {
        final boolean success = viewRecordManager.modify(dto);
        if (!success) {
            // 修改失败操作
            return;
        }
        // 修改成功的后续操作
    }

    @Override
    public Result<Long> getCount(final int type) {
        final Serializable userId = ExtendInfoUtil.getUserId();
        final long count = viewRecordManager.getCountByUserId(userId, type);
        final long total;
        if (type == 0) {
            //通知公告统计
            total = sysNewsManager.count();
        } else {
            //消息统计
            total = 0;
        }

        final Result<Long> result = new Result<>();
        result.setData(total - count < 0 ? 0 : total - count);
        return result;
    }

}
