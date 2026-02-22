package com.carlos.system.dict.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carlos.system.dict.pojo.entity.SysDict;
import com.carlos.system.dict.pojo.param.SysDictPageParam;
import com.carlos.system.dict.pojo.vo.SysDictVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 系统字典 查询接口
 * </p>
 *
 * @author carlos
 * @date 2021-11-22 14:49:00
 */
@Mapper
public interface SysDictMapper extends BaseMapper<SysDict> {


    /**
     * 查询分页
     *
     * @param page  分页信息
     * @param param 查询参数
     * @author carlos
     * @date 2021-11-22 14:49:00
     */
    IPage<SysDictVO> selectOwnPage(Page page, @Param("param") SysDictPageParam param);
}
