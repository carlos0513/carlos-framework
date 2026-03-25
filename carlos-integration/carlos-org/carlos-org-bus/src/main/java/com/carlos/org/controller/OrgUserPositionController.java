package com.carlos.org.controller;


import com.carlos.core.response.Result;
import com.carlos.org.pojo.param.OrgUserAssignPositionParam;
import com.carlos.org.service.OrgUserPositionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户岗位管理 rest服务接口
 * </p>
 * <p>
 * 实现UM-014用户分配岗位功能
 *
 * @author carlos
 * @date 2026-03-03
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("org/user/position")
@Tag(name = "用户岗位管理")
@Slf4j
public class OrgUserPositionController {

    public static final String BASE_NAME = "用户岗位";

    private final OrgUserPositionService userPositionService;

    /**
     * 用户分配岗位
     * 实现UM-014需求
     *
     * @param param 分配参数
     * @return 结果
     */
    @PostMapping("assign")
    @Operation(summary = "用户分配岗位")
    public Result<Boolean> assignPosition(@RequestBody @Validated OrgUserAssignPositionParam param) {
        userPositionService.assignPosition(param);
        return Result.success(true);
    }

    /**
     * 用户卸任岗位
     *
     * @param userId     用户ID
     * @param positionId 岗位ID
     * @return 结果
     */
    @PostMapping("resign")
    @Operation(summary = "用户卸任岗位")
    public Result<Boolean> resignPosition(@RequestParam String userId, @RequestParam String positionId) {
        userPositionService.resignPosition(userId, positionId);
        return Result.success(true);
    }

}
