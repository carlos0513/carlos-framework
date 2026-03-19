package com.carlos.system.configration.controller;


import com.carlos.core.pagination.Paging;
import com.carlos.core.param.ParamIdSet;
import com.carlos.system.configration.convert.SysConfigConvert;
import com.carlos.system.configration.manager.SysConfigManager;
import com.carlos.system.configration.pojo.dto.SysConfigDTO;
import com.carlos.system.configration.pojo.param.SysConfigCreateParam;
import com.carlos.system.configration.pojo.param.SysConfigPageParam;
import com.carlos.system.configration.pojo.param.SysConfigUpdateParam;
import com.carlos.system.configration.pojo.vo.SysConfigLoginPageVO;
import com.carlos.system.configration.pojo.vo.SysConfigVO;
import com.carlos.system.configration.service.SysConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 系统配置 rest服务接口
 * </p>
 *
 * @author carlos
 * @date 2022-11-3 13:47:54
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys/config")
@Tag(name = "系统配置")
public class SysConfigController {

    public static final String BASE_NAME = "系统配置";

    private final SysConfigService configService;

    private final SysConfigManager configManager;


    @PostMapping
    @Operation(summary = "新增" + BASE_NAME)

    public void add(@RequestBody @Validated SysConfigCreateParam param) {
        SysConfigDTO dto = SysConfigConvert.INSTANCE.toDTO(param);
        this.configService.addSysConfig(dto);
    }

    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)

    public void delete(@RequestBody ParamIdSet<Serializable> param) {
        this.configService.deleteSysConfig(param.getIds());
    }


    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)

    public void update(@RequestBody @Validated SysConfigUpdateParam param) {
        SysConfigDTO dto = SysConfigConvert.INSTANCE.toDTO(param);
        this.configService.updateSysConfig(dto);
    }

    @GetMapping("{id}")
    @Operation(summary = BASE_NAME + "详情")

    public SysConfigVO detail(@PathVariable String id) {
        return SysConfigConvert.INSTANCE.toVO(this.configManager.getDtoById(id));
    }

    @GetMapping("code")
    @Operation(summary = "根据code获取配置")

    public SysConfigVO getByCode(String code) {
        return SysConfigConvert.INSTANCE.toVO(this.configService.getByCode(code));
    }

    @GetMapping("all")
    @Operation(summary = "获取全部配置")

    public Map<String, SysConfigVO> getAllConfig() {
        List<SysConfigDTO> configs = configService.listConfig();
        Map<String, SysConfigVO> map = new HashMap<>();
        for (SysConfigDTO config : configs) {
            map.put(config.getConfigCode(), SysConfigConvert.INSTANCE.toVO(config));
        }
        return map;
    }

    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")

    public Paging<SysConfigVO> page(SysConfigPageParam param) {
        return this.configManager.getPage(param);
    }


    @GetMapping("loginpage")
    @Operation(summary = "登录页配置")
    public SysConfigLoginPageVO loginpage() {
        return configService.getHomePageConfig();
    }
}
