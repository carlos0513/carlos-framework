package com.yunjin.org.convert;

import com.yunjin.org.pojo.dto.HelpFileDTO;
import com.yunjin.org.pojo.entity.HelpFile;
import com.yunjin.org.pojo.param.HelpFileCreateParam;
import com.yunjin.org.pojo.param.HelpFileUpdateParam;
import com.yunjin.org.pojo.vo.HelpFileVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = {CommonConvert.class})
public interface HelpFileConvert {
    HelpFileConvert INSTANCE = Mappers.getMapper(HelpFileConvert.class);

    HelpFileDTO toDTO(HelpFileCreateParam param);

    HelpFileDTO toDTO(HelpFileUpdateParam param);
    HelpFileDTO toDTO(HelpFile helpFile);
    HelpFile toDO(HelpFileDTO dto);

    HelpFileVO toVO(HelpFile dtoById);

    HelpFileVO toVO(HelpFileDTO dto);

    List<HelpFileVO> toVO(List<HelpFile> dos);

    List<HelpFileDTO> toDTO(List<HelpFile> list);
}
