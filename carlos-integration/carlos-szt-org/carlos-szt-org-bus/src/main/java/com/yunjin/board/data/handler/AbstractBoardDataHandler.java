package com.yunjin.board.data.handler;


import cn.hutool.core.bean.BeanUtil;
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
public class AbstractBoardDataHandler<Q extends BoardDataQuery, R extends BoardDataResult> implements BoardDataHandler<Q, R> {


    @Override
    public R getData(Map<String, Object> param) {
        BoardDataQuery query = convertQueryParams(param);
        return null;
    }


    @Override
    public Q convertQueryParams(Map<String, Object> param) {
        if (param != null) {
            return BeanUtil.toBean(param, getQueryClass());
        }

        return null;
    }

    private Class<Q> getQueryClass() {
        return (Class<Q>) ((Class<?>) ((java.lang.reflect.ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    }


}
