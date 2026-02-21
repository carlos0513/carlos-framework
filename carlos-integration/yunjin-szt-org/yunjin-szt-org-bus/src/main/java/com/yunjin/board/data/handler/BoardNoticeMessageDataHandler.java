package com.yunjin.board.data.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.yunjin.board.data.query.BoardNoticeMessageDataQuery;
import com.yunjin.board.data.result.BoardNoticeMessageDataResult;
import com.yunjin.core.pagination.Paging;
import com.yunjin.org.UserUtil;
import com.yunjin.org.manager.OrgUserMessageManager;
import com.yunjin.org.pojo.dto.UserDTO;
import com.yunjin.org.pojo.enums.UserMessageStatus;
import com.yunjin.org.pojo.param.OrgUserMessagePageParam;
import com.yunjin.org.pojo.vo.OrgUserMessageVO;
import com.yunjin.org.service.UserService;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;


/**
 * <p>
 * 通知公告 1、展示展示当前登录用户消息提示（包括系统公告、投诉建议、采集任务、核查任务需求申请） 2、展示指标：类型、标题、时间 3、点击“更多”按钮，页面跳转消息中心页面
 * </p>
 *
 * @author Carlos
 * @date 2025-05-16 13:35
 */
public class BoardNoticeMessageDataHandler extends AbstractBoardDataHandler<BoardNoticeMessageDataQuery, BoardNoticeMessageDataResult> {

    @Override
    public BoardNoticeMessageDataResult getData(Map<String, Object> param) {
        BoardNoticeMessageDataQuery query = convertQueryParams(param);
        BoardNoticeMessageDataResult result = new BoardNoticeMessageDataResult();

        OrgUserMessagePageParam p = new OrgUserMessagePageParam();
        p.setUserId(UserUtil.getId());
        // p.setType(UserMessageType.MESSAGE);
        p.setStatus(UserMessageStatus.UNREAD);
        p.setDeptCode(UserUtil.getDepartment().getDeptCode());
        if (query != null) {
            p.setCurrent(query.getCurrent());
            p.setSize(query.getSize());
        }
        OrgUserMessageManager userMessageManager = SpringUtil.getBean(OrgUserMessageManager.class);
        Paging<OrgUserMessageVO> page = userMessageManager.getPage(p);

        result.setTotal(page.getTotal());
        result.setSize(p.getSize());
        result.setPages(page.getPages());
        result.setCurrent(p.getCurrent());
        List<OrgUserMessageVO> records = page.getRecords();

        Map<String, String> map = new HashMap<>(4);
        if (CollUtil.isNotEmpty(records)) {
            Set<String> userIds = records.stream().map(OrgUserMessageVO::getCreator).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
            UserService userService = SpringUtil.getBean(UserService.class);
            List<UserDTO> users = userService.listByIds(userIds);
            map = users.stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getRealname));
        }

        List<BoardNoticeMessageDataResult.Item> list = new ArrayList<>();
        for (OrgUserMessageVO record : records) {
            BoardNoticeMessageDataResult.Item item = new BoardNoticeMessageDataResult.Item();
            item.setId(record.getId());
            item.setUserId(record.getUserId());
            item.setMessageId(record.getMessageId());
            item.setType(record.getType());
            item.setTitle(record.getTitle());
            item.setStatus(record.getStatus());
            item.setCreator(record.getCreator());
            item.setSendDate(record.getSendDate());
            item.setContent(record.getContent());
            item.setDeptCode(record.getDeptCode());
            item.setCreatorName(map.get(record.getCreator()));
            list.add(item);
        }
        result.setRecords(list);
        return result;
    }
}
