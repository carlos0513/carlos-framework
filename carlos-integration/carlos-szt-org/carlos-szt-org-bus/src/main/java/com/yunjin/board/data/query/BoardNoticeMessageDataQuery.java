package com.yunjin.board.data.query;

import com.yunjin.core.param.ParamPage;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BoardNoticeMessageDataQuery extends ParamPage implements BoardDataQuery {

    private String status;
}