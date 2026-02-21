package com.yunjin.org.convert;

import com.yunjin.core.pagination.Paging;
import com.yunjin.org.pojo.ao.OrgUserMessageAO;
import com.yunjin.org.pojo.ao.OrgUserMessageDetailAO;
import com.yunjin.org.pojo.dto.OrgUserMessageDTO;
import com.yunjin.org.pojo.entity.OrgUserMessage;
import com.yunjin.org.pojo.param.ApiOrgUserMessageCreateParam;
import com.yunjin.org.pojo.param.OrgUserMessageCreateParam;
import com.yunjin.org.pojo.param.OrgUserMessageUpdateParam;
import com.yunjin.org.pojo.vo.OrgUserMessageVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 用户消息表 转换器
 * </p>
 *
 * @author yunjin
 * @date 2024-2-28 17:39:16
 */
@Mapper(uses = {CommonConvert.class})
public interface OrgUserMessageConvert {

    OrgUserMessageConvert INSTANCE = Mappers.getMapper(OrgUserMessageConvert.class);

    /**
     * 接口新增参数对象转数据传输对象
     *
     * @param param 新增参数
     * @return 数据传输对象
     * @author yunjin
     * @date 2024-2-28 17:39:16
     */
    OrgUserMessageDTO toDTO(OrgUserMessageCreateParam param);

    OrgUserMessageDTO toDTO(ApiOrgUserMessageCreateParam param);

    /**
     * 接口修改参数对象转数据传输对象
     *
     * @param param 修改参数
     * @return 数据传输对象
     * @author yunjin
     * @date 2024-2-28 17:39:16
     */
    OrgUserMessageDTO toDTO(OrgUserMessageUpdateParam param);

    /**
     * 持久化对象列表转数据传输对象列表
     *
     * @param dos 数据持久化对象列表
     * @return 数据传输对象列表
     * @author yunjin
     * @date 2024-2-28 17:39:16
     */
    List<OrgUserMessageDTO> toDTO(List<OrgUserMessage> dos);

    /**
     * 持久化对象转数据传输对象
     *
     * @param entity 数据持久化对象
     * @return 数据传输对象列表
     * @author yunjin
     * @date 2024-2-28 17:39:16
     */
    OrgUserMessageDTO toDTO(OrgUserMessage entity);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author yunjin
     * @date 2024-2-28 17:39:16
     */
    OrgUserMessage toDO(OrgUserMessageDTO dto);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dto 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2024-2-28 17:39:16
     */
    OrgUserMessageVO toVO(OrgUserMessageDTO dto);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2024-2-28 17:39:16
     */
    List<OrgUserMessageVO> toVO(List<OrgUserMessage> dos);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param entity 数据传输对象
     * @return 数据显示对象
     * @author yunjin
     * @date 2024-2-28 17:39:16
     */
    OrgUserMessageVO toVO(OrgUserMessage entity);

    List<OrgUserMessageAO> toAOS(List<OrgUserMessageVO> records);

    Paging<OrgUserMessageAO> toPageAO(Paging<OrgUserMessageVO> page);

    OrgUserMessageDetailAO toAO(OrgUserMessageDTO messageById);
}
