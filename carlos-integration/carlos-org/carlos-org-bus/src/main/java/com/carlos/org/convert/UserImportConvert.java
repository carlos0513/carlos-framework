package com.carlos.org.convert;

import com.carlos.org.pojo.dto.UserImportDTO;
import com.carlos.org.pojo.entity.UserImport;
import com.carlos.org.pojo.param.UserImportCreateParam;
import com.carlos.org.pojo.param.UserImportUpdateParam;
import com.carlos.org.pojo.vo.UserImportVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 用户导入 转换器
 * </p>
 *
 * @author Carlos
 * @date 2023-5-27 12:52:09
 */
@Mapper(uses = {CommonConvert.class})
public interface UserImportConvert {

    UserImportConvert INSTANCE = Mappers.getMapper(UserImportConvert.class);

    /**
     * 接口新增参数对象转数据传输对象
     *
     * @param param 新增参数
     * @return 数据传输对象
     * @author Carlos
     * @date 2023-5-27 12:52:09
     */
    UserImportDTO toDTO(UserImportCreateParam param);

    /**
     * 接口修改参数对象转数据传输对象
     *
     * @param param 修改参数
     * @return 数据传输对象
     * @author Carlos
     * @date 2023-5-27 12:52:09
     */
    UserImportDTO toDTO(UserImportUpdateParam param);

    /**
     * 持久化对象列表转数据传输对象列表
     *
     * @param dos 数据持久化对象列表
     * @return 数据传输对象列表
     * @author Carlos
     * @date 2023-5-27 12:52:09
     */
    List<UserImportDTO> toDTO(List<UserImport> dos);

    /**
     * 持久化对象转数据传输对象
     *
     * @param entity 数据持久化对象
     * @return 数据传输对象列表
     * @author Carlos
     * @date 2023-5-27 12:52:09
     */
    UserImportDTO toDTO(UserImport entity);

    /**
     * 数据传输对象转数据持久化对象
     *
     * @param dto 数据传输对象
     * @return 数据持久化对象
     * @author Carlos
     * @date 2023-5-27 12:52:09
     */
    UserImport toDO(UserImportDTO dto);

    /**
     * 数据传输对象转数据显示对象
     *
     * @param dto 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2023-5-27 12:52:09
     */
    UserImportVO toVO(UserImportDTO dto);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param dos 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2023-5-27 12:52:09
     */
    List<UserImportVO> toVO(List<UserImport> dos);

    /**
     * 数据持久对象转数据显示对象
     *
     * @param entity 数据传输对象
     * @return 数据显示对象
     * @author Carlos
     * @date 2023-5-27 12:52:09
     */
    UserImportVO toVO(UserImport entity);
}
