package com.carlos.system.menu.convert;

import com.carlos.system.menu.pojo.dto.MenuOperateDTO;
import com.carlos.system.menu.pojo.entity.MenuOperate;
import com.carlos.system.menu.pojo.param.MenuOperateCreateParam;
import com.carlos.system.menu.pojo.param.MenuOperateUpdateParam;
import com.carlos.system.menu.pojo.vo.MenuOperateVO;
import com.carlos.system.pojo.ao.MenuOperateAO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 菜单操作 转换器
 * </p>
 *
 * @author carlos
 * @date 2023-7-7 14:19:55
 */
@Mapper(uses = {CommonConvert.class})
public interface MenuOperateConvert {

    MenuOperateConvert INSTANCE = Mappers.getMapper(MenuOperateConvert.class);

    /**
     * 接口新增参数对象转数据传输对象
     *
     * @param param 新增参数
     * @return 数据传输对象
     * @author carlos
     * @date 2023-7-7 14:19:55
     */
    MenuOperateDTO toDTO(MenuOperateCreateParam param);

    /**
     * 接口修改参数对象转数据传输对象
     *
     * @param param 修改参数
     * @return 数据传输对象
     * @author carlos
     * @date 2023-7-7 14:19:55
     */
    MenuOperateDTO toDTO(MenuOperateUpdateParam param);

    /**
     * 持久化对象列表转数据传输对象列表
     *
     * @param dos 数据持久化对象列表
     * @return 数据传输对象列表
     * @author carlos
     * @date 2023-7-7 14:19:55
     */
    List<MenuOperateDTO> toDTO(List<MenuOperate> dos);

    /**
     * 持久化对象转数据传输对象
     *
     * @param entity 数据持久化对象
     * @return 数据传输对象列表
     * @author carlos
     * @date 2023-7-7 14:19:55
     */
    MenuOperateDTO toDTO(MenuOperate entity);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author carlos
     * @date 2023-7-7 14:19:55
     */
    MenuOperate toDO(MenuOperateDTO dto);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dto 数据传输对象
     * @return 数据显示对象
     * @author carlos
     * @date 2023-7-7 14:19:55
     */
    MenuOperateVO toVO(MenuOperateDTO dto);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author carlos
     * @date 2023-7-7 14:19:55
     */
    List<MenuOperateVO> toVO(List<MenuOperate> dos);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param entity 数据传输对象
     * @return 数据显示对象
     * @author carlos
     * @date 2023-7-7 14:19:55
     */
    MenuOperateVO toVO(MenuOperate entity);

    List<MenuOperateVO> toVOS(List<MenuOperateDTO> list);

    List<MenuOperateAO> toAOList(List<MenuOperateDTO> menuOperates);
}
