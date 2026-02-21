package com.yunjin.org.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunjin.org.pojo.entity.OrgComplaintReport;
import com.yunjin.org.pojo.vo.OrgComplaintReportVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
* @description 针对表【org_complaint_report】的数据库操作Mapper
* @createDate 2024-03-21 15:38:19
* @Entity generator.domain.OrgComplaintReport
*/
@Mapper
public interface OrgComplaintReportMapper extends BaseMapper<OrgComplaintReport> {

    List<HashMap<String,String>> selectFormDept(@Param("ids") List<String> ids);

    List<OrgComplaintReportVO> selectComplaintInfo(@Param("ids") List<String> ids);

    String selectFormName(@Param("id") String id);

    String selectTaskName(@Param("id") String id);

    String selectUserName(@Param("id") String id);
}




