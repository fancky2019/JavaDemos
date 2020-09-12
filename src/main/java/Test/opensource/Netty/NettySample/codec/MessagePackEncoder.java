package Test.opensource.Netty.NettySample.codec;

import Test.opensource.protobuf.model.PersonProto;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.msgpack.MessagePack;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.List;

public class MessagePackEncoder<T> extends MessageToMessageEncoder<Object> {

    Class<T> clacc;

    public MessagePackEncoder(Class<T> clacc) {
        this.clacc = clacc;
    }

//    @Override
//    protected void encode(ChannelHandlerContext ctx, T msg, ByteBuf out)
//            throws Exception {
//        try {
//            MessagePack msgpack = new MessagePack();
//            //在实体类加@Message 注解或者下面语句注册实体类，否则不能序列化
//            //注册：生成实体类模板
////            msgpack.register(clacc);
//            // Serialize
//            byte[] messageBytes = msgpack.write(msg);
//            // Now serializable...
//
////        ByteBuf byteBuf = Unpooled.directBuffer(messageBytes.length);
////        byteBuf.writeBytes(messageBytes);
//
//            ByteBuf byteBuf = Unpooled.wrappedBuffer(messageBytes);
//            ctx.writeAndFlush(byteBuf);
//        }
//        catch (Exception ex)
//        {
//            String msg1=ex.getMessage();
//            int m=0;
//        }
//
//    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {

        try {
            MessagePack msgpack = new MessagePack();
            byte[] bytes = msgpack.write(msg);
            out.add(bytes);
        } catch (Exception ex) {
            String msg1 = ex.getMessage();
            int m = 0;
        }

    }

}
