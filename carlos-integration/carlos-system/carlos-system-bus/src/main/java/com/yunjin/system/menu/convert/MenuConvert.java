package com.carlos.system.menu.convert;

import com.carlos.system.config.CommonConvert;
import com.carlos.system.menu.pojo.dto.MenuDTO;
import com.carlos.system.menu.pojo.entity.Menu;
import com.carlos.system.menu.pojo.param.MenuCreateParam;
import com.carlos.system.menu.pojo.param.MenuUpdateParam;
import com.carlos.system.menu.pojo.vo.MenuRecursionVO;
import com.carlos.system.menu.pojo.vo.MenuTreeVO;
import com.carlos.system.menu.pojo.vo.MenuVO;
import com.carlos.system.pojo.ao.MenuAO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * <p>
 * 系统菜单 转换器
 * </p>
 *
 * @author yunjin
 * @date 2021-12-28 15:26:57
 */
@Mapper(uses = CommonConvert.class)
public interface MenuConvert {

    MenuConvert INSTANCE = Mappers.getMapper(MenuConvert.class);

    /**
     * 接口新增参数对象转数据传输对象
     *
     * @param param 新增参数
     * @return 数据传输对象
     * @author yunjin
     * @date 2021-12-28 15:26:57
     */

    @Mapping(target = "parentId", source = "parentId", defaultValue = "0")
    MenuDTO toDTO(MenuCreateParam param);

    /**
     * 接口修改参数对象转数据传输对象
     *
     * @param param 修改参数
     * @return 数据传输对象
     * @author yunjin
     * @date 2021-12-28 15:26:57
     */
    MenuDTO toDTO(MenuUpdateParam param);

    /**
     * 持久化对象列表转数据传输对象列表
     *
     * @param dos 数据持久化对象列表
     * @return 数据传输对象列表
     * @author yunjin
     * @date 2021-12-28 15:26:57
     */
    List<MenuDTO> toDTO(List<Menu> dos);

    /**
     * 持久化对象转数据传输对象
     *
     * @param entity 数据持久化对象
     * @return 数据传输对象列表
     * @author yunjin
     * @date 2021-12-28 15:26:57
     */
    MenuDTO toDTO(Menu entity);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author yunjin
     * @date 2021-12-28 15:26:57
     */
    Menu toDO(MenuDTO dto);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dto 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2021-12-28 15:26:57
     */
    @Mapping(target = "haveChildren", source = "id", qualifiedByName = "haveChildrenMenu")
    MenuVO toVO(MenuDTO dto);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2021-12-28 15:26:57
     */
    List<MenuVO> toVO(List<Menu> dos);

    /**
     * 简单转换
     *
     * @param dtos 数据传输对象
     * @return java.util.List<com.carlos.user.pojo.vo.MenuTreeVO>
     * @author yunjin
     * @date 2021/12/28 18:23
     */
    List<MenuVO> toListVO(List<MenuDTO> dtos);

    /**
     * 简单转换
     *
     * @param dtos 数据传输对象
     * @return java.util.List<com.carlos.user.pojo.vo.MenuTreeVO>
     * @author yunjin
     * @date 2021/12/28 18:23
     */
    List<MenuTreeVO> toTreeListVO(List<MenuDTO> dtos);

    /**
     * 简单转换
     *
     * @param dtos 数据传输对象
     * @return java.util.List<com.carlos.user.pojo.vo.MenuTreeVO>
     * @author yunjin
     * @date 2021/12/28 18:23
     */
    List<MenuRecursionVO> toRecursionListVO(List<MenuDTO> dtos);

    MenuAO toAO(MenuDTO dto);

    List<MenuAO> toAOList(List<MenuDTO> menus);
}
