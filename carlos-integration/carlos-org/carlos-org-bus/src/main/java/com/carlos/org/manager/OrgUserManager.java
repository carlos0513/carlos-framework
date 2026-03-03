package com.carlos.org.manager;

import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseService;
import com.carlos.org.pojo.dto.OrgUserDTO;
import com.carlos.org.pojo.entity.OrgUser;
import com.carlos.org.pojo.param.OrgUserPageParam;
import com.carlos.org.pojo.vo.OrgUserVO;

import java.io.Serializable;

/**
 * <p>
 * 系统用户 查询封装接口
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
public interface OrgUserManager extends BaseService<OrgUser> {

    /**
     * 新增系统用户
     *
     * @param dto 系统用户数据
     * @return boolean
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    boolean add(OrgUserDTO dto);

    /**
     * 删除系统用户
     *
     * @param id 系统用户id
     * @return boolean
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    boolean delete(Serializable id);

    /**
     * 修改系统用户信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    boolean modify(OrgUserDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.carlos.org.pojo.dto.OrgUserDTO
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    OrgUserDTO getDtoById(Serializable id);

    /**
     * 分页列表
     *
     * @param  param 分页参数
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    Paging<OrgUserVO> getPage(OrgUserPageParam param);

    // 逻辑删除由框架自动处理，使用removeById即可

    /**
     * 根据账号查询用户
     *
     * @param account 账号
     * @return OrgUserDTO
     */
    OrgUserDTO getUserByAccount(String account);

    /**
     * 根据手机号查询用户
     *
     * @param phone 手机号
     * @return OrgUserDTO
     */
    OrgUserDTO getUserByPhone(String phone);
}
