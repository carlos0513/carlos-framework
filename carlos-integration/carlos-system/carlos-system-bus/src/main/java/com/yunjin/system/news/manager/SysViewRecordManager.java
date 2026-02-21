package com.carlos.system.news.manager;

import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseService;
import com.carlos.system.news.pojo.dto.SysViewRecordDTO;
import com.carlos.system.news.pojo.entity.SysViewRecord;
import com.carlos.system.news.pojo.param.SysViewRecordPageParam;
import com.carlos.system.news.pojo.vo.SysViewRecordVO;
import java.io.Serializable;

/**
 * <p>
 * 浏览记录 查询封装接口
 * </p>
 *
 * @author yunjin
 * @date 2023-1-13 16:31:50
 */
public interface SysViewRecordManager extends BaseService<SysViewRecord> {

    /**
     * 新增浏览记录
     *
     * @param dto 浏览记录数据
     * @return boolean
     * @author yunjin
     * @date 2023-1-13 16:31:50
     */
    boolean add(SysViewRecordDTO dto);

    /**
     * 删除浏览记录
     *
     * @param id 浏览记录id
     * @return boolean
     * @author yunjin
     * @date 2023-1-13 16:31:50
     */
    boolean delete(Serializable id);

    /**
     * 修改浏览记录信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author yunjin
     * @date 2023-1-13 16:31:50
     */
    boolean modify(SysViewRecordDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.carlos.system.news.pojo.dto.SysViewRecordDTO
     * @author yunjin
     * @date 2023-1-13 16:31:50
     */
    SysViewRecordDTO getDtoById(Serializable id);

    /**
     * 分页列表
     *
     * @param param 分页参数
     * @author yunjin
     * @date 2023-1-13 16:31:50
     */
    Paging<SysViewRecordVO> getPage(SysViewRecordPageParam param);

    long getCountByUserId(Serializable userId, int type);

    SysViewRecordDTO getByUserIdAndReferenceId(Serializable userId, String newsId);
}
