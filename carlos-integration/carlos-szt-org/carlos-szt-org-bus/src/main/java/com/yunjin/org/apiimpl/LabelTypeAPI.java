package com.yunjin.org.apiimpl;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * <p>
 * 标签分类 api接口
 * </p>
 *
 * @author  yunjin
 * @date    2024-3-22 15:07:09
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/org/bbt/label/type")
@Tag(name = "标签分类Feign接口", hidden = true)
public class LabelTypeAPI {


}
