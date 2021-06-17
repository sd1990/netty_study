package org.songdan.netty.mt;

import lombok.Data;

/**
 * 通信包
 *
 * @author: Songdan
 * @create: 2021-06-17 14:22
 **/
@Data
public abstract class Packet {

    private byte version = -1;

    public abstract Byte getCommand();

}
