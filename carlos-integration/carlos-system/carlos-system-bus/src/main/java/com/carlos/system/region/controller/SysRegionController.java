package com.carlos.system.region.controller;


import cn.hutool.core.util.StrUtil;
import cn.idev.excel.EasyExcel;
import com.carlos.core.exception.ServiceException;
import com.carlos.core.pagination.Paging;
import com.carlos.core.param.ParamIdSet;
import com.carlos.core.response.Result;
import com.carlos.log.annotation.Log;
import com.carlos.log.enums.BusinessType;
import com.carlos.system.region.convert.SysRegionConvert;
import com.carlos.system.region.listener.RegionExcelListener;
import com.carlos.system.region.manager.SysRegionManager;
import com.carlos.system.region.pojo.dto.SysRegionDTO;
import com.carlos.system.region.pojo.excel.RegionExcel;
import com.carlos.system.region.pojo.param.SysRegionConvertParam;
import com.carlos.system.region.pojo.param.SysRegionCreateParam;
import com.carlos.system.region.pojo.param.SysRegionPageParam;
import com.carlos.system.region.pojo.param.SysRegionUpdateParam;
import com.carlos.system.region.pojo.vo.SysRegionVO;
import com.carlos.system.region.service.SysRegionService;
import com.carlos.util.easyexcel.ExcelUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;


/**
 * <p>
 * 行政区域划分 rest服务接口
 * </p>
 *
 * @author yunjin
 * @date 2022-11-8 19:30:24
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys/region")
@Tag(name = "行政区域管理")
public class SysRegionController {

    public static final String BASE_NAME = "行政区域";

    private final SysRegionService regionService;

    private final SysRegionManager regionManager;


    @PostMapping
    @Operation(summary = "新增" + BASE_NAME)
    @Log(title = "区域管理", businessType = BusinessType.INSERT)
    public void add(@RequestBody @Validated final SysRegionCreateParam param) {
        final SysRegionDTO dto = SysRegionConvert.INSTANCE.toDTO(param);
        this.regionService.addSysRegion(dto);
    }


    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    @Log(title = "区域管理", businessType = BusinessType.DELETE)
    public void delete(@RequestBody final ParamIdSet<Serializable> param) {
        this.regionService.deleteSysRegion(param.getIds());
    }


    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    @Log(title = "区域管理", businessType = BusinessType.UPDATE)
    public void update(@RequestBody @Validated final SysRegionUpdateParam param) {
        final SysRegionDTO dto = SysRegionConvert.INSTANCE.toDTO(param);
        this.regionService.updateSysRegion(dto);
    }

    @GetMapping("{id}")
    @Operation(summary = BASE_NAME + "详情")
    public SysRegionVO detail(@PathVariable final String id) {
        return SysRegionConvert.INSTANCE.toVO(this.regionManager.getDtoById(id));
    }

    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    public Paging<SysRegionVO> page(final SysRegionPageParam param) {
        return this.regionManager.getPage(param);
    }


    @GetMapping("tree/list")
    @Operation(summary = "行政区域树形列表", description = "一次性全部加载")
    @Parameters({
            @Parameter(name = "regionCode", description = "当前区域code"),
            @Parameter(name = "regionName", description = "当前区域名称"),
            @Parameter(name = "codeTop", description = "是否以传入的code作为顶级树的开始")
    })
    public Result<List<SysRegionVO>> treeList(final String parentId, final String regionName, final String regionCode, final boolean codeTop) {
        return Result.ok(this.regionService.getRegionTree(parentId, regionName, regionCode, true, codeTop));
    }

    @GetMapping("loadStep")
    @Operation(summary = "逐级加载行政区域")
    @Parameters({@Parameter(name = "regionCode", description = "当前区域code")})
    public List<SysRegionVO> lodeStep(final String regionCode) {
        final List<SysRegionDTO> dtos = this.regionService.loadRegionStep(regionCode);
        return SysRegionConvert.INSTANCE.dto2vo(dtos);
    }


    @PostMapping("import")
    @Operation(summary = "0-导入区域")
    @Log(title = "区域管理--导入区域", businessType = BusinessType.IMPORT)
    public Result<?> importData(@RequestPart final MultipartFile file) {
        final RegionExcelListener listener = new RegionExcelListener(regionService);
        try {
            final String filename = file.getOriginalFilename();
            if (StrUtil.isBlank(filename)) {
                throw new ServiceException("文件名不能为空");
            }
            ExcelUtil.checkExcel(filename);
            EasyExcel.read(file.getInputStream(), RegionExcel.class, listener).sheet().doRead();
        } catch (final IOException e) {
            return Result.fail("数据导入失败！");
        }
        return Result.ok("区域数据初始化导入成功！");
    }


    @PostMapping("importV2")
    @Operation(summary = "0-导入区域V2", description = "支持自定义表字段和多sheet")
    public Result<?> id2code(SysRegionConvertParam param) {
        MultipartFile file = param.getFile();
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            return Result.fail("请选择文件！");
        }
        regionService.importCustomRegions(param);

        return Result.ok("区域数据初始化导入成功！");
    }


    @GetMapping("export")
    @Operation(summary = "导出数据")
    @Log(title = "行政区域--导出区域", businessType = BusinessType.EXPORT)
    public void exportUser(final HttpServletResponse response) {
        regionService.export(response, false);
    }


    @GetMapping("export/template")
    @Operation(summary = "导出模板")
    @Log(title = "行政区域--导出模板", businessType = BusinessType.EXPORT)
    public void exportTemplate(final HttpServletResponse response) {
        regionService.export(response, true);
    }


    @PostMapping("parents/reset")
    @Operation(summary = "0-初始化父级编码")
    public void resetParents() {
        regionService.resetParents();
    }

    @PostMapping("cache/init")
    @Operation(summary = "0-缓存初始化")
    @Log(title = "初始化区域缓存", businessType = BusinessType.INSERT)
    public void cacheInit() {
        this.regionManager.initCache();
    }


    @PostMapping("cache/clear")
    @Operation(summary = "0-缓存清空")
    public void cacheClear() {
        regionManager.clearCache();
    }

    @GetMapping("/getAllParentRegions")
    @Operation(summary = "根据code，获取上级区域,传入C,并按照A、B、C层级返回")
    public List<SysRegionVO> getAllParentRegions(@RequestParam(required = false) String regionCode) {
        List<SysRegionDTO> dto = regionService.getAllParentRegions(regionCode);
        return SysRegionConvert.INSTANCE.dto2vo(dto);
    }


}
