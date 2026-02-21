package com.yunjin.board.data.handler;


import com.yunjin.board.data.query.BoardDataQuery;
import com.yunjin.board.data.result.BoardDataResult;

import java.util.Map;

/**
 * <p>
 * 看板数据处理器
 * </p>
 *
 * @author Carlos
 * @date 2025-05-15 11:18
 */
public interface BoardDataHandler<Q extends BoardDataQuery, R extends BoardDataResult> {

    /**
     * 获取看板数据
     *
     * @param param 参数
     * @return {@link BoardDataResult}
     */
    R getData(Map<String, Object> param);


    Q convertQueryParams(Map<String, Object> param);
}
