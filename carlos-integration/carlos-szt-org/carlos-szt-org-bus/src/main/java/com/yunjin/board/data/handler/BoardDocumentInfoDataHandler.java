package com.yunjin.board.data.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.yunjin.board.data.query.BoardDocumentInfoDataQuery;
import com.yunjin.board.data.result.BoardDocumentInfoDataResult;
import com.yunjin.core.response.Result;
import com.yunjin.org.pojo.dto.HelpFileDTO;
import com.yunjin.org.service.HelpFileService;
import com.yunjin.system.api.ApiFile;
import com.yunjin.system.pojo.ao.FileInfoAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Comparator;


/**
 * <p>
 * 文件资料 1、以卡片式列表展示通过后台上传的文件，该功能泸州已开发。 2、引导语：显示上传文件情况，可以了解系统操作手册、相关政策文件等。
 * </p>
 *
 * @author Carlos
 * @date 2025-05-16 13:42
 */
public class BoardDocumentInfoDataHandler extends AbstractBoardDataHandler<BoardDocumentInfoDataQuery, BoardDocumentInfoDataResult> {
    @Override
    public BoardDocumentInfoDataResult getData(Map<String, Object> param) {
        BoardDocumentInfoDataResult result = new BoardDocumentInfoDataResult();

        HelpFileService helpFileService = SpringUtil.getBean(HelpFileService.class);
        List<BoardDocumentInfoDataResult.Item> list = new ArrayList<>();

        ApiFile api = SpringUtil.getBean(ApiFile.class);

        List<HelpFileDTO> files = helpFileService.getAllHelpFile();
        if (CollUtil.isNotEmpty(files)) {
            for (HelpFileDTO file : files) {
                BoardDocumentInfoDataResult.Item item = new BoardDocumentInfoDataResult.Item();
                item.setId(file.getId());
                item.setTitle(file.getFileName());

                String fileSample = file.getFileSample();
                if (StrUtil.isNotBlank(fileSample)) {
                    List<FileInfoAO> its = JSONUtil.toList(fileSample, FileInfoAO.class);
                    if (CollUtil.isNotEmpty(its)) {
                        Result<FileInfoAO> file1 = api.getFile(its.get(0).getId());
                        Optional.ofNullable(file1.getData()).ifPresent(data -> {
                            item.setUrl(data.getUrl());
                            item.setFileName(data.getName());
                        });
                    }
                }
                item.setUpdateTime(file.getUpdateTime() != null ? file.getUpdateTime() : file.getCreateTime());
                list.add(item);
            }
        }

        // updateTime倒序
        list.sort(Comparator.comparing(BoardDocumentInfoDataResult.Item::getUpdateTime, Comparator.nullsLast(Comparator.reverseOrder())));

        result.setItems(list);
        return result;
    }
}
