package com.carlos.system.dict.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Sets;
import com.carlos.core.exception.RestException;
import com.carlos.core.param.ParamIdSet;
import com.carlos.log.annotation.Log;
import com.carlos.log.enums.BusinessType;
import com.carlos.system.dict.convert.SysDictItemConvert;
import com.carlos.system.dict.manager.SysDictItemManager;
import com.carlos.system.dict.pojo.dto.SysDictItemDTO;
import com.carlos.system.dict.pojo.param.SysDictItemCreateParam;
import com.carlos.system.dict.pojo.param.SysDictItemPageParam;
import com.carlos.system.dict.pojo.param.SysDictItemUpdateParam;
import com.carlos.system.dict.pojo.vo.SysDictItemListVO;
import com.carlos.system.dict.pojo.vo.SysDictItemVO;
import com.carlos.system.dict.service.SysDictItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 系统字典详情 rest服务接口
 * </p>
 *
 * @author yunjin
 * @date 2021-11-22 14:49:00
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys/dict/item")
@Api(tags = "系统字典选项")
public class SysDictItemController {

    public static final String BASE_NAME = "字典选项";

    private final SysDictItemService dictItemService;

    private final SysDictItemManager dictItemManager;


    @PostMapping
    @ApiOperation(value = "新增" + BASE_NAME)
    @Log(title = "系统字典选项", businessType = BusinessType.INSERT)
    public void add(@RequestBody @Validated SysDictItemCreateParam param) {
        SysDictItemDTO dto = SysDictItemConvert.INSTANCE.toDTO(param);
        this.dictItemService.addDictItem(param.getDictId(), Collections.singletonList(dto));
    }

    @PostMapping("delete")
    @ApiOperation(value = "删除" + BASE_NAME)
    @Log(title = "系统字典选项", businessType = BusinessType.DELETE)
    public boolean delete(@RequestBody ParamIdSet<String> param) {
        return this.dictItemService.deleteDictItem(param.getIds());
    }


    @PostMapping("update")
    @ApiOperation(value = "修改" + BASE_NAME)
    @Log(title = "系统字典选项", businessType = BusinessType.UPDATE)
    public boolean update(@RequestBody @Validated SysDictItemUpdateParam command) {
        SysDictItemDTO dto = SysDictItemConvert.INSTANCE.toDTO(command);
        return this.dictItemService.updateDictItem(dto);
    }

    @GetMapping("info/{id}")
    @ApiOperation(value = BASE_NAME + "详情")
    @Log(title = "系统字典选项", businessType = BusinessType.QUERY_DETAIL)
    public SysDictItemVO detail(@PathVariable String id) {
        return SysDictItemConvert.INSTANCE.toVO(this.dictItemManager.getItemById(id));
    }

    @GetMapping("page")
    @ApiOperation(value = BASE_NAME + "分页列表")
    @Log(title = "系统字典选项", businessType = BusinessType.QUERY)
    public IPage<SysDictItemVO> page(SysDictItemPageParam param) {
        return this.dictItemManager.getPage(param);
    }

    @GetMapping("list")
    @ApiOperation(value = BASE_NAME + "列表")
    @Log(title = "系统字典选项", businessType = BusinessType.QUERY)
    public List<SysDictItemVO> list(String dictId) {
        return SysDictItemConvert.INSTANCE.toListVO(this.dictItemManager.listItems(Sets.newHashSet(dictId), null, true));
    }

    @GetMapping("select")
    @ApiOperation(value = BASE_NAME + "下拉列表")
    @Log(title = "系统字典选项", businessType = BusinessType.QUERY)
    public List<SysDictItemListVO> select(String code, String name) {
        if (StringUtils.isBlank(code)) {
            throw new RestException("字典code不能为空");
        }
        return SysDictItemConvert.INSTANCE.toVO(this.dictItemService.getItemsByDictCode(code, name));
    }

}
