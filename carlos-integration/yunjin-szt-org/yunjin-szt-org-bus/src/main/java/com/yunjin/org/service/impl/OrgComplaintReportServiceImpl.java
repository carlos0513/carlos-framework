package com.yunjin.org.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunjin.citytask.pojo.ao.TaskCityComplaintAO;
import com.yunjin.core.exception.ServiceException;
import com.yunjin.core.pagination.Paging;
import com.yunjin.core.response.Result;
import com.yunjin.excel.easyexcel.ExcelUtil;
import com.yunjin.form.api.ApiForm;
import com.yunjin.form.pojo.ao.FormTableBaseInfoAO;
import com.yunjin.org.UserUtil;
import com.yunjin.org.convert.OrgComplaintLogConvert;
import com.yunjin.org.convert.OrgComplaintReportConvert;
import com.yunjin.org.enums.MessageTemplate;
import com.yunjin.org.enums.SmsMessageEnum;
import com.yunjin.org.manager.DepartmentManager;
import com.yunjin.org.manager.OrgComplaintLogManager;
import com.yunjin.org.manager.OrgComplaintReportManager;
import com.yunjin.org.mapper.OrgComplaintReportMapper;
import com.yunjin.org.pojo.ao.UserLoginAO;
import com.yunjin.org.pojo.dto.DepartmentDTO;
import com.yunjin.org.pojo.dto.OrgComplaintLogDTO;
import com.yunjin.org.pojo.dto.OrgComplaintReportDTO;
import com.yunjin.org.pojo.dto.OrgUserMessageDTO;
import com.yunjin.org.pojo.emuns.*;
import com.yunjin.org.pojo.entity.OrgComplaintReport;
import com.yunjin.org.pojo.enums.ComplainSourceEnum;
import com.yunjin.org.pojo.enums.ComplainTypeEnum;
import com.yunjin.org.pojo.enums.UserMessageStatus;
import com.yunjin.org.pojo.excel.ComplaintReportExcel;
import com.yunjin.org.pojo.param.OrgComplaintReportPageParam;
import com.yunjin.org.pojo.vo.OrgComplaintReportDetailVO;
import com.yunjin.org.pojo.vo.OrgComplaintReportVO;
import com.yunjin.org.service.OrgComplaintReportService;
import com.yunjin.org.service.OrgUserMessageService;
import com.yunjin.system.api.ApiFile;
import com.yunjin.system.pojo.ao.FileInfoAO;
import com.yunjin.system.pojo.ao.UploadResultAO;
import com.yunjin.system.pojo.param.ApiFileUploadParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description 针对表【org_complaint_report】的数据库操作Service实现
 * @createDate 2024-03-21 15:38:19
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrgComplaintReportServiceImpl extends ServiceImpl<OrgComplaintReportMapper, OrgComplaintReport>
        implements OrgComplaintReportService {


    private final OrgComplaintReportMapper orgComplaintReportMapper;

    private final OrgUserMessageService orgUserMessageService;

    private final OrgComplaintReportManager complaintReportManager;

    private final DepartmentManager departmentManager;

    private final OrgComplaintLogManager orgComplaintLogManager;

    private final ApiFile apiFile;


    @Override
    public void addComplaintReport(OrgComplaintReportDTO addInfo) {

        addInfo.setStatus(ComplainStatusEnum.UN_ACCEPTED.getCode());
        addInfo.setHandleLevel(ComplainHandleLevelEnum.REGION.getCode());
        if (StringUtils.isNotBlank(addInfo.getComplaintForm())) {
            String[] tableIds = addInfo.getComplaintForm().split(",");
            List<HashMap<String, String>> deptInfos = orgComplaintReportMapper.selectFormDept(Arrays.asList(tableIds));
            //list转map
            deptInfos.forEach(e -> {
                OrgComplaintReport orgComplaintReport = new OrgComplaintReport();
                BeanUtil.copyProperties(addInfo, orgComplaintReport);
                orgComplaintReport.setComplaintForm(e.get("id"));
                orgComplaintReport.setFormDept(e.get("departmentId"));
                OrgComplaintReportDTO dto = OrgComplaintReportConvert.INSTANCE.toDTO(orgComplaintReport);
                complaintReportManager.add(dto);
                //增加创建记录
                orgComplaintLogManager.add(new OrgComplaintLogDTO().setComplaintId(dto.getId()).setHandleType(ComplainActionEnum.CREATE.getCode()).setDeptCode(UserUtil.getDepartment().getDeptCode()).setRemark(addInfo.getReason()));
            });
        } else {
            //报表通外的任务,没有表单id和部门id
            OrgComplaintReport orgComplaintReport = new OrgComplaintReport();
            BeanUtil.copyProperties(addInfo, orgComplaintReport);
            OrgComplaintReportDTO dto = OrgComplaintReportConvert.INSTANCE.toDTO(orgComplaintReport);
            complaintReportManager.add(dto);
            //增加创建记录
            orgComplaintLogManager.add(new OrgComplaintLogDTO().setComplaintId(dto.getId()).setHandleType(ComplainActionEnum.CREATE.getCode()).setDeptCode(UserUtil.getDepartment().getDeptCode()).setRemark(addInfo.getReason()));
        }
    }

    @Override
    public Paging<OrgComplaintReportVO> queryComplaintReport(OrgComplaintReportPageParam param) {
        Set<String> deptCodes = null;
        if (StrUtil.isBlank(param.getUserId())) {
            //如果不传userid,则按照部门查
            UserLoginAO.Department department = UserUtil.getDepartment();
            deptCodes = UserUtil.allSubDepartmentCodeByParentId(department.getId(), true);
        }
        //传userId,按照自己投诉的查
        Paging<OrgComplaintReportVO> page = complaintReportManager.getPage(param, deptCodes);
        Set<String> tableIsd = new HashSet<>();
        if (!page.getRecords().isEmpty()) {
            page.getRecords().forEach(e -> {
                if (StrUtil.isNotBlank(e.getFormDept())) {
                    DepartmentDTO departmentByCode = departmentManager.getDepartmentByCode(e.getFormDept());
                    if (ObjectUtils.isEmpty(departmentByCode)){
                        e.setFormDept("");
                    }
                    String parentName = "";
                    if (StrUtil.isNotBlank(departmentByCode.getParentId()) && !"0".equals(departmentByCode.getParentId())) {
                        parentName = departmentManager.getDepartmentName(departmentByCode.getParentId());
                        e.setFormDept(parentName + "-" + departmentByCode.getDeptName());
                    } else {
                        e.setFormDept(departmentByCode.getDeptName());
                    }
                }
                e.setComplaintSourceName(ComplainSourceEnum.getDescByCode(e.getComplaintSource()));
                if (StrUtil.isNotBlank(e.getComplaintTask())) {
                    e.setTaskSourceName(orgComplaintReportMapper.selectTaskName(e.getComplaintTask()));
                }
                if (11 == e.getComplaintSource()){
                    if (22 == e.getTaskSource()){
                        e.setTaskSourceName("线下任务");
                    }
                    if (21 == e.getTaskSource()){
                        e.setTaskSourceName("其他系统任务");
                    }

                }
                e.setComplaintTypeName(ComplainTypeEnum.getDescByCode(e.getComplaintType()));
                e.setStatusName(ComplainStatusEnum.getDescByCode(e.getStatus()));
                tableIsd.add(e.getComplaintForm());
            });

            ApiForm apiForm = SpringUtil.getBean(ApiForm.class);
            Result<List<FormTableBaseInfoAO>> result = apiForm.listTableInfo(tableIsd, true);
            if (ObjectUtils.isNotEmpty(result.getData())) {
                Map<String, String> idToTableLabelMap = result.getData().stream()
                        .collect(Collectors.toMap(
                                FormTableBaseInfoAO::getId,
                                FormTableBaseInfoAO::getTableLabel,
                                (existing, replacement) -> existing
                        ));
                page.getRecords().forEach(e -> {
                    String complaintFormId = e.getComplaintForm();
                    String tableLabel = idToTableLabelMap.get(complaintFormId);
                    e.setComplaintFormName(tableLabel != null ? tableLabel : "");
                });
            }
        }
        return page;
    }

    @Override
    public void signFor(String id) {

        OrgComplaintReport orgComplaintReport = orgComplaintReportMapper.selectById(id);
        orgComplaintReport.setStatus(ComplainStatusEnum.ACCEPTED.getCode());
        orgComplaintReportMapper.updateById(orgComplaintReport);
        //增加签收记录
        orgComplaintLogManager.add(new OrgComplaintLogDTO().setComplaintId(id).setHandleType(ComplainActionEnum.ACCEPT.getCode()).setDeptCode(UserUtil.getDepartment().getDeptCode()));

        // 添加投诉 已受理消息
        OrgUserMessageDTO message = new OrgUserMessageDTO()
                .setTitle(MessageTemplate.COMPLAIN_SIGN_TITLE)
                .setContent(String.format(MessageTemplate.COMPLAIN_SIGN_CONTENT, removeBrackets(orgComplaintReport.getReason())))
                .setUserId(orgComplaintReport.getCreateBy())//接收人
                .setMessageId(orgComplaintReport.getId()) // 投诉ID
                .setCreator(UserUtil.getId())
                .setType(com.yunjin.org.pojo.enums.UserMessageType.COMPLAINTS)
                .setDeptCode(null)
                .setSendDate(LocalDateTime.now())
                .setStatus(UserMessageStatus.UNREAD)
                .setSmsMessageEnum(SmsMessageEnum.FEEDBACK_COMMIT);
        orgUserMessageService.addOrgUserMessage(message);
    }

    // 因为消息短信的原因,所以需要将content去除【】
    public String removeBrackets(String str) {
        if (str.contains("【") && str.contains("】")) {
            return str.replaceAll("【", "").replaceAll("】", "");
        } else {
            return str;
        }
    }

    @Override
    public void deal(OrgComplaintReport dealInfo) {
        OrgComplaintReport orgComplaintReport = orgComplaintReportMapper.selectById(dealInfo.getId());
        orgComplaintReport.setReply(dealInfo.getReply());
        orgComplaintReport.setStatus(ComplainStatusEnum.HANDLED.getCode());
        orgComplaintReportMapper.updateById(orgComplaintReport);
        //增加处理记录
        orgComplaintLogManager.add(new OrgComplaintLogDTO().setComplaintId(dealInfo.getId()).setHandleType(ComplainActionEnum.HANDLED.getCode()).setDeptCode(UserUtil.getDepartment().getDeptCode()).setRemark(dealInfo.getReply()));
        // 添加投诉 已处理消息
        OrgUserMessageDTO message = new OrgUserMessageDTO()
                .setTitle(MessageTemplate.COMPLAIN_DEAL_TITLE)
                .setContent(String.format(MessageTemplate.COMPLAIN_DEAL_CONTENT, removeBrackets(orgComplaintReport.getReason()), removeBrackets(orgComplaintReport.getReply())))
                .setUserId(orgComplaintReport.getCreateBy()) //接收人
                .setMessageId(orgComplaintReport.getId()) // 投诉ID
                .setType(com.yunjin.org.pojo.enums.UserMessageType.COMPLAINTS)
                .setDeptCode(null)
                .setCreator(UserUtil.getId())
                .setSendDate(LocalDateTime.now())
                .setStatus(UserMessageStatus.UNREAD)
                .setSmsMessageEnum(SmsMessageEnum.FEEDBACK_RESULT);
        orgUserMessageService.addOrgUserMessage(message);
    }

    public void export(HttpServletResponse response, OrgComplaintReportPageParam param) {

        //表格标题，就是模型的属性名
        String name = "投诉反馈--" + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN);
        List<ComplaintReportExcel> data = new ArrayList<>();
        // 获取数据
        List<OrgComplaintReportVO> list = this.queryComplaintReport(param).getRecords();
        list.forEach(e -> {
            ComplaintReportExcel excelData = new ComplaintReportExcel();
            BeanUtil.copyProperties(e, excelData);
            excelData.setComplaintTime(DateUtil.format(e.getCreateTime(), DatePattern.NORM_DATETIME_PATTERN));
            data.add(excelData);
        });

        try {
            ExcelUtil.download(response, name, ComplaintReportExcel.class, data);
        } catch (Exception e) {
            throw new ServiceException("投诉反馈导出失败");
        }
    }

    @Override
    public void urge(String id, String content) {
        //在log中找到最后一条受理的记录，找出受理人，发消息
        OrgComplaintLogDTO lastAcceptedLog = orgComplaintLogManager.getLastAcceptedLog(id);
        if (Objects.isNull(lastAcceptedLog)) {
            throw new ServiceException("投诉未受理，无法催办");
        }
        //增加处理记录
        orgComplaintLogManager.add(new OrgComplaintLogDTO().setComplaintId(id).setHandleType(ComplainActionEnum.URGE_REPORT.getCode()).setDeptCode(UserUtil.getDepartment().getDeptCode()).setRemark(content));
        // 添加投诉 已处理消息
        OrgUserMessageDTO message = new OrgUserMessageDTO()
                .setTitle(MessageTemplate.COMPLAIN_URGE_TITLE)
                .setContent(String.format(MessageTemplate.COMPLAIN_URGE_CONTENT, removeBrackets(content)))
                .setUserId(lastAcceptedLog.getCreateBy()) //接收人
                .setMessageId(id) // 投诉ID
                .setType(com.yunjin.org.pojo.enums.UserMessageType.COMPLAINTS)
                .setDeptCode(null)
                .setCreator(UserUtil.getId())
                .setSendDate(LocalDateTime.now())
                .setStatus(UserMessageStatus.UNREAD)
                .setSmsMessageEnum(SmsMessageEnum.FEEDBACK_RESULT);
        orgUserMessageService.addOrgUserMessage(message);
    }

    @Override
    public void deny(String id, String content) {
        OrgComplaintReportDTO dtoById = complaintReportManager.getDtoById(id);
        dtoById.setStatus(ComplainStatusEnum.DENY_ACCEPTED.getCode());
        complaintReportManager.modify(dtoById);
        //增加不处理记录
        orgComplaintLogManager.add(new OrgComplaintLogDTO().setComplaintId(id).setHandleType(ComplainActionEnum.DENY_ACCEPTED.getCode()).setDeptCode(UserUtil.getDepartment().getDeptCode()).setRemark(content));
        // 添加投诉 已处理消息
        OrgUserMessageDTO message = new OrgUserMessageDTO()
                .setTitle(MessageTemplate.COMPLAIN_DENY_TITLE)
                .setContent(String.format(MessageTemplate.COMPLAIN_DENY_CONTENT, removeBrackets(dtoById.getReason()), removeBrackets(content)))
                .setUserId(dtoById.getCreateBy()) //给创建人发信息
                .setMessageId(id) // 投诉ID
                .setType(com.yunjin.org.pojo.enums.UserMessageType.COMPLAINTS)
                .setDeptCode(null)
                .setCreator(UserUtil.getId())
                .setSendDate(LocalDateTime.now())
                .setStatus(UserMessageStatus.UNREAD)
                .setSmsMessageEnum(SmsMessageEnum.FEEDBACK_RESULT);
        orgUserMessageService.addOrgUserMessage(message);
    }

    @Override
    public OrgComplaintReportDetailVO queryComplaintDetail(String id) {
        OrgComplaintReportDTO e = complaintReportManager.getDtoById(id);
        List<OrgComplaintLogDTO> handleHistory = orgComplaintLogManager.getDtoByComplaintId(id);
        OrgComplaintReportDetailVO detailVO = new OrgComplaintReportDetailVO();
        BeanUtil.copyProperties(e, detailVO);

        // 获取投诉来源，判断是否为报表通外任务
        ComplainSourceEnum complaintSource = ComplainSourceEnum.codeOf(e.getComplaintSource());

        // 处理部门信息
        if (StrUtil.isNotBlank(e.getFormDept())) {
            // 有部门编码的情况就是内部系统任务
            DepartmentDTO departmentByCode = departmentManager.getDepartmentByCode(e.getFormDept());
            String parentName = "";
            if (StrUtil.isNotBlank(departmentByCode.getParentId()) && !"0".equals(departmentByCode.getParentId())) {
                parentName = departmentManager.getDepartmentName(departmentByCode.getParentId());
                detailVO.setFormDept(parentName + "-" + departmentByCode.getDeptName());
            } else {
                detailVO.setFormDept(departmentByCode.getDeptName());
            }
        } else {
            // 没有部门编码的情况
            if (complaintSource == ComplainSourceEnum.OUT_TASK) {
                // 报表通外任务，设置默认描述
                detailVO.setFormDept("外部任务");
            } else if (complaintSource == ComplainSourceEnum.BBT_TASK) {
                // 报表通任务但没有部门编码，可以设置为空或默认值
                detailVO.setFormDept("未指定部门");
            } else {
                // 其他来源，默认处理
                detailVO.setFormDept("未指定部门");
            }
        }

        detailVO.setComplaintSourceName(ComplainSourceEnum.getDescByCode(e.getComplaintSource()));
        detailVO.setTaskSourceName(orgComplaintReportMapper.selectTaskName(e.getComplaintTask()));
        detailVO.setComplaintTypeName(ComplainTypeEnum.getDescByCode(e.getComplaintType()));
        detailVO.setStatusName(ComplainStatusEnum.getDescByCode(e.getStatus()));
        detailVO.setHandleList(OrgComplaintLogConvert.INSTANCE.toVO(handleHistory));

        if (StrUtil.isNotBlank(e.getComplaintTask())) {
            detailVO.setComplaintTaskName(orgComplaintReportMapper.selectTaskName(e.getComplaintTask()));
        }
        if (StrUtil.isNotBlank(e.getComplaintForm())) {
            detailVO.setComplaintFormName(orgComplaintReportMapper.selectFormName(e.getComplaintForm()));
        }

        return detailVO;
    }

    /**
     * 上报
     *
     * @param id
     */
    @Override
    public void upgrade(String id) {
        OrgComplaintReportDTO dtoById = complaintReportManager.getDtoById(id);
        //上报市级
        TaskCityComplaintAO taskCityComplaint = new TaskCityComplaintAO();
        taskCityComplaint.setId(id);
        taskCityComplaint.setComplaintUnit(departmentManager.getDepartmentByCode(dtoById.getFormDept()).getDeptName());
        if (StrUtil.isNotBlank(dtoById.getPictures())) {
            List<String> fileIds = JSONUtil.toList(dtoById.getPictures(), FileInfoAO.class).stream().map(FileInfoAO::getId).collect(Collectors.toList());
            taskCityComplaint.setFileIds(StrUtil.join("、", fileIds));
        }
        taskCityComplaint.setReportName(orgComplaintReportMapper.selectFormName(dtoById.getComplaintForm()));
        taskCityComplaint.setSourcePersonText(orgComplaintReportMapper.selectUserName(dtoById.getCreateBy()));
        taskCityComplaint.setSourcePersonValue(dtoById.getCreateBy());
        taskCityComplaint.setContent(dtoById.getReason());
        taskCityComplaint.setDistrictDataId(id);
        taskCityComplaint.setComplaintTime(dtoById.getCreateTime());
        taskCityComplaint.setSubmitTime(LocalDateTime.now());
        log.info("投诉上报报文：{}", JSONUtil.toJsonStr(taskCityComplaint));
        //todo 需要现场同学补全缺失字段  现场测试
//        apiTaskCityComplaint.submitTaskCityComplaint(taskCityComplaint);
        // 修改投诉状态
        dtoById.setHandleLevel(ComplainHandleLevelEnum.CITY.getCode());
        dtoById.setStatus(ComplainStatusEnum.UP_REPORT.getCode());
        complaintReportManager.modify(dtoById);
        //增加上报记录
        orgComplaintLogManager.add(new OrgComplaintLogDTO().setComplaintId(id).setHandleType(ComplainActionEnum.UP_REPORT.getCode()).setDeptCode(UserUtil.getDepartment().getDeptCode()));
    }

    /**
     * 撤回
     *
     * @param id
     */
    @Override
    public void withdraw(String id) {
        //todo 从市级撤回  现场测试
//        apiTaskCityComplaint.withdrawTaskCityComplaintById(id);
        // 修改投诉状态
        OrgComplaintReportDTO dtoById = complaintReportManager.getDtoById(id);
        dtoById.setHandleLevel(ComplainHandleLevelEnum.REGION.getCode());
        dtoById.setStatus(ComplainStatusEnum.ACCEPTED.getCode());
        complaintReportManager.modify(dtoById);
        //增加上报记录
        orgComplaintLogManager.add(new OrgComplaintLogDTO().setComplaintId(id).setHandleType(ComplainActionEnum.WITHDRAW.getCode()).setDeptCode(UserUtil.getDepartment().getDeptCode()));
    }

    /**
     * 白名单新增
     * @param dto
     */
    @Override
    public void whiteListAddComplaintReport(OrgComplaintReportDTO dto) {
        //签名校验
        if (!DigestUtil.md5Hex("TASK_CODE").equals(dto.getSgin())) {
            throw new ServiceException("新增失败");
        }
        if (StrUtil.isNotBlank(dto.getPictures())){
            //处理图片
            processThePicture(dto);
        }
        this.addComplaintReport(dto);
    }

    /**
     * 处理图片
     * @param dto
     */
    private void processThePicture(OrgComplaintReportDTO dto) {
        //前端传base64图片编码
        String pictures = dto.getPictures();
        if (pictures.startsWith("data:")) {
            pictures = pictures.split(",")[1];
        }
        // 解码 Base64 字符串
        byte[] bytes = Base64.getDecoder().decode(pictures);

        //fileApi调用，上传图片，返回图片信息
        List<ApiFileUploadParam.UploadFile> list = new ArrayList<>();
        list.add(new ApiFileUploadParam.UploadFile().setName(RandomUtil.randomString(5)).setBytes(bytes));
        ApiFileUploadParam apiFileUploadParam = new ApiFileUploadParam().setFiles(list).setNamespace("yunjin-system");
        Result<UploadResultAO> fileResult = apiFile.upload(apiFileUploadParam);
        UploadResultAO uploadResultAO;
        if (fileResult.data().isPresent()) {
            uploadResultAO = fileResult.data().get();
        } else {
            throw new ServiceException("文件上传失败，上传结果为空");
        }
        dto.setPictures(JSONUtil.toJsonStr(uploadResultAO.getFiles().get(0)));
    }
}




