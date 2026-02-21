package com.carlos.system.news.controller;


import com.carlos.core.pagination.Paging;
import com.carlos.core.param.ParamIdSet;
import com.carlos.log.annotation.Log;
import com.carlos.log.enums.BusinessType;
import com.carlos.system.news.convert.SysNewsConvert;
import com.carlos.system.news.manager.SysNewsManager;
import com.carlos.system.news.pojo.dto.SysNewsDTO;
import com.carlos.system.news.pojo.param.SysNewsCreateParam;
import com.carlos.system.news.pojo.param.SysNewsPageParam;
import com.carlos.system.news.pojo.param.SysNewsUpdateParam;
import com.carlos.system.news.pojo.vo.SysNewsVO;
import com.carlos.system.news.service.SysNewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.Serializable;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p>
 * 系统-通知公告 rest服务接口
 * </p>
 *
 * @author yunjin
 * @date 2022-11-14 23:48:53
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys/news")
@Api(tags = "系统-通知公告")
public class SysNewsController {

    public static final String BASE_NAME = "系统-通知公告";

    private final SysNewsService newsService;

    private final SysNewsManager newsManager;


    @PostMapping
    @ApiOperation(value = "新增" + BASE_NAME)
    @Log(title = BASE_NAME, businessType = BusinessType.INSERT)
    public boolean add(@RequestBody @Validated SysNewsCreateParam param) {
        SysNewsDTO dto = SysNewsConvert.INSTANCE.toDTO(param);
        return this.newsService.addSysNews(dto);
    }

    @PostMapping("delete")
    @ApiOperation(value = "删除" + BASE_NAME)
    @Log(title = BASE_NAME, businessType = BusinessType.DELETE)
    public void delete(@RequestBody ParamIdSet<Serializable> param) {
        this.newsService.deleteSysNews(param.getIds());
    }


    @PostMapping("update")
    @ApiOperation(value = "更新" + BASE_NAME)
    @Log(title = BASE_NAME, businessType = BusinessType.UPDATE)
    public void update(@RequestBody @Validated SysNewsUpdateParam param) {
        SysNewsDTO dto = SysNewsConvert.INSTANCE.toDTO(param);
        this.newsService.updateSysNews(dto);
    }

    @GetMapping("{id}")
    @ApiOperation(value = BASE_NAME + "详情")
    public SysNewsVO detail(@PathVariable String id) {
        return SysNewsConvert.INSTANCE.toVO(this.newsManager.getDtoById(id));
    }

    @GetMapping("page")
    @ApiOperation(value = BASE_NAME + "分页列表")
    public Paging<SysNewsVO> page(SysNewsPageParam param) {
        return this.newsManager.getPage(param);
    }
}
