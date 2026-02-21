package com.carlos.msg.base.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.carlos.core.exception.ServiceException;
import com.carlos.excel.easyexcel.ExcelUtil;
import com.carlos.msg.base.manager.MsgMessageTemplateManager;
import com.carlos.msg.base.pojo.dto.MsgMessageTemplateDTO;
import com.carlos.msg.base.pojo.excel.MsgMessageTemplateExcel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;


/**
 * <p>
 * 消息模板 业务
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:04
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MsgMessageTemplateService {

    private final MsgMessageTemplateManager messageTemplateManager;

    public Boolean addMsgMessageTemplate(MsgMessageTemplateDTO dto) {
        checkTemplateCode(dto.getTemplateCode());
        boolean success = messageTemplateManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            log.info("模板保存失败！");
            return false;
        } else {
            return true;
        }
    }

    private void checkTemplateCode(String templateCode) {
        MsgMessageTemplateDTO byCode = messageTemplateManager.getByCode(templateCode);
        if (byCode != null) {
            throw new ServiceException("模板编码有重复，请重新设置！");
        }

    }

    public void deleteMsgMessageTemplate(Set<String> ids) {
        for (Serializable id : ids) {
            boolean success = messageTemplateManager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }
            // 删除成功的后续业务
        }
    }

    public void updateMsgMessageTemplate(MsgMessageTemplateDTO dto) {
        checkTemplateCode(dto.getTemplateCode());
        boolean success = messageTemplateManager.modify(dto);
        if (!success) {
            // 修改失败操作
            throw new ServiceException("修改模板失败！");
        }
        // 修改成功的后续操作
    }

    public void exportMsgTemplate(HttpServletResponse response) {
        String fileName = "消息模板--" + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN);
        List<MsgMessageTemplateExcel> byIsActive = messageTemplateManager.getByIsActive();
        try {
            ExcelUtil.download(response, fileName, MsgMessageTemplateExcel.class, byIsActive);
        } catch (Exception e) {
            throw new ServiceException("消息模板导出失败！");
        }


    }

    public void exportTemplate(HttpServletResponse response) {
        String fileName = "样例模板--" + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN);
        ArrayList<MsgMessageTemplateExcel> list = new ArrayList<>();
        try {
            ExcelUtil.download(response, fileName, MsgMessageTemplateExcel.class, list);
        } catch (Exception e) {
            throw new ServiceException("样例模板导出失败！");
        }
    }

    /**
     * 获取模板信息
     *
     * @param templateCode 模板编码
     * @return com.carlos.msg.base.pojo.dto.MsgMessageTemplateDTO
     * @author Carlos
     * @date 2025-05-06 23:28
     */
    public MsgMessageTemplateDTO getTemplateByCode(String templateCode) {
        if (StrUtil.isBlank(templateCode)) {
            return null;
        }
        return messageTemplateManager.getByCode(templateCode);
    }
}
