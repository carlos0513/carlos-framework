package com.yunjin.org.manager.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yunjin.core.pagination.Paging;
import com.yunjin.datasource.base.BaseServiceImpl;
import com.yunjin.datasource.pagination.MybatisPage;
import com.yunjin.datasource.pagination.PageInfo;
import com.yunjin.org.convert.HelpFileConvert;
import com.yunjin.org.manager.HelpFileManager;
import com.yunjin.org.mapper.HelpFileMapper;
import com.yunjin.org.pojo.dto.HelpFileDTO;
import com.yunjin.org.pojo.entity.HelpFile;
import com.yunjin.org.pojo.param.HelpFilePageParam;
import com.yunjin.org.pojo.vo.HelpFileVO;
import com.yunjin.system.pojo.ao.FileInfoAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Component
public class HelpFileManagerImpl  extends BaseServiceImpl<HelpFileMapper, HelpFile> implements HelpFileManager {

    @Override
    public Boolean isExist(String fileName) {
        LambdaQueryWrapper<HelpFile> wrapper = queryWrapper();
        wrapper.eq(HelpFile::getFileName, fileName);
        return getBaseMapper().exists(wrapper);
    }

    @Override
    public Boolean add(HelpFileDTO dto) {
        HelpFile helpFile = HelpFileConvert.INSTANCE.toDO(dto);
        boolean success = save(helpFile);
        if (!success) {
            log.warn("Insert 'BbtLabelType' data fail, entity:{}", helpFile);
            return false;
        }
        dto.setId(helpFile.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'BbtLabelType' data: id:{}", helpFile.getId());
        }
        return true;
    }

    @Override
    public HelpFileDTO getDtoById(String id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        HelpFile entity = getBaseMapper().selectById(id);
        return HelpFileConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<HelpFileVO> getPage(HelpFilePageParam param) {
        LambdaQueryWrapper<HelpFile> wrapper = queryWrapper();
        wrapper.select(
                        HelpFile::getId,
                        HelpFile::getFileName,
                        HelpFile::getFileType,
                        HelpFile::getSort,
                        HelpFile::getCreateBy,
                        HelpFile::getCreateTime,
                        HelpFile::getUpdateBy,
                        HelpFile::getUpdateTime,
                        HelpFile::getFileSample,
                        HelpFile::getFileContent
                )
                .like(StrUtil.isNotBlank(param.getFileName()), HelpFile::getFileName, param.getFileName().trim())
                .orderByDesc(HelpFile::getSort);

        PageInfo<HelpFile> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, items -> {
            List<HelpFileVO> vos = new ArrayList<>();
            for (HelpFile item : items) {
                HelpFileVO vo = HelpFileConvert.INSTANCE.toVO(item);
                String fileSample = item.getFileSample();
                if (StrUtil.isNotBlank(fileSample)) {
                    List<FileInfoAO> its = JSONUtil.toList(fileSample, FileInfoAO.class);
                    if (CollUtil.isNotEmpty(its)) {
                        Optional.ofNullable(its.get(0)).ifPresent(data -> {
                            vo.setFileName(data.getName());
                        });
                    }
                }
                vos.add(vo);
            }

            return vos;
        });
    }

    @Override
    public void deleteHelpFile(Set<String> ids) {
        for (String id : ids) {
//            HelpFileDTO dtoById = helpFileManager.getDtoById(id);
            Boolean delete = this.delete(id);
        }
    }

    @Override
    public Boolean delete(String id) {
        if (id == null) {
            log.warn("id can't be null");
            return false;
        }
        boolean success = removeById(id);
        if (!success) {
            log.warn("Remove 'BbtLabelType' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'BbtLabelType' data by id:{}", id);
        }
        return true;
    }

    @Override
    public Boolean modify(HelpFileDTO dto) {
        HelpFile entity = HelpFileConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'Label' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'Label' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public List<HelpFileDTO> listAll() {
        List<HelpFile> list = list();
        return HelpFileConvert.INSTANCE.toDTO(list);
    }
}