package org.songdan.netty.decoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author: Songdan
 * @create: 2018-12-23 11:06
 **/
public class Server {

    private int port;


    public Server(int port) {
        this.port = port;
    }

    private void init() throws InterruptedException {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup(4);
        try {
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            //设置定长字符串接收(5个长度)
                            sc.pipeline().addLast(new MyFixedLengthDecoder(5));
                            //设置字符串形式的编码
                            sc.pipeline().addLast(new MyStringEncoder());
                            //设置字符串形式的解码，以后在Handler那里获取的msg就是String类型的了。
                            sc.pipeline().addLast(new StringDecoder());
                            sc.pipeline().addLast(new ServerHandler()); //配置具体数据接收方法的处理器

                        }
                    });
            ChannelFuture future = serverBootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully().sync();
            workGroup.shutdownGracefully().sync();
        }
    }


    class ServerHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            //因为配置了字符串形式的解码，所以msg会是String类型
            String data = (String) msg;
            System.out.println("Server接收到的数据:" + data);
            //服务器给客户端返回数据
            String str = "abc";
            ctx.writeAndFlush(str);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new Server(10001).init();
    }

}
