package com.yunjin.board.data.handler;


import cn.hutool.extra.spring.SpringUtil;
import com.google.common.collect.Lists;
import com.yunjin.board.data.query.ApplicationCaseDataQuery;
import com.yunjin.board.data.result.ApplicationCaseDataResult;
import com.yunjin.core.response.Result;
import com.yunjin.szt.grid.api.ApiGridInitInfo;
import com.yunjin.szt.grid.api.pojo.ao.GridScenarioInfoAO;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class ApplicationCaseDataHandler extends AbstractBoardDataHandler<ApplicationCaseDataQuery, ApplicationCaseDataResult> {
    public ApplicationCaseDataResult getData(Map<String, Object> param) {
        ApplicationCaseDataResult result = new ApplicationCaseDataResult();
        ApiGridInitInfo apiGridScenarioInfo = SpringUtil.getBean(ApiGridInitInfo.class);
        Result<List<GridScenarioInfoAO>> list = apiGridScenarioInfo.getList();
        ArrayList<ApplicationCaseDataResult.Item> objects = Lists.newArrayList();
        if(list.getData() != null) {
            List<GridScenarioInfoAO> data = list.getData();
            for(GridScenarioInfoAO vo : data) {
                objects.add(new ApplicationCaseDataResult.Item(vo.getId(), vo.getName(), vo.getSort(), vo.getLogo(), vo.getLogoId(), vo.getDescription(), vo.getVisible(), vo.getCreateBy(), vo.getCreateTime(), vo.getUpdateBy(), vo.getUpdateTime(), vo.getDeptIds(), vo.getRoleIds(), vo.getUserIds(), vo.getClassification(), vo.getReportNum(), vo.getFormNum(), vo.getBiNum()));
            }
        }
        return result.setItems(objects);
    }
}
