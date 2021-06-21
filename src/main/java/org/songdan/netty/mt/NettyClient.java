package org.songdan.netty.mt;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Date;
import java.util.Scanner;

/**
 * @author: Songdan
 * @create: 2021-06-08 13:42
 **/
public class NettyClient {

    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        bootstrap
                .group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
//                        ch.pipeline().addLast(new FirstClientChannelHandler());
                        ch.pipeline().addLast(new ClientHandler());
                    }
                })
                .connect("127.0.0.1", 8000)
                .addListener(future -> {
                    if (future.isSuccess()) {
                        System.out.println("连接成功");
                        ChannelFuture channelFuture = (ChannelFuture) future;
                        Channel channel = channelFuture.channel();
                        startConsoleThread(channel);
                    } else {
                        System.out.println("连接失败");
                    }
                });
    }

    private static void startConsoleThread(Channel channel) {
        new Thread(() -> {
            while (!Thread.interrupted()) {
                if (LoginUtil.hasLogin(channel)) {
                    System.out.println("输入消息发送至服务端: ");
                    Scanner sc = new Scanner(System.in);
                    String line = sc.nextLine();

                    MessageRequestPacket packet = new MessageRequestPacket();
                    packet.setMessage(line);
                    ByteBuf byteBuf = PacketCodeC.INSTANCE.encode(channel.alloc(), packet);
                    channel.writeAndFlush(byteBuf);
                }
            }
        }).start();
    }

}
