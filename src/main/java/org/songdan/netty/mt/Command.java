package org.songdan.netty.mt;

/**
 * @author: Songdan
 * @create: 2021-06-17 14:24
 **/
public interface Command {

    Byte LOGIN_REQUEST = 1;
    Byte LOGIN_RESPONSE = -1;
    Byte MESSAGE_REQUEST = 2;
    Byte MESSAGE_RESPONSE = -2;

}
