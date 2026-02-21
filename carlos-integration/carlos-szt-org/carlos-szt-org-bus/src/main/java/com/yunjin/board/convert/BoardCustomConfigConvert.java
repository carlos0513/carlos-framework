package com.yunjin.board.convert;

import com.yunjin.board.pojo.dto.BoardCustomConfigDTO;
import com.yunjin.board.pojo.dto.BoardCustomConfigDetailDTO;
import com.yunjin.board.pojo.entity.BoardCustomConfig;
import com.yunjin.board.pojo.param.BoardCustomConfigModifyParam;
import com.yunjin.board.pojo.param.BoardCustomConfigUpdateParam;
import com.yunjin.board.pojo.vo.BoardCustomConfigVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 看板自定义配置 转换器
 * </p>
 *
 * @author Carlos
 * @date 2025-5-13 11:07:57
 */
@Mapper(uses = {CommonConvert.class})
public interface BoardCustomConfigConvert {

    BoardCustomConfigConvert INSTANCE = Mappers.getMapper(BoardCustomConfigConvert.class);

    /**
     * 接口新增参数对象转数据传输对象
     *
     * @param param 新增参数
     * @return 数据传输对象
     * @author Carlos
     * @date 2025-5-13 11:07:57
     */
    BoardCustomConfigDTO toDTO(BoardCustomConfigModifyParam param);

    /**
     * 接口修改参数对象转数据传输对象
     *
     * @param param 修改参数
     * @return 数据传输对象
     * @author Carlos
     * @date 2025-5-13 11:07:57
     */
    BoardCustomConfigDTO toDTO(BoardCustomConfigUpdateParam param);

    /**
     * 持久化对象列表转数据传输对象列表
     *
     * @param dos 数据持久化对象列表
     * @return 数据传输对象列表
     * @author Carlos
     * @date 2025-5-13 11:07:57
     */
    List<BoardCustomConfigDTO> toDTO(List<BoardCustomConfig> dos);

    /**
     * 持久化对象转数据传输对象
     *
     * @param entity 数据持久化对象
     * @return 数据传输对象列表
     * @author Carlos
     * @date 2025-5-13 11:07:57
     */
    BoardCustomConfigDTO toDTO(BoardCustomConfig entity);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author Carlos
     * @date 2025-5-13 11:07:57
     */
    BoardCustomConfig toDO(BoardCustomConfigDTO dto);


    List<BoardCustomConfigVO> toConfigVO(List<BoardCustomConfigDetailDTO> userConfig);

    List<BoardCustomConfigDetailDTO> toConfigDetail(List<BoardCustomConfigModifyParam.Item> items);
}
