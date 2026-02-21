package com.yunjin.board.data.query;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BoardDataMarketDataQuery implements BoardDataQuery {

    /**
     * 排序方式
     */
    private SortType sortType;


    public enum SortType {

        data_count, view_count

    }
}
