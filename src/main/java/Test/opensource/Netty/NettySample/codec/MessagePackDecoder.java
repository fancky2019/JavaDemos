package Test.opensource.Netty.NettySample.codec;

import Test.opensource.Netty.MessageInfo;
import Test.opensource.Netty.MessageType;
import Test.opensource.protobuf.model.PersonProto;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.msgpack.MessagePack;
import org.msgpack.jackson.dataformat.MessagePackFactory;

import java.util.List;

/**
 * jackson-dataformat-msgpack序列化和msgpack不能交叉使用。
 */
public class MessagePackDecoder<T> extends MessageToMessageDecoder<ByteBuf> {

    private Class<T> clacc;
    ObjectMapper objectMapper = new ObjectMapper(new MessagePackFactory());

    public MessagePackDecoder(Class<T> clacc) {
        this.clacc = clacc;
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    //jackson-dataformat-msgpack序列化,
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {


        try {

            final int readableBytes = byteBuf.readableBytes();
//            ByteBuf byteBufIn = Unpooled.buffer(length);
//            byteBufIn.readBytes(byteBufIn);
//            byte[] inByte = byteBuf.array();

            if (readableBytes > 0) {
                byte[] bytes = new byte[readableBytes];
                byteBuf.getBytes(byteBuf.readerIndex(), bytes, 0, readableBytes);
//                byteBuf.getBytes(0, bytes);
                T t = objectMapper.readValue(bytes, clacc);
                if (t != null) {
                    list.add(t);
                }
            }
        } catch (Exception ex) {
            String msg1 = ex.getMessage();
            int m = 0;
        }


    }


//    //msgpack 反序列化
//    @Override
//    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
//
//
//        try {
//            MessagePack msgpack = new MessagePack();
//
//
//            //在实体类加@Message 注解或者下面语句注册实体类，否则不能序列化
//            //注册：生成实体类模板
//            //注意：先注册子类的模板，有点类似js引人文件顺序
//
//            msgpack.register(MessageType.class);
//            msgpack.register(MessageInfo.class);
//
//
//            final int readableBytes = byteBuf.readableBytes();
////            ByteBuf byteBufIn = Unpooled.buffer(length);
////            byteBufIn.readBytes(byteBufIn);
////            byte[] inByte = byteBuf.array();
//
//            if (readableBytes > 0) {
//                byte[] bytes = new byte[readableBytes];
//                byteBuf.getBytes(byteBuf.readerIndex(), bytes, 0, readableBytes);
////                byteBuf.getBytes(0, bytes);
//
//                // Deserialize
//                MessageInfo t = msgpack.read(bytes, MessageInfo.class);
//
//                if (t != null) {
//                    list.add(t);
//                }
//
//                msgpack.unregister(MessageInfo.class);
//                msgpack.unregister(MessageType.class);
//            }
//        } catch (Exception ex) {
//            String msg1 = ex.getMessage();
//            int m = 0;
//        }
//    }
}

