package com.yunjin.org.apiimpl;


import com.yunjin.core.response.Result;
import com.yunjin.org.api.ApiLabel;
import com.yunjin.org.convert.LabelConvert;
import com.yunjin.org.pojo.ao.LabelAO;
import com.yunjin.org.pojo.dto.LabelDTO;
import com.yunjin.org.service.LabelService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 标签 api接口
 * </p>
 *
 * @author  yunjin
 * @date    2024-3-22 15:36:43
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/org/label")
@Tag(name = "标签Feign接口", hidden = true)
public class LabelAPI implements ApiLabel {

    private final LabelService labelService;
    @Override
    @GetMapping("/id/{id}")
    public Result<LabelAO> getById(@PathVariable(value = "id") String id) {
        LabelDTO byId = labelService.getById(id);
        return Result.ok(LabelConvert.INSTANCE.toAO(byId));
    }

    @Override
    @GetMapping("ids/{ids}")
    public Result<List<LabelAO>> getByIds(@PathVariable(value = "ids") String ids) {
        List<LabelDTO> byIds = labelService.getByIds(ids);
        return Result.ok(LabelConvert.INSTANCE.toAOS(byIds));
    }
}
