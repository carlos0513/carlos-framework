package com.carlos.system.dict.apiimpl;


import com.carlos.core.base.Dict;
import com.carlos.core.exception.RestException;
import com.carlos.core.response.Result;
import com.carlos.system.api.ApiDict;
import com.carlos.system.dict.convert.SysDictItemConvert;
import com.carlos.system.dict.manager.SysDictCacheManager;
import com.carlos.system.dict.pojo.dto.SysDictItemDTO;
import com.carlos.system.dict.service.SysDictItemService;
import com.carlos.system.pojo.ao.DictItemAO;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 系统字典详情 api
 * </p>
 *
 * @author carlos
 * @date 2021-11-22 14:49:00
 */
// @Hidden
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sys/dict")
@Tag(name = "字典feign接口")
@Hidden
public class ApiDictImpl implements ApiDict {

    private final SysDictItemService dictItemService;

    private final SysDictCacheManager cacheManager;

    @Override
    @GetMapping("/item/id")
    @Operation(summary = "根据id获取字典选项详情")
    public Result<Dict> getById(@RequestParam(value = "id") String id) {
        if (id == null) {
            throw new RestException("字典选项id不能为空");
        }
        Dict dict = cacheManager.getDictByItemId(id);
        return Result.ok(dict);
    }

    @Override
    @GetMapping("/item/code")
    @Operation(summary = "根据字典选项code获取字典详情")
    public Result<Dict> getByCode(@RequestParam(value = "code") String code) {
        if (StringUtils.isBlank(code)) {
            throw new RestException("字典选项code不能为空");
        }
        Dict dict = cacheManager.getDictByItemCode(code);
        return Result.ok(dict);
    }

    @Override
    @GetMapping("/item/list")
    @Operation(summary = "获取字典选项列表")
    public Result<List<DictItemAO>> list(@RequestParam(value = "code") String code) {
        if (StringUtils.isBlank(code)) {
            throw new RestException("字典code不能为空");
        }
        List<SysDictItemDTO> dtos = dictItemService.getItemCache(code);
        return Result.ok(SysDictItemConvert.INSTANCE.toAOList(dtos));
    }
}
