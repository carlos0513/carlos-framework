package com.carlos.system.dict.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Sets;
import com.carlos.core.exception.ServiceException;
import com.carlos.system.dict.convert.SysDictConvert;
import com.carlos.system.dict.convert.SysDictItemConvert;
import com.carlos.system.dict.manager.SysDictItemManager;
import com.carlos.system.dict.manager.SysDictManager;
import com.carlos.system.dict.pojo.dto.SysDictDTO;
import com.carlos.system.dict.pojo.dto.SysDictItemDTO;
import com.carlos.system.dict.pojo.param.SysDictCreateParam;
import com.carlos.system.dict.pojo.param.SysDictCreateParam.Item;
import com.carlos.system.dict.pojo.param.SysDictUpdateParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;


/**
 * <p>
 * 系统字典 业务接口实现类
 * </p>
 *
 * @author yunjin
 * @date 2021-11-22 14:49:00
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysDictService {

    private final SysDictManager dictManager;

    private final SysDictItemManager dictItemManager;

    private final SysDictItemService itemService;

    /**
     * 新增字典
     *
     * @param dto 字典数据
     * @return boolean
     * @author yunjin
     * @date 2021/11/30 10:28
     */

    @Transactional(rollbackFor = Exception.class)
    public boolean addDict(SysDictDTO dto, List<SysDictCreateParam.Item> items) {
        if (dictManager.count(dto.getDictCode(), null, null) > 0) {
            throw new ServiceException("字典编码不可重复!");
        }
        if (dictManager.count(null, dto.getDictName(), null) > 0) {
            throw new ServiceException("字典名称不可重复!");
        }
        boolean save = dictManager.add(dto);
        if (save) {
            // 保存字典选项
            if (CollectionUtil.isNotEmpty(items)) {
                itemService.addDictItem(dto.getId(), SysDictItemConvert.INSTANCE.create2dto(items));
            }
        }
        return true;
    }

    /**
     * 修改字典信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author yunjin
     * @date 2021/11/30 16:59
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean update(SysDictDTO dto, List<SysDictUpdateParam.Item> items) {
        String dictId = dto.getId();
        SysDictDTO dict = dictManager.getDictById(dictId);
        if (dict == null) {
            throw new ServiceException("字典不存在!");
        }

        if (dictManager.count(null, dto.getDictName(), dto.getId()) > 0) {
            throw new ServiceException("字典名称不可重复!");
        }
        boolean success = dictManager.updateById(SysDictConvert.INSTANCE.toDO(dto));
        if (success) {
            // 保存字典选项
            if (CollectionUtil.isEmpty(items)) {
                return true;
            }
            dto.setDictCode(dict.getDictCode());
            List<SysDictItemDTO> dtos = SysDictItemConvert.INSTANCE.update2dto(items);
            itemService.saveOrUpdate(dto, dtos);

        }
        return true;
    }

    /**
     * 删除字典
     *
     * @param ids 字典id
     * @return boolean
     * @author yunjin
     * @date 2021/11/30 11:03
     */

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDict(Set<Serializable> ids) {
        List<SysDictDTO> dicts = dictManager.listByDictIds(ids);
        if (dictManager.removeBatchByIds(ids)) {
            log.info("删除字典: {}", dicts);
        }
        for (SysDictDTO dict : dicts) {
            itemService.deleteByDict(dict);
        }
        return true;
    }

    /**
     * 根据名称获取下拉列表
     *
     * @param name 字典名称
     * @return com.carlos.dict.pojo.dto.DictDTO
     */
    public List<SysDictDTO> getListByName(String name) {
        List<SysDictDTO> list = this.dictManager.listDict(name);
        for (SysDictDTO dict : list) {
            List<SysDictItemDTO> items = itemService.getItemCache(dict.getDictCode());
            items.sort(Comparator.comparing(SysDictItemDTO::getSort));
            dict.setItems(items);
        }
        return list;
    }

    /**
     * 获取字典详情
     *
     * @param id 参数0
     * @return com.carlos.system.dict.pojo.dto.DictDTO
     * @author Carlos
     * @date 2022/12/7 23:54
     */
    public SysDictDTO getDetail(String id) {
        SysDictDTO dict = this.dictManager.getDictById(id);
        if (dict == null) {
            throw new ServiceException("字典不存在");
        }
        List<SysDictItemDTO> items = dictItemManager.listItems(Sets.newHashSet(dict.getId()), null, false);
        dict.setItems(items);
        return dict;
    }

    /**
     * 使用文本初始化字典
     *
     * @param path 文本路径
     * @author Carlos
     * @date 2023/4/6 19:32
     */
    public void initWithTxt(String path) {
        List<String> lines = FileUtil.readUtf8Lines(path);
        SysDictDTO dict = null;
        int dictCode = 25;
        List<SysDictCreateParam.Item> items = null;
        int sort = 1;
        for (String line : lines) {
            if (StrUtil.isBlank(line)) {
                if (dict != null) {
                    addDict(dict, items);
                    log.info("dict: {}", dict);
                    log.info("items: {}", items);
                    dict = null;
                }
                continue;
            }
            List<String> words = StrUtil.split(line, StrUtil.DASHED);
            if (words.size() == 1) {
                dict = new SysDictDTO();
                items = new ArrayList<>();
                sort = 1;
                dict.setDictName(words.get(0));
                dict.setDescription(words.get(0));
                dict.setDictCode(StrUtil.padPre(dictCode + "", 3, "0"));
                dictCode++;
                continue;
            }
            Item item = new Item();
            item.setItemName(words.get(1));
            item.setItemCode(words.get(0));
            item.setDescription(words.get(1));
            item.setSort(sort);
            sort++;
            items.add(item);
        }
    }
}
