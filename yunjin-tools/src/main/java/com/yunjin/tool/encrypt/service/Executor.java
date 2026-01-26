package com.yunjin.tool.encrypt.service;


import com.yunjin.tool.encrypt.config.DatabaseInfo;
import com.yunjin.tool.encrypt.config.ToolInfo;
import com.yunjin.tool.encrypt.enums.ToolType;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 代码生成类
 * </p>
 *
 * @author Carlos
 * @date 2019/10/19 23:45 ---------     -------------   --------------------------------------
 */
@Slf4j
public class Executor {

    private final DatabaseService databaseService;

    private final ToolInfo toolInfo;
    private DataHandler handler = null;

    public Executor(DatabaseInfo databaseInfo, ToolInfo toolInfo) {
        databaseService = new DatabaseService(databaseInfo);
        this.toolInfo = toolInfo;
    }

    public void execute() {
        ToolType toolType = toolInfo.getToolType();
        if (log.isDebugEnabled()) {
            log.debug("开始进行[" + toolType.getDesc() + "]处理..........................................................................");
        }


        switch (toolType) {

            case SM4_ENCRYPT:
                handler = new EncryptHandler(databaseService, toolInfo);
                break;
            case SM4_DECRYPT:
                handler = new DecryptHandler(databaseService, toolInfo);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + toolType);
        }
        handler.handle();

    }

    /**
     * 获取执行进度
     *
     * @return java.lang.String
     * @author Carlos
     * @date 2023/9/5 23:24
     */
    public String getProcess() {
        if (handler == null) {
            return "";
        }
        return handler.getProcess();
    }
}
