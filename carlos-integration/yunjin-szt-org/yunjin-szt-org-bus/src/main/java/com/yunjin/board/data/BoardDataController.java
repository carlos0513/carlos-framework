package com.yunjin.board.data;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yunjin.board.data.result.BoardDataResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p>
 * 看板自定义配置 rest服务接口
 * </p>
 *
 * @author Carlos
 * @date 2025-5-13 11:07:57
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("org/board/data")
@Tag(name = "看板数据")
public class BoardDataController {


    private final BoardDataService dataService;


    @ApiOperationSupport(author = "Carlos")
    @PostMapping("query")
    @Operation(summary = "查询看板数据")
    public BoardDataResult add(@RequestBody @Validated BoardDataQueryParam param) {
        return dataService.queryBoardData(param);

    }

}
