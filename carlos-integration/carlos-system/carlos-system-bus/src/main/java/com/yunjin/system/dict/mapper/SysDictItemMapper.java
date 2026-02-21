package com.carlos.system.dict.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carlos.system.dict.pojo.entity.SysDictItem;
import com.carlos.system.dict.pojo.param.SysDictItemPageParam;
import com.carlos.system.dict.pojo.vo.SysDictItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 系统字典详情 查询接口
 * </p>
 *
 * @author yunjin
 * @date 2021-11-22 14:49:00
 */
@Mapper
public interface SysDictItemMapper extends BaseMapper<SysDictItem> {

    /**
     * 查询分页
     *
     * @param page  分页信息
     * @param param 查询参数
     * @author yunjin
     * @date 2021-11-22 14:49:00
     */
    IPage<SysDictItemVO> selectOwnPage(Page page, @Param("param") SysDictItemPageParam param);
}
