package com.yunjin.org.enums;

/**
 * @Author GaoQiao
 * @Date 2023/11/2 11:19
 * 消息模板
 **/
public interface MessageTemplate {

    String COMPLETION_MESSAGE = "【%s】完成了任务！任务：【%s】";

    String COMPLETION_MESSAGE_REMARK = "【完成备注】%s";
    /**
     * 任务接收人名称+任务名称+提交完成
     */
    String SYS_COMPLETION_MESSAGE_TITLE = "%s%s提交完成";
    /**
     * 任务接收人名称+已提交完成了+任务名称，请尽快前往【我的任务】进行审核确认
     */
    String SYS_COMPLETION_MESSAGE_CONTENT = "%s已提交完成了%s，请尽快前往【我的任务】进行审核确认。";
    /**
     * 任务名称+任务完成审核+审核结果
     */
    String SYS_TASK_HANDLE_MESSAGE_TITLE = "%s任务完成审核%s";
    /**
     * 您的+任务名称+已被+审核人员+审核+审核结果，要求您于+要求整改完成时间+整改完成，请前往【我的任务】继续执行该任务
     */
    String SYS_TASK_HANDLE_REJECT_MESSAGE_CONTENT = "您的%s已被%s审核%s，要求您于%s整改完成，请前往【我的任务】继续执行该任务。";
    /**
     * 您的+任务名称+已被+审核人员+审核+审核结果
     */
    String SYS_TASK_HANDLE_PASS_MESSAGE_CONTENT = "您的%s已被%s审核%s。";

    /**
     * 任务到期提醒
     */
    String TASK_OVERDUE_MESSAGE = "尊敬的用户，任务【%s】已过期。";

    String COLLECTION_RUNNING_CONTENT_RECEIVER_ABNORMAL = "【%s】已驳回,要求您于【%s】内完成整改,审核意见:【%s】";

    // 采集任务相关模板
    String COLLECTION_NOT_STARTED_TITLE = "数据采集任务已驳回";
    String COLLECTION_NOT_STARTED_CONTENT = "【%s】已驳回,审核意见:【%s】,点击【%s】";
    // RUNNING
    String COLLECTION_RUNNING_TITLE_CREATOR = "数据采集任务通过审核";
    String COLLECTION_RUNNING_CONTENT_CREATOR = "【%s】已经通过审核,审核意见:【%s】";
    String COLLECTION_TITLE = "数据采集任务待您执行";
    String COLLECTION_CONTENT = "【%s】待您采集";
    String COLLECTION_STANDBY_TITLE = "数据采集任务待您确认";
    String COLLECTION_STANDBY_CONTENT = "【%s】待您确认";
    String COLLECTION_COMPLETED_TITLE = "数据采集任务已完成";
    String COLLECTION_COMPLETED_CONTENT = "【%s】已完成,审核意见:【%s】";
    String COLLECTION_PAUSE_TITLE = "数据采集任务已暂停";
    String COLLECTION_PAUSE_CONTENT = "【%s】已暂停";
    String COLLECTION_REJECT_TITLE = "数据采集任务待您确认";
    String COLLECTION_REJECT_CONTENT = "【%s】已驳回,要求您于【%s】内完成整改,审核意见:【%s】";
    String COLLECTION_CANCELED_TITLE = "数据采集任务已取消";
    String COLLECTION_CANCELED_CONTENT = "【%s】已取消";
    String COLLECTION_URGE_TITLE = "数据采集任务被催办";
    String COLLECTION_URGE_CONTENT = "【%s】被催办";
    String COLLECTION_AS_OF_DATE = "（截至日期为%s）";

    // 核查任务相关模板
    String CHECK_TITLE = "数据核查任务待您执行";
    String CHECK_CONTENT = "【%s】待您核查";
    String CHECK_STANDBY_TITLE = "数据核查任务待您确认";
    String CHECK_STANDBY_CONTENT = "【%s】待您确认";
    String CHECK_COMPLETED_TITLE = "数据核查任务已完成";
    String CHECK_COMPLETED_CONTENT = "【%s】已完成,审核意见:【%s】";
    String CHECK_REJECT_TITLE = "数据核查任务待您确认";
    String CHECK_REJECT_CONTENT = "【%s】已驳回,要求您于【%s】内完成整改,审核意见:【%s】";
    String CHECK_PAUSE_TITLE = "数据核查任务已暂停";
    String CHECK_PAUSE_CONTENT = "【%s】已暂停";
    String CHECK_CANCELED_TITLE = "数据核查任务已取消";
    String CHECK_CANCELED_CONTENT = "【%s】已取消";
    String CHECK_URGE_TITLE = "数据核查任务被催办";
    String CHECK_URGE_CONTENT = "【%s】被催办";

    // 业务类型 - 申请
    String APPLY_RUNNING_TITLE = "%s待您审核";
    String APPLY_RUNNING_CONTENT = "【%s】待您审核";
    String APPLY_PASS_TITLE = "%s申请已通过";
    String APPLY_PASS_CONTENT = "【%s】已通过";
    String APPLY_REJECT_TITLE = "%s申请已驳回";
    String APPLY_REJECT_CONTENT = "【%s】已驳回";

    // 投诉建议
    String COMPLAIN_SIGN_TITLE = "投诉举报已签收";
    String COMPLAIN_SIGN_CONTENT = "您的【%s】已被受理,有关部门会尽快核实反馈";
    String COMPLAIN_DEAL_TITLE = "投诉举报已处理";
    String COMPLAIN_DEAL_CONTENT = "您的【%s】已被处理,处理人反馈【%s】";
    String COMPLAIN_URGE_TITLE = "投诉举报催办";
    String COMPLAIN_URGE_CONTENT = "【%s】被催办，请您尽快进行办理。";
    String COMPLAIN_DENY_TITLE = "投诉举报被驳回";
    String COMPLAIN_DENY_CONTENT = "您的【%s】已被驳回,处理人反馈【%s】";

    // 数仓
    String WAREHOUSE_SHARED_RETURN_TITLE = "基础共用回流数据已入仓";
    String WAREHOUSE_SHARED_RETURN_CONTENT = "【回流数据{%s-%s}】已入仓";
    String WAREHOUSE_TASK_TITLE = "任务数据已入仓";
    String WAREHOUSE_TASK_CONTENT = "【任务数据{%s}】已入仓";
    String WAREHOUSE_RELEASE_WITHOUT_REVIEW_TITLE = "未审即发报表待确认";
    String WAREHOUSE_RELEASE_WITHOUT_REVIEW_CONTENT = "收到【未审即发报表{%s}】";


}
