package com.yunjin.board.convert;

import com.yunjin.board.pojo.dto.BoardCardInfoDTO;
import com.yunjin.board.pojo.entity.BoardCardInfo;
import com.yunjin.board.pojo.param.BoardCardInfoCreateParam;
import com.yunjin.board.pojo.param.BoardCardInfoUpdateParam;
import com.yunjin.board.pojo.vo.BoardCardInfoVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 工作台卡片信息 转换器
 * </p>
 *
 * @author Carlos
 * @date 2025-5-13 11:07:57
 */
@Mapper(uses = {CommonConvert.class})
public interface BoardCardInfoConvert {

    BoardCardInfoConvert INSTANCE = Mappers.getMapper(BoardCardInfoConvert.class);

    /**
     * 接口新增参数对象转数据传输对象
     *
     * @param param 新增参数
     * @return 数据传输对象
     * @author Carlos
     * @date 2025-5-13 11:07:57
     */
    BoardCardInfoDTO toDTO(BoardCardInfoCreateParam param);

    /**
     * 接口修改参数对象转数据传输对象
     *
     * @param param 修改参数
     * @return 数据传输对象
     * @author Carlos
     * @date 2025-5-13 11:07:57
     */
    BoardCardInfoDTO toDTO(BoardCardInfoUpdateParam param);

    /**
     * 持久化对象列表转数据传输对象列表
     *
     * @param dos 数据持久化对象列表
     * @return 数据传输对象列表
     * @author Carlos
     * @date 2025-5-13 11:07:57
     */
    List<BoardCardInfoDTO> toDTO(List<BoardCardInfo> dos);

    /**
     * 持久化对象转数据传输对象
     *
     * @param entity 数据持久化对象
     * @return 数据传输对象列表
     * @author Carlos
     * @date 2025-5-13 11:07:57
     */
    BoardCardInfoDTO toDTO(BoardCardInfo entity);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author Carlos
     * @date 2025-5-13 11:07:57
     */
    BoardCardInfo toDO(BoardCardInfoDTO dto);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dto 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2025-5-13 11:07:57
     */
    BoardCardInfoVO toVO(BoardCardInfoDTO dto);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2025-5-13 11:07:57
     */
    List<BoardCardInfoVO> toVO(List<BoardCardInfo> dos);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param entity 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2025-5-13 11:07:57
     */
    BoardCardInfoVO toVO(BoardCardInfo entity);
}
