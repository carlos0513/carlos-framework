package com.carlos.system.dict.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Sets;
import com.carlos.core.param.ParamIdSet;
import com.carlos.log.annotation.Log;
import com.carlos.log.enums.BusinessType;
import com.carlos.system.dict.convert.SysDictConvert;
import com.carlos.system.dict.manager.SysDictCacheManager;
import com.carlos.system.dict.manager.SysDictManager;
import com.carlos.system.dict.pojo.dto.SysDictDTO;
import com.carlos.system.dict.pojo.param.SysDictCreateParam;
import com.carlos.system.dict.pojo.param.SysDictPageParam;
import com.carlos.system.dict.pojo.param.SysDictUpdateParam;
import com.carlos.system.dict.pojo.vo.SysDictListVO;
import com.carlos.system.dict.pojo.vo.SysDictVO;
import com.carlos.system.dict.service.SysDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 系统字典 rest服务接口
 * </p>
 *
 * @author yunjin
 * @date 2021-11-22 14:49:00
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys/dict")
@Api(tags = "系统字典类型")
public class SysDictController {

    public static final String BASE_NAME = "字典";

    private final SysDictService dictService;

    private final SysDictManager dictManager;

    private final SysDictCacheManager cacheManager;


    @PostMapping
    @ApiOperation(value = "新增" + BASE_NAME)
    @Log(title = "系统字典", businessType = BusinessType.INSERT)
    public boolean add(@RequestBody @Validated SysDictCreateParam param) {
        SysDictDTO dto = SysDictConvert.INSTANCE.toDTO(param);
        return this.dictService.addDict(dto, param.getItems());
    }

    @PostMapping("delete")
    @ApiOperation(value = "删除" + BASE_NAME)
    @Log(title = "系统字典", businessType = BusinessType.DELETE)
    public boolean delete(@RequestBody ParamIdSet<String> param) {
        return this.dictService.deleteDict(Sets.newHashSet(param.getIds()));
    }

    @PostMapping("update")
    @ApiOperation(value = "更新" + BASE_NAME)
    @Log(title = "系统字典", businessType = BusinessType.UPDATE)
    public boolean update(@RequestBody @Validated SysDictUpdateParam command) {
        SysDictDTO dto = SysDictConvert.INSTANCE.toDTO(command);
        return this.dictService.update(dto, command.getItems());
    }


    @GetMapping("{id}")
    @ApiOperation(value = BASE_NAME + "详情")
    public SysDictVO detail(@PathVariable String id) {
        return SysDictConvert.INSTANCE.toVO(this.dictService.getDetail(id));
    }

    @GetMapping("list")
    @ApiOperation(value = BASE_NAME + "下拉列表", notes = "支持按名称过滤")
    public List<SysDictListVO> list(String name) {
        return SysDictConvert.INSTANCE.toVO(this.dictService.getListByName(name));
    }

    @GetMapping("full/list")
    @ApiOperation(value = BASE_NAME + "下拉列表 + 选项列表", notes = "支持按名称过滤")
    public List<SysDictListVO> fullList(String name) {
        return SysDictConvert.INSTANCE.toVO(this.dictService.getListByName(name));
    }

    @GetMapping("page")
    @ApiOperation(value = BASE_NAME + "分页列表")
    public IPage<SysDictVO> page(SysDictPageParam param) {
        return this.dictManager.getPage(param);
    }


    @GetMapping("initWithTxt")
    @ApiOperation(value = "使用txt文本初始化")
    public void initWithTxt(String path) {
        dictService.initWithTxt(path);
    }


    @PostMapping("cache/refresh")
    @ApiOperation(value = "强制刷新" + BASE_NAME)
    @Log(title = "系统字典强制刷新", businessType = BusinessType.REFRESH)
    public boolean refresh() {
        cacheManager.clearCache();
        this.cacheManager.initCache();
        return true;
    }

}
