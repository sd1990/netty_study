package org.songdan.netty.mt;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

/**
 * @author: Songdan
 * @create: 2021-06-17 15:01
 **/
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf requestByteBuf = (ByteBuf) msg;

        // 解码
        Packet packet = PacketCodeC.INSTANCE.decode(requestByteBuf);
        // 判断是否是登录请求数据包
        if (packet instanceof LoginRequestPacket) {
            LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
            loginResponsePacket.setVersion(packet.getVersion());
            LoginRequestPacket loginRequestPacket = (LoginRequestPacket) packet;
            // 登录校验
            if (valid(loginRequestPacket)) {
                // 校验成功
                System.out.println(new Date() + ":" + loginRequestPacket.getUserId() + "登陆成功");
                loginResponsePacket.setSuccess(true);
            } else {
                // 校验失败
                System.out.println(new Date() + ":" + loginRequestPacket.getUserId() + "登陆失败");
                loginResponsePacket.setSuccess(false);
            }
            ByteBuf responseByateBuf = PacketCodeC.INSTANCE.encode(ctx.alloc(), loginResponsePacket);
            ctx.channel().writeAndFlush(responseByateBuf);
        } else if(packet instanceof MessageRequestPacket) {
            MessageRequestPacket messageRequestPacket = ((MessageRequestPacket) packet);
            System.out.println(new Date() + ": 收到客户端消息: " + messageRequestPacket.getMessage());

            MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
            messageResponsePacket.setMessage("服务端回复【" + messageRequestPacket.getMessage() + "】");
            ByteBuf responseByteBuf = PacketCodeC.INSTANCE.encode(ctx.alloc(), messageResponsePacket);
            ctx.channel().writeAndFlush(responseByteBuf);
        }
    }

    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }
}
