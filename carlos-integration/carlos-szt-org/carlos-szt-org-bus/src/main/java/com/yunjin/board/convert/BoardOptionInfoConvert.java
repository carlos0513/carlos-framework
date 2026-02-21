package com.yunjin.board.convert;

import com.yunjin.board.pojo.dto.BoardOptionInfoDTO;
import com.yunjin.board.pojo.entity.BoardOptionInfo;
import com.yunjin.board.pojo.param.BoardOptionInfoCreateParam;
import com.yunjin.board.pojo.param.BoardOptionInfoUpdateParam;
import com.yunjin.board.pojo.vo.BoardOptionInfoVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 工作台卡片选项信息 转换器
 * </p>
 *
 * @author Carlos
 * @date 2025-5-13 11:07:57
 */
@Mapper(uses = {CommonConvert.class})
public interface BoardOptionInfoConvert {

    BoardOptionInfoConvert INSTANCE = Mappers.getMapper(BoardOptionInfoConvert.class);

    /**
     * 接口新增参数对象转数据传输对象
     *
     * @param param 新增参数
     * @return 数据传输对象
     * @author Carlos
     * @date 2025-5-13 11:07:57
     */
    BoardOptionInfoDTO toDTO(BoardOptionInfoCreateParam param);

    /**
     * 接口修改参数对象转数据传输对象
     *
     * @param param 修改参数
     * @return 数据传输对象
     * @author Carlos
     * @date 2025-5-13 11:07:57
     */
    BoardOptionInfoDTO toDTO(BoardOptionInfoUpdateParam param);

    /**
     * 持久化对象列表转数据传输对象列表
     *
     * @param dos 数据持久化对象列表
     * @return 数据传输对象列表
     * @author Carlos
     * @date 2025-5-13 11:07:57
     */
    List<BoardOptionInfoDTO> toDTO(List<BoardOptionInfo> dos);

    /**
     * 持久化对象转数据传输对象
     *
     * @param entity 数据持久化对象
     * @return 数据传输对象列表
     * @author Carlos
     * @date 2025-5-13 11:07:57
     */
    BoardOptionInfoDTO toDTO(BoardOptionInfo entity);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author Carlos
     * @date 2025-5-13 11:07:57
     */
    BoardOptionInfo toDO(BoardOptionInfoDTO dto);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dto 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2025-5-13 11:07:57
     */
    BoardOptionInfoVO toVO(BoardOptionInfoDTO dto);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2025-5-13 11:07:57
     */
    List<BoardOptionInfoVO> toVO(List<BoardOptionInfo> dos);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param entity 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2025-5-13 11:07:57
     */
    BoardOptionInfoVO toVO(BoardOptionInfo entity);
}
