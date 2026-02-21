package com.yunjin.org.manager;

import com.yunjin.core.pagination.Paging;
import com.yunjin.datasource.base.BaseService;
import com.yunjin.org.pojo.dto.HelpFileDTO;
import com.yunjin.org.pojo.entity.HelpFile;
import com.yunjin.org.pojo.param.HelpFilePageParam;
import com.yunjin.org.pojo.vo.HelpFileVO;

import java.util.List;
import java.util.Set;

public interface HelpFileManager extends BaseService<HelpFile>{
    Boolean isExist(String fileName);

    Boolean add(HelpFileDTO dto);

    HelpFileDTO getDtoById(String id);

    Paging<HelpFileVO> getPage(HelpFilePageParam param);

    void deleteHelpFile(Set<String> ids);

    Boolean delete(String id);

    Boolean modify(HelpFileDTO dto);

    /**
     * 获取所有记录
     *
     * @return java.util.List<com.yunjin.org.pojo.dto.HelpFileDTO>
     * @throws
     * @author Carlos
     * @date 2025-05-19 13:46
     */
    List<HelpFileDTO> listAll();
}
