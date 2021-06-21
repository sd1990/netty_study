package org.songdan.netty.mt;

import lombok.Data;

/**
 * 消息请求包
 *
 * @author: Songdan
 * @create: 2021-06-20 12:52
 **/
@Data
public class MessageRequestPacket extends Packet {

    private String message;

    @Override
    public Byte getCommand() {
        return Command.MESSAGE_REQUEST;
    }
}
