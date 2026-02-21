package com.carlos.system.news.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.carlos.core.pagination.Paging;
import com.carlos.core.param.ParamIdSet;
import com.carlos.core.response.Result;
import com.carlos.system.AuthorConstant;
import com.carlos.system.news.convert.SysViewRecordConvert;
import com.carlos.system.news.manager.SysViewRecordManager;
import com.carlos.system.news.pojo.dto.SysViewRecordDTO;
import com.carlos.system.news.pojo.param.SysViewRecordCreateParam;
import com.carlos.system.news.pojo.param.SysViewRecordPageParam;
import com.carlos.system.news.pojo.param.SysViewRecordUpdateParam;
import com.carlos.system.news.pojo.vo.SysViewRecordVO;
import com.carlos.system.news.service.SysViewRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
 * 浏览记录 rest服务接口
 * </p>
 *
 * @author yunjin
 * @date 2023-1-13 16:31:50
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys/view/record")
@Api(tags = "浏览记录")
public class SysViewRecordController {

    public static final String BASE_NAME = "浏览记录";

    private final SysViewRecordService viewRecordService;

    private final SysViewRecordManager viewRecordManager;


    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @PostMapping
    @ApiOperation(value = "新增" + BASE_NAME)
    public void add(@RequestBody @Validated SysViewRecordCreateParam param) {
        SysViewRecordDTO dto = SysViewRecordConvert.INSTANCE.toDTO(param);
        this.viewRecordService.addSysViewRecord(dto);
    }

    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @PostMapping("delete")
    @ApiOperation(value = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<String> param) {
        this.viewRecordService.deleteSysViewRecord(param.getIds());
    }

    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @PostMapping("update")
    @ApiOperation(value = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated SysViewRecordUpdateParam param) {
        SysViewRecordDTO dto = SysViewRecordConvert.INSTANCE.toDTO(param);
        this.viewRecordService.updateSysViewRecord(dto);
    }

    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @GetMapping("{id}")
    @ApiOperation(value = BASE_NAME + "详情")
    public SysViewRecordVO detail(@PathVariable String id) {
        return SysViewRecordConvert.INSTANCE.toVO(this.viewRecordManager.getDtoById(id));
    }

    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @GetMapping("page")
    @ApiOperation(value = BASE_NAME + "分页列表")
    public Paging<SysViewRecordVO> page(SysViewRecordPageParam param) {
        return this.viewRecordManager.getPage(param);
    }

    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @GetMapping("getCount/{type}")
    @ApiOperation(value = "未读统计-" + BASE_NAME)
    public Result<Long> getCount(@PathVariable int type) {
        return this.viewRecordService.getCount(type);
    }
}
