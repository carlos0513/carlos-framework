package com.yunjin.board.service;

import com.yunjin.board.manager.BoardCardInfoManager;
import com.yunjin.board.pojo.dto.BoardCardInfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;


/**
 * <p>
 * 工作台卡片信息 业务
 * </p>
 *
 * @author Carlos
 * @date 2025-5-13 11:07:57
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BoardCardInfoService {

    private final BoardCardInfoManager cardInfoManager;

    /**
     * 新增工作台卡片信息
     *
     * @param dto 工作台卡片信息数据
     * @author Carlos
     * @date 2025-5-13 11:07:57
     */
    public void addBoardCardInfo(BoardCardInfoDTO dto) {
        boolean success = cardInfoManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            return;
        }
        Serializable id = dto.getId();
        log.info("Insert 'BoardCardInfo' data: id:{}", id);
        // 保存完成的后续业务
    }

    /**
     * 删除工作台卡片信息
     *
     * @param ids 工作台卡片信息id
     * @author Carlos
     * @date 2025-5-13 11:07:57
     */
    public void deleteBoardCardInfo(Set<Serializable> ids) {
        for (Serializable id : ids) {
            boolean success = cardInfoManager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }
            // 删除成功的后续业务
        }
    }

    /**
     * 修改工作台卡片信息信息
     *
     * @param dto 对象信息
     * @author Carlos
     * @date 2025-5-13 11:07:57
     */
    public void updateBoardCardInfo(BoardCardInfoDTO dto) {
        boolean success = cardInfoManager.modify(dto);
        if (!success) {
            // 修改失败操作
            return;
        }
        // 修改成功的后续操作
        log.info("Update 'BoardCardInfo' data: id:{}", dto.getId());
    }

    /**
     * 获取所有配置
     *
     * @return java.util.List<com.yunjin.board.pojo.dto.BoardCardInfoDTO>
     * @throws
     * @author Carlos
     * @date 2025-05-13 14:12
     */
    public List<BoardCardInfoDTO> getAllCard() {
        return cardInfoManager.getAllCard();
    }
}
