package org.songdan.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @author: Songdan
 * @create: 2018-12-23 12:09
 **/
public class MyStringEncoder extends StringEncoder {

    private Charset charset;

    public MyStringEncoder() {
        this(Charset.defaultCharset());
    }

    /**
     * Creates a new instance with the specified character set.
     */
    public MyStringEncoder(Charset charset) {
        super(charset);
        this.charset = charset;
    }


    @Override
    protected void encode(ChannelHandlerContext ctx, CharSequence msg, List<Object> out) throws Exception {
        if (msg.length() == 0) {
            return;
        }

        ByteBuf byteBuf = ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap(msg), charset);
        byte[] bytes = new byte[byteBuf.readableBytes()];
        Unpooled.wrappedBuffer(byteBuf).readBytes(bytes);
        System.out.println("encode string is:"+new String(bytes));
        out.add(byteBuf);
    }

}
