package com.yunjin.board.data;

import com.yunjin.board.data.handler.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 字段配置项枚举
 * </p>
 *
 * @author yunjin
 * @date 2021/11/17 23:54
 */
@Getter
@AllArgsConstructor
public enum DataKeyEnum {

    /**
     * 开启
     */
    LoginInfo(1, "登陆信息栏", new BoardLoginInfoDataHandler()),
    CustomTask(2, "我收到的/我下派的/我审核的/我申请的", new BoardCustomTaskDataHandler()),
    UserOverview(4, "用户数据", new BoardUserOverviewDataHandler()),
    // 大邑定开用户数据
//    UserOverviewV1(5, "用户数据", new BoardUserOverviewDataHandler()),
    RtReportOverview(6, "报表", new BoardRtReportOverviewDataHandler()),
    // 大邑定开报表
//    RtReportOverviewV1(7, "报表准入县级", new BoardRtReportOverviewV1DataHandler()),
    DataMarket(8, "基础公用", new BoardDataMarketDataHandler()),
    CommonApplications(9, "常用应用", new BoardCommonApplicationsDataHandler()),
    TaskOverview(10, "任务", new BoardTaskOverviewDataHandler()),
    TaskRunningOverview(11, "任务运行情况", new BoardTaskRunningOverviewDataHandler()),
    // 大邑定开任务运行情况
    TaskRunningOverviewV1(12, "任务下发运行情况", new BoardTaskRunningOverviewV2DataHandler()),
    MyData(13, "我的数据", new BoardMyWarehouseDataHandler()),
    RefluxData(14, "回流数据/回流数据排行", new BoardRefluxDataDataHandler()),
    WarehouseOverview(15, "数仓", new BoardWarehouseOverviewDataHandler()),
    // 大邑定开数仓
//    WarehouseOverviewV1(16, "数仓", new BoardWarehouseOverviewV1DataHandler()),
    NoticeMessage(17, "通知公告", new BoardNoticeMessageDataHandler()),
    DocumentInfo(18, "文件资料", new BoardDocumentInfoDataHandler()),
    FieldMarket(19, "字段超市", new BoardFieldMarketDataHandler()),
    ReportsNumber(20, "县（市、区）报表数", new BoardReportNumberDataHandler()),
    //大邑定开应用案例
//    ApplicationCase(21, "应用案列", new ApplicationCaseDataHandler()),

    ;

    private final Integer code;

    private final String desc;

    private final BoardDataHandler dataHandler;


}
