package com.carlos.system.news.apiimpl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.carlos.core.exception.ServiceException;
import com.carlos.core.response.Result;
import com.carlos.system.api.ApiSysNews;
import com.carlos.system.news.convert.SysNewsConvert;
import com.carlos.system.news.manager.SysNewsManager;
import com.carlos.system.news.pojo.dto.SysNewsDTO;
import com.carlos.system.news.pojo.vo.SysNewsVO;
import com.carlos.system.pojo.ao.SysNewsAO;
import com.carlos.system.pojo.ao.SysNewsDetailAO;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 系统通知 api
 * </p>
 *
 * @author yunjin
 * @date 2021-11-22 14:49:00
 */
@ApiIgnore
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sys/news")
@Api(tags = "系统通知feign接口", hidden = true)
public class ApiSystemNewsImpl implements ApiSysNews {

    private final SysNewsManager sysNewsManager;


    @Override
    public Result<List<SysNewsAO>> list() {
        List<SysNewsDTO> news = sysNewsManager.listAll();
        return Result.ok(SysNewsConvert.INSTANCE.toAOs(news));
    }

    @Override
    public Result<SysNewsDetailAO> getById(String id) {
        SysNewsVO vo = SysNewsConvert.INSTANCE.toVO(sysNewsManager.getDtoById(id));
        SysNewsDetailAO sysNewsDetailAO = SysNewsConvert.INSTANCE.toAO(vo);
        return Result.ok(sysNewsDetailAO);
    }

    @Override
    public boolean addSysNews(SysNewsAO ao) {
        SysNewsDTO dto = SysNewsConvert.INSTANCE.toDTO(ao);
        if (StrUtil.isBlank(dto.getTitle())) {
            throw new ServiceException("新增失败，标题不可为空！");
        }
        if (StrUtil.isBlank(dto.getContent())) {
            throw new ServiceException("内容不可为空，标题不可为空！");
        }
        boolean success = this.sysNewsManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            return false;
        }
        Serializable id = dto.getId();
        // 保存完成的后续业务
        return true;
    }
}
