package com.yunjin.tool.encrypt.service;


/**
 * <p>
 * 数据处理器
 * </p>
 *
 * @author Carlos
 * @date 2023/9/5 9:16
 */
public interface DataHandler {


    /**
     * 处理逻辑
     *
     * @author Carlos
     * @date 2023/9/5 23:15
     */
    void handle();

    /**
     * 获取进度
     *
     * @author Carlos
     * @date 2023/9/5 23:15
     */
    String getProcess();

    /**
     * 设置进度
     */
    void setProcess(String process);
}
