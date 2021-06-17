package org.songdan.netty.decoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.songdan.netty.echo.client.EchoClientHandler;

import java.net.InetSocketAddress;

/**
 * 客户端
 *
 * @author: Songdan
 * @create: 2018-12-23 11:06
 **/
public class Client {

    private int port;

    private String host;

    public Client(int port, String host) {
        this.port = port;
        this.host = host;

    }

    private void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel sc)
                                throws Exception {
                            //设置定长字符串接收
                            sc.pipeline().addLast(new MyFixedLengthDecoder(5));
                            //设置字符串形式的编码
                            sc.pipeline().addLast(new StringEncoder());
                            //设置字符串解码
                            sc.pipeline().addLast(new StringDecoder());
                            sc.pipeline().addLast(new ClientHandler());

                        }
                    });
            ChannelFuture f = b.connect().sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    class ClientHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("client receive: " + msg);
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ctx.channel().writeAndFlush("aaaaabbbbbccccc");
        }
    }



    public static void main(String[] args) throws Exception {
        new Client(10001, "127.0.0.1").start();
    }
}
