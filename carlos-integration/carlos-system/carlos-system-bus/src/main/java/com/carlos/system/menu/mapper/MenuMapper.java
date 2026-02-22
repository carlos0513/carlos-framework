package com.carlos.system.menu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.carlos.system.menu.pojo.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 系统菜单 查询接口
 * </p>
 *
 * @author yunjin
 * @date 2021-12-28 15:26:57
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {


}
