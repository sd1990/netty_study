package org.songdan.netty.mt;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * @author: Songdan
 * @create: 2021-06-17 14:40
 **/
public class PacketCodeC {

    private static final int MAGIC_NUMBER = 0x12345678;

    public static PacketCodeC INSTANCE = new PacketCodeC();

    public ByteBuf encode(ByteBufAllocator alloc, Packet packet) {
        ByteBuf byteBuf = alloc.buffer();
        byte[] bytes = Serializer.DEFAULT.serialize(packet);
        //4个字节魔数
        byteBuf.writeInt(MAGIC_NUMBER);
        //协议版本
        byteBuf.writeByte(packet.getVersion());
        //序列化算法
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        //指令类型
        byteBuf.writeByte(packet.getCommand());
        //内容长度
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
        return byteBuf;
    }

    public Packet decode(ByteBuf byteBuf) {
        // 跳过 magic number
        byteBuf.skipBytes(4);

        // 跳过版本号
        byteBuf.skipBytes(1);

        // 序列化算法标识
        byte serializeAlgorithm = byteBuf.readByte();

        // 指令
        byte command = byteBuf.readByte();

        // 数据包长度
        int length = byteBuf.readInt();

        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        Class<? extends Packet> requestType = getRequestType(command);
        Serializer serializer = getSerializer(serializeAlgorithm);

        if (requestType != null && serializer != null) {
            return serializer.deserialize(requestType, bytes);
        }

        return null;
    }

    private Serializer getSerializer(byte serializeAlgorithm) {
        if (Serializer.JSON_SERIALIZER == serializeAlgorithm) {
            return Serializer.DEFAULT;
        }
        return null;
    }

    private Class<? extends Packet> getRequestType(byte command) {
        if (Command.LOGIN_REQUEST == command) {
            return LoginRequestPacket.class;
        }
        if (Command.LOGIN_RESPONSE == command) {
            return LoginResponsePacket.class;
        }
        if (Command.MESSAGE_REQUEST == command) {
            return MessageRequestPacket.class;
        }
        if (Command.MESSAGE_RESPONSE == command) {
            return MessageResponsePacket.class;
        }
        return null;
    }


}
