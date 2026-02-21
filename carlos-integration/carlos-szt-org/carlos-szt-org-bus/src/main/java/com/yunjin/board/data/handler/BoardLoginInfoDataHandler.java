package com.yunjin.board.data.handler;


import com.yunjin.board.data.query.BoardLoginInfoDataQuery;
import com.yunjin.board.data.result.BoardDataResult;
import com.yunjin.board.data.result.BoardLoginInfoDataResult;
import com.yunjin.org.UserUtil;
import com.yunjin.org.pojo.ao.UserLoginAO;

import java.util.Map;

/**
 * <p>
 * 看板数据处理器
 * </p>
 *
 * @author Carlos
 * @date 2025-05-15 11:18
 */
public class BoardLoginInfoDataHandler extends AbstractBoardDataHandler<BoardLoginInfoDataQuery, BoardLoginInfoDataResult> {

    /**
     * 获取看板数据
     *
     * @param param 参数
     * @return {@link BoardDataResult}
     */
    @Override
    public BoardLoginInfoDataResult getData(Map<String, Object> param) {
        UserLoginAO user = UserUtil.getUser();
        return null;
    }


}


