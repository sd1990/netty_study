package org.songdan.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.FixedLengthFrameDecoder;

/**
 * @author: Songdan
 * @create: 2018-12-23 12:02
 **/
public class MyFixedLengthDecoder extends FixedLengthFrameDecoder {
    private int myLength;

    /**
     * Creates a new instance.
     *
     * @param frameLength the length of the frame
     */
    public MyFixedLengthDecoder(int frameLength) {
        super(frameLength);
        this.myLength = frameLength;
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        System.out.println("readable bytes is " + in.readableBytes());
        if (in.readableBytes() < myLength) {
            byte[] intactBytes = new byte[in.readableBytes()];
            Unpooled.wrappedBuffer(in).readBytes(intactBytes);
            System.out.println(new String(intactBytes));
            return null;
        } else {
            return in.readRetainedSlice(myLength);
        }
    }
}
