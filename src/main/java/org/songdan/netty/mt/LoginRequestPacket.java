package org.songdan.netty.mt;

import lombok.Data;

/**
 * @author: Songdan
 * @create: 2021-06-17 14:25
 **/
@Data
public class LoginRequestPacket extends Packet {

    private String userId;

    private String username;

    private String password;


    @Override
    public Byte getCommand() {
        return Command.LOGIN_REQUEST;
    }
}
