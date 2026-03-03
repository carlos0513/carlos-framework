package com.carlos.org.position.apiimpl;


import com.carlos.org.position.api.ApiOrgUserPosition;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户岗位职级关联表（核心任职信息） api接口
 * </p>
 *
 * @author Carlos
 * @date 2026年3月3日 下午11:19:10
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/org/user/position")
@Tag(name = "用户岗位职级关联表（核心任职信息）Feign接口")
public class ApiOrgUserPositionImpl implements ApiOrgUserPosition {


}
