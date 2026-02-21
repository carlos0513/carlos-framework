package com.yunjin.org.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yunjin.core.pagination.Paging;
import com.yunjin.org.pojo.dto.OrgComplaintReportDTO;
import com.yunjin.org.pojo.entity.OrgComplaintReport;
import com.yunjin.org.pojo.param.OrgComplaintReportPageParam;
import com.yunjin.org.pojo.vo.OrgComplaintReportDetailVO;
import com.yunjin.org.pojo.vo.OrgComplaintReportVO;

import javax.servlet.http.HttpServletResponse;

/**
* @description 针对表【org_complaint_report】的数据库操作Service
* @createDate 2024-03-21 15:38:19
*/
public interface OrgComplaintReportService extends IService<OrgComplaintReport> {

    //新增
    void addComplaintReport(OrgComplaintReportDTO addInfo);

    //查询
    Paging<OrgComplaintReportVO> queryComplaintReport(OrgComplaintReportPageParam param);

    //签收
    void signFor(String id);

    //处理
    void deal(OrgComplaintReport dealInfo);

    void export(HttpServletResponse response, OrgComplaintReportPageParam param);

    void urge(String id, String content);

    void deny(String id, String content);

    OrgComplaintReportDetailVO queryComplaintDetail(String id);

    void upgrade(String id);


    void withdraw(String id);

    void whiteListAddComplaintReport(OrgComplaintReportDTO dto);
}
