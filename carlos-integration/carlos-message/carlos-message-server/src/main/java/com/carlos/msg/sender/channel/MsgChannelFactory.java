package com.carlos.msg.sender.channel;


import com.carlos.message.channel.ChannelConfig;
import com.carlos.msg.api.pojo.enums.ChannelType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  channel构造工厂
 * </p>
 *
 * @author Carlos
 * @date 2025-03-25 10:50 
 */
@Slf4j
@Component
public class MsgChannelFactory {

    // FIXME: Carlos 2025-05-07 如果采用本地存储，会出现分布式问题
    private Map<String, Channel> channels = new HashMap<>();


    public Channel getChannel(ChannelType channelType) {

        return null;
    }

    public void disable(ChannelType channelType) {
    }

    public void enable(ChannelType channelType) {
    }

    public void reload(ChannelType channelType, ChannelConfig channelConfig) {
    }

    public void regiest(ChannelType channelType, ChannelConfig channelConfig) {
    }
}
