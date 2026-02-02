package com.carlos.fx.encrypt.service;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.SM4;
import cn.hutool.db.*;
import com.carlos.fx.encrypt.config.DatabaseInfo;
import com.carlos.fx.encrypt.config.ToolInfo;
import com.carlos.fx.encrypt.config.ToolInfo.Encrypt;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 解密处理器
 * </p>
 *
 * @author Carlos
 * @date 2023/9/5 23:14
 */
@Slf4j
public class EncryptHandler implements DataHandler {

    private final DatabaseService databaseService;

    private final ToolInfo toolInfo;

    private String process;


    public EncryptHandler(DatabaseService databaseService, ToolInfo toolInfo) {
        this.databaseService = databaseService;
        this.toolInfo = toolInfo;
    }


    @Override
    public void handle() {
        List<String> fields = toolInfo.getSelectFields();
        if (CollUtil.isEmpty(fields)) {
            log.error("加密字段为空");
            return;
        }
        log.info(String.format("加密字段为：%s", StrUtil.join(StrUtil.COMMA, fields)));
        Encrypt encrypt = toolInfo.getEncrypt();
        String key = encrypt.getKey();
        String iv = encrypt.getIv();
        SM4 sm4 = new SM4(Mode.CBC, Padding.PKCS5Padding, key.getBytes(), iv.getBytes());

        // ThreadPoolExecutor pool = ExecutorUtil.get(6, 8, "data-decrypt-", 100, null);

        DatabaseInfo dbinfo = databaseService.getDatabaseInfo();
        String targetTable = toolInfo.getTargetTable();

        Db db = DbUtil.use(dbinfo.getDataSource());
        int size = 5000;
        int page = 0;


        List<Entity> list = null;
        PageResult<Entity> pageResult;
        int num = 1;
        do {
            try {
                pageResult = db.page(new Entity(toolInfo.getSourceTable()), new Page(page, size));
                log.info("数据读取成功：" + toolInfo.getSourceTable());
            } catch (SQLException e) {
                log.error("源数据查询失败！", e);
                return;
            }
            List<Entity> newList = new ArrayList<>();
            for (Entity entity : pageResult) {
                process = String.format("处理第%s批数据， %s/%s", page + 1, num, (page + 1) * size);
                log.info(process);
                for (String field : fields) {
                    Object value = entity.get(field);
                    Optional.ofNullable(value).ifPresent(i -> {
                        String valueStr = (String) value;
                        if (StrUtil.isBlank(valueStr)) {
                            return;
                        }
                        if (valueStr.equalsIgnoreCase("NULL")) {
                            entity.set(field, null);
                        }
                        String encryptStr;
                        try {
                            encryptStr = sm4.encryptBase64(valueStr);
                            entity.set(field, encryptStr);
                        } catch (Exception e) {
                            log.warn("数据加密失败：" + valueStr, e);
                        }
                    });
                }
                entity.setTableName(targetTable);
                newList.add(entity);
                num++;
            }
            try {
                log.info("数据处理完成:开始写入数据到目标表");
                db.insert(newList);
                setProcess(String.format("第%s批数据保存成功", page + 1));
                log.info(String.format("数据写入成功:%s", targetTable));
            } catch (SQLException e) {
                log.error("数据写入失败！", e);
            }
            page++;
        } while (!pageResult.isEmpty());
    }

    @Override
    public String getProcess() {
        return this.process;
    }

    @Override
    public void setProcess(String process) {
        this.process = process;
    }
}
