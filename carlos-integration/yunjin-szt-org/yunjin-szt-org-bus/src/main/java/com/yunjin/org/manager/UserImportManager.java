package com.yunjin.org.manager;

import com.yunjin.core.pagination.Paging;
import com.yunjin.datasource.base.BaseService;
import com.yunjin.org.pojo.dto.UserImportDTO;
import com.yunjin.org.pojo.entity.UserImport;
import com.yunjin.org.pojo.param.UserImportPageParam;
import com.yunjin.org.pojo.vo.UserImportVO;

import java.io.Serializable;

/**
 * <p>
 * 用户导入 查询封装接口
 * </p>
 *
 * @author Carlos
 * @date 2023-5-27 12:52:09
 */
public interface UserImportManager extends BaseService<UserImport> {

    /**
     * 新增用户导入
     *
     * @param dto 用户导入数据
     * @return boolean
     * @author Carlos
     * @date 2023-5-27 12:52:09
     */
    boolean add(UserImportDTO dto);

    /**
     * 删除用户导入
     *
     * @param id 用户导入id
     * @return boolean
     * @author Carlos
     * @date 2023-5-27 12:52:09
     */
    boolean delete(Serializable id);

    /**
     * 修改用户导入信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author Carlos
     * @date 2023-5-27 12:52:09
     */
    boolean modify(UserImportDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.yunjin.org.pojo.dto.UserImportDTO
     * @author Carlos
     * @date 2023-5-27 12:52:09
     */
    UserImportDTO getDtoById(Serializable id);

    /**
     * 分页列表
     *
     * @param param 分页参数
     * @author Carlos
     * @date 2023-5-27 12:52:09
     */
    Paging<UserImportVO> getPage(UserImportPageParam param);
}
