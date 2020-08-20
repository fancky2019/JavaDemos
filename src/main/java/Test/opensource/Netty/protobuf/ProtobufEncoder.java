package Test.opensource.Netty.protobuf;

import Test.opensource.protobuf.model.PersonProto;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Auther fancky
 * @Date 2020-8-20 10:49
 * @Description
 */
public class ProtobufEncoder extends MessageToByteEncoder<PersonProto.Person> {
    @Override
    protected void encode(ChannelHandlerContext ctx, PersonProto.Person msg, ByteBuf out)
            throws Exception {
        byte[] bytes = msg.toByteArray();// 将对象转换为byte
        int length = bytes.length;// 读取消息的长度
        ByteBuf buf = Unpooled.buffer(2 + length);
        buf.writeShort(length);// 先将消息长度写入，也就是消息头
        buf.writeBytes(bytes);// 消息体中包含我们要发送的数据
        out.writeBytes(buf);
    }

}