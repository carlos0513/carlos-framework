package com.carlos.org.service;

import com.carlos.org.manager.OrgUserManager;
import com.carlos.org.pojo.dto.OrgUserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Set;


/**
 * <p>
 * 系统用户 业务
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrgUserService {

    private final OrgUserManager userManager;

    /**
     * 新增系统用户
     *
     * @param dto 系统用户数据
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    public void addOrgUser(OrgUserDTO dto) {
        boolean success = userManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            return;
        }
        Serializable id = dto.getId();
        log.info("Insert 'OrgUser' data: id:{}", id);
        // 保存完成的后续业务
    }

    /**
     * 删除系统用户
     *
     * @param ids 系统用户id
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    public void deleteOrgUser(Set<Serializable> ids) {
        for (Serializable id : ids) {
            boolean success = userManager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }
            // 删除成功的后续业务
        }
    }

    /**
     * 修改系统用户信息
     *
     * @param dto 对象信息
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    public void updateOrgUser(OrgUserDTO dto) {
        boolean success = userManager.modify(dto);
        if (!success) {
            // 修改失败操作
            return;
        }
        // 修改成功的后续操作
        log.info("Update 'OrgUser' data: id:{}", dto.getId());
    }

}
