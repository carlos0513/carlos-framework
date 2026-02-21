package com.yunjin.org.service;

import com.yunjin.org.UserUtil;
import com.yunjin.org.manager.HelpFileManager;
import com.yunjin.org.pojo.dto.HelpFileDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.hutool.core.io.FileUtil.exist;

@Slf4j
@Service
@RequiredArgsConstructor
public class HelpFileService {
    private final HelpFileManager helpFileManager;

    public Boolean isExist(String fileName) {
        return helpFileManager.isExist(fileName);
    }

    public void add(HelpFileDTO dto) {
        dto.setCreateBy(UserUtil.getId());
        Boolean success = helpFileManager.add(dto);

        if (!success) {
            // 保存失败的应对措施
            return;
        }
    }

    public void updateBbtLabelType(HelpFileDTO dto, Object o, Object o1) {
        // 判断名字是否有冲突
        if (!helpFileManager.getById(dto.getId()).getFileName().equals(dto.getFileName())) {
            exist(dto.getFileName());
        }
        dto.setUpdateBy(UserUtil.getId());
        helpFileManager.modify(dto);
    }


    /**
     * 获取所有帮助文件
     *
     * @return java.util.List<com.yunjin.org.pojo.dto.HelpFileDTO>
     * @author Carlos
     * @date 2025-05-19 13:45
     */
    public List<HelpFileDTO> getAllHelpFile() {
        return helpFileManager.listAll();
    }
}
