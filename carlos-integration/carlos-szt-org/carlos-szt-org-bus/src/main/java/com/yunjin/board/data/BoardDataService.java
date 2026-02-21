package com.yunjin.board.data;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.yunjin.board.data.handler.BoardDataHandler;
import com.yunjin.board.data.result.BoardDataResult;
import com.yunjin.core.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 看板数据查询 业务
 * </p>
 *
 * @author Carlos
 * @date 2025-5-13 11:07:57
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BoardDataService {


    public BoardDataResult queryBoardData(BoardDataQueryParam param) {
        String key = param.getKey();
        // 根据key获取Handler
        BoardDataHandler handler = getHandler(key);
        long start = System.currentTimeMillis();

        BoardDataResult result = null;
        try {
            result = handler.getData(param.getParam());
        } catch (Exception e) {
            log.error("获取指标数据失败:{}", key, e);
            result = new BoardDataResult();
            result.setErrMsg(e.getMessage());
        }
        result.setKey(key);
        result.setTake(DateUtil.formatBetween(DateUtil.spendMs(start)));
        return result;
    }

    private BoardDataHandler getHandler(String key) {
        if (StrUtil.isBlank(key)) {
            throw new ServiceException("数据key不能为空");
        }
        DataKeyEnum keyEnum = null;
        try {
            keyEnum = DataKeyEnum.valueOf(key);
        } catch (IllegalArgumentException e) {
            throw new ServiceException("数据key不合法！");
        }
        return keyEnum.getDataHandler();
    }
}
