package com.carlos.system.news.service.impl;

import cn.hutool.core.util.StrUtil;
import com.carlos.boot.util.ExtendInfoUtil;
import com.carlos.core.exception.ServiceException;
import com.carlos.core.pagination.Paging;
import com.carlos.system.news.convert.SysNewsConvert;
import com.carlos.system.news.manager.SysNewsManager;
import com.carlos.system.news.manager.SysViewRecordManager;
import com.carlos.system.news.pojo.dto.SysNewsDTO;
import com.carlos.system.news.pojo.dto.SysViewRecordDTO;
import com.carlos.system.news.pojo.param.SysNewsPageParam;
import com.carlos.system.news.pojo.vo.SysNewsVO;
import com.carlos.system.news.service.SysNewsService;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 系统-通知公告 业务接口实现类
 * </p>
 *
 * @author yunjin
 * @date 2022-11-14 23:48:53
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysNewsServiceImpl implements SysNewsService {

    private final SysNewsManager newsManager;

    private final SysViewRecordManager sysViewRecordManager;

    @Override
    public boolean addSysNews(SysNewsDTO dto) {
        if (StrUtil.isBlank(dto.getTitle())) {
            throw new ServiceException("新增失败，标题不可为空！");
        }
        if (StrUtil.isBlank(dto.getContent())) {
            throw new ServiceException("内容不可为空，标题不可为空！");
        }
        boolean success = this.newsManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            return false;
        }
        Serializable id = dto.getId();
        // 保存完成的后续业务
        return true;
    }

    @Override
    public void deleteSysNews(Set<Serializable> ids) {
        for (Serializable id : ids) {
            boolean success = this.newsManager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }
            // 删除成功的后续业务
        }
    }

    @Override
    public void updateSysNews(SysNewsDTO dto) {
        boolean success = this.newsManager.modify(dto);
        if (!success) {
            // 修改失败操作
            return;
        }
        // 修改成功的后续操作
    }

    @Override
    public Paging<SysNewsVO> getPage(SysNewsPageParam param) {
        Paging<SysNewsVO> page = this.newsManager.getPage(param);
        List<SysNewsVO> records = page.getRecords();
        Serializable userId = ExtendInfoUtil.getUserId();
        for (SysNewsVO record : records) {
            SysViewRecordDTO sysViewRecordDTO = this.sysViewRecordManager.getByUserIdAndReferenceId(userId, record.getId());
            if (sysViewRecordDTO == null) {
                record.setIsRead(false);
            } else {
                record.setIsRead(true);
            }
        }
        return page;
    }

    @Override
    public SysNewsVO getDtoById(String id) {
        SysNewsDTO dtoById = this.newsManager.getDtoById(id);
        SysNewsVO sysNewsVO = SysNewsConvert.INSTANCE.toVO(dtoById);
        if (sysNewsVO != null) {
            Serializable userId = ExtendInfoUtil.getUserId();
            SysViewRecordDTO sysViewRecordDTO = this.sysViewRecordManager.getByUserIdAndReferenceId(userId, sysNewsVO.getId());
            if (sysViewRecordDTO == null) {
                sysViewRecordDTO = new SysViewRecordDTO();
                sysViewRecordDTO.setUserId(userId.toString());
                sysViewRecordDTO.setType(0);
                sysViewRecordDTO.setReferenceId(id);
                this.sysViewRecordManager.add(sysViewRecordDTO);
            }
            sysNewsVO.setIsRead(true);
        }
        return sysNewsVO;
    }

}
