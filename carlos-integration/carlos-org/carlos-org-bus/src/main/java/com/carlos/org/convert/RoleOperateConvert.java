package com.carlos.org.convert;

import com.carlos.org.pojo.dto.RoleOperateDTO;
import com.carlos.org.pojo.entity.RoleOperate;
import com.carlos.org.pojo.param.RoleOperateCreateParam;
import com.carlos.org.pojo.param.RoleOperateUpdateParam;
import com.carlos.org.pojo.vo.RoleOperateVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 角色菜单操作表 转换器
 * </p>
 *
 * @author carlos
 * @date 2023-7-7 14:19:55
 */
@Mapper(uses = {CommonConvert.class})
public interface RoleOperateConvert {

    RoleOperateConvert INSTANCE = Mappers.getMapper(RoleOperateConvert.class);

    /**
     * 接口新增参数对象转数据传输对象
     *
     * @param param 新增参数
     * @return 数据传输对象
     * @author carlos
     * @date 2023-7-7 14:19:55
     */
    RoleOperateDTO toDTO(RoleOperateCreateParam param);

    /**
     * 接口修改参数对象转数据传输对象
     *
     * @param param 修改参数
     * @return 数据传输对象
     * @author carlos
     * @date 2023-7-7 14:19:55
     */
    RoleOperateDTO toDTO(RoleOperateUpdateParam param);

    /**
     * 持久化对象列表转数据传输对象列表
     *
     * @param dos 数据持久化对象列表
     * @return 数据传输对象列表
     * @author carlos
     * @date 2023-7-7 14:19:55
     */
    List<RoleOperateDTO> toDTO(List<RoleOperate> dos);

    /**
     * 持久化对象转数据传输对象
     *
     * @param entity 数据持久化对象
     * @return 数据传输对象列表
     * @author carlos
     * @date 2023-7-7 14:19:55
     */
    RoleOperateDTO toDTO(RoleOperate entity);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author carlos
     * @date 2023-7-7 14:19:55
     */
    RoleOperate toDO(RoleOperateDTO dto);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dto 数据传输对象
     * @return 数据显示对象
     * @author carlos
     * @date 2023-7-7 14:19:55
     */
    RoleOperateVO toVO(RoleOperateDTO dto);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author carlos
     * @date 2023-7-7 14:19:55
     */
    List<RoleOperateVO> toVO(List<RoleOperate> dos);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param entity 数据传输对象
     * @return 数据显示对象
     * @author carlos
     * @date 2023-7-7 14:19:55
     */
    RoleOperateVO toVO(RoleOperate entity);
}
