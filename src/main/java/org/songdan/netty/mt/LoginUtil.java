package org.songdan.netty.mt;

import io.netty.channel.Channel;
import io.netty.util.Attribute;

import java.util.Objects;

/**
 * @author: Songdan
 * @create: 2021-06-21 12:39
 **/
public class LoginUtil {

    public static void markAsLogin(Channel channel) {
        channel.attr(Attributes.LOGIN).set(true);
    }

    public static boolean hasLogin(Channel channel) {
        return Objects.nonNull(channel.attr(Attributes.LOGIN).get());
    }

}
