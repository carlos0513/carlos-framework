package com.carlos.org.convert;


import com.carlos.org.pojo.dto.OrgUserMenuDTO;
import com.carlos.org.pojo.entity.OrgUserMenu;
import com.carlos.org.pojo.param.OrgUserMenuCreateParam;
import com.carlos.org.pojo.param.OrgUserMenuUpdateParam;
import com.carlos.org.pojo.vo.OrgUserMenuVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 用户菜单收藏表 转换器
 * </p>
 *
 * @author carlos
 * @date 2024-2-28 11:10:01
 */
@Mapper(uses = {CommonConvert.class})
public interface OrgUserMenuConvert {

    OrgUserMenuConvert INSTANCE = Mappers.getMapper(OrgUserMenuConvert.class);

    /**
     * 接口新增参数对象转数据传输对象
     *
     * @param param 新增参数
     * @return 数据传输对象
     * @author carlos
     * @date 2024-2-28 11:10:01
     */
    OrgUserMenuDTO toDTO(OrgUserMenuCreateParam param);

    /**
     * 接口修改参数对象转数据传输对象
     *
     * @param param 修改参数
     * @return 数据传输对象
     * @author carlos
     * @date 2024-2-28 11:10:01
     */
    OrgUserMenuDTO toDTO(OrgUserMenuUpdateParam param);

    /**
     * 持久化对象列表转数据传输对象列表
     *
     * @param dos 数据持久化对象列表
     * @return 数据传输对象列表
     * @author carlos
     * @date 2024-2-28 11:10:01
     */
    List<OrgUserMenuDTO> toDTO(List<OrgUserMenu> dos);

    /**
     * 持久化对象转数据传输对象
     *
     * @param entity 数据持久化对象
     * @return 数据传输对象列表
     * @author carlos
     * @date 2024-2-28 11:10:01
     */
    OrgUserMenuDTO toDTO(OrgUserMenu entity);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author carlos
     * @date 2024-2-28 11:10:01
     */
    OrgUserMenu toDO(OrgUserMenuDTO dto);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dto 数据传输对象
     * @return 数据显示对象
     * @author carlos
     * @date 2024-2-28 11:10:01
     */
    OrgUserMenuVO toVO(OrgUserMenuDTO dto);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author carlos
     * @date 2024-2-28 11:10:01
     */
    List<OrgUserMenuVO> toVO(List<OrgUserMenu> dos);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param entity 数据传输对象
     * @return 数据显示对象
     * @author carlos
     * @date 2024-2-28 11:10:01
     */
    OrgUserMenuVO toVO(OrgUserMenu entity);
}
