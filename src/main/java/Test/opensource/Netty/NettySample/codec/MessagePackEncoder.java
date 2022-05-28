package Test.opensource.Netty.NettySample.codec;

import Test.opensource.Netty.MessageInfo;
import Test.opensource.Netty.MessageType;
import Test.opensource.protobuf.model.PersonProto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;

import org.msgpack.jackson.dataformat.JsonArrayFormat;
import org.msgpack.jackson.dataformat.MessagePackFactory;
//import org.omg.CORBA.PUBLIC_MEMBER;

import java.nio.*;
import java.util.List;

/**
 * jackson-dataformat-msgpack序列化和msgpack不能交叉使用。
 */
public class MessagePackEncoder extends MessageToByteEncoder<Object> {

    ObjectMapper objectMapper = new ObjectMapper(new MessagePackFactory());

    public MessagePackEncoder() {
        //兼容V6,添加下面变成V6的序列化
//        objectMapper.setAnnotationIntrospector(new JsonArrayFormat());
    }

    //    jackson-dataformat-msgpack序列化,
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object msg, ByteBuf byteBuf) throws Exception {
        try {

            byte[] bytes = objectMapper.writeValueAsBytes(msg);
//            byteBuf=Unpooled.wrappedBuffer(bytes);

           /* Java基本类型（除了Boolean类型）都对应有一种缓冲区
             MappedByteBuffer ByteBuffer CharBuffer ShortBuffer   LongBuffer FloatBuffer  DoubleBuffer
             */
            byteBuf = Unpooled.directBuffer(bytes.length);
            byteBuf.writeBytes(bytes);

//            byte[] newBytes=new byte[byteBuf.readableBytes()];
//            byteBuf.getBytes(byteBuf.readerIndex(), newBytes, 0, byteBuf.readableBytes());
//            MessageInfo deserialized = objectMapper.readValue(newBytes , MessageInfo.class);

            channelHandlerContext.writeAndFlush(byteBuf);
        } catch (Exception ex) {
            String msg1 = ex.getMessage();
            int m = 0;
        }
    }


//    //msgpack 序列化
//    @Override
//    protected void encode(ChannelHandlerContext channelHandlerContext, Object msg, ByteBuf byteBuf) throws Exception {
//
//
//        MessagePack msgpack = new MessagePack();
//
//        try {
//            //在实体类加@Message 注解或者下面语句注册实体类，否则不能序列化
//            //注册：生成实体类模板
//            //注意：先注册子类的模板，有点类似js引人文件顺序
//
//            msgpack.register(MessageType.class);
//            msgpack.register(MessageInfo.class);
//
//            //序列化对象
//            // Serialize
//            byte[] bytes = msgpack.write(msg);
//
////            byteBuf=Unpooled.wrappedBuffer(bytes);
//
//            byteBuf = Unpooled.directBuffer(bytes.length);
//            byteBuf.writeBytes(bytes);
//
//            channelHandlerContext.writeAndFlush(byteBuf);
//        } catch (Exception ex) {
//            String msg1 = ex.getMessage();
//            int m = 0;
//        }
//
//    }


}
