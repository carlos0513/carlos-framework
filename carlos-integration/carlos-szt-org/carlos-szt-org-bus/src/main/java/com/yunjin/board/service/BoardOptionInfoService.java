package com.yunjin.board.service;

import com.yunjin.board.manager.BoardOptionInfoManager;
import com.yunjin.board.pojo.dto.BoardOptionInfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Set;


/**
 * <p>
 * 工作台卡片选项信息 业务
 * </p>
 *
 * @author Carlos
 * @date 2025-5-13 11:07:57
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BoardOptionInfoService {

    private final BoardOptionInfoManager optionInfoManager;

    /**
     * 新增工作台卡片选项信息
     *
     * @param dto 工作台卡片选项信息数据
     * @author Carlos
     * @date 2025-5-13 11:07:57
     */
    public void addBoardOptionInfo(BoardOptionInfoDTO dto) {
        boolean success = optionInfoManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            return;
        }
        Serializable id = dto.getId();
        log.info("Insert 'BoardOptionInfo' data: id:{}", id);
        // 保存完成的后续业务
    }

    /**
     * 删除工作台卡片选项信息
     *
     * @param ids 工作台卡片选项信息id
     * @author Carlos
     * @date 2025-5-13 11:07:57
     */
    public void deleteBoardOptionInfo(Set<Serializable> ids) {
        for (Serializable id : ids) {
            boolean success = optionInfoManager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }
            // 删除成功的后续业务
        }
    }

    /**
     * 修改工作台卡片选项信息信息
     *
     * @param dto 对象信息
     * @author Carlos
     * @date 2025-5-13 11:07:57
     */
    public void updateBoardOptionInfo(BoardOptionInfoDTO dto) {
        boolean success = optionInfoManager.modify(dto);
        if (!success) {
            // 修改失败操作
            return;
        }
        // 修改成功的后续操作
        log.info("Update 'BoardOptionInfo' data: id:{}", dto.getId());
    }

}
