package Test.opensource.Netty.NettySample.codec;

import Test.opensource.protobuf.model.PersonProto;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

public class MessagePackDecoder<T> extends ByteToMessageDecoder {

    private Class<T> clacc;

    public MessagePackDecoder(Class<T> clacc) {
        this.clacc = clacc;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int readableBytes = in.readableBytes();

        if (readableBytes > 0) {
            try {
                ByteBuf byteBuf = Unpooled.buffer(readableBytes);
                in.readBytes(byteBuf);
                MessagePack msgpack = new MessagePack();
                //在实体类加@Message 注解或者下面语句注册实体类，否则不能序列化
                //注册：生成实体类模板
                msgpack.register(clacc);
                byte[] inByte = byteBuf.array();
                // Deserialize
               T t=  msgpack.read(inByte,clacc);
                msgpack.unregister(clacc);

                if (t != null) {
                    out.add(t);
                }
            } catch (Exception innerException) {
                throw new Exception(innerException.getMessage());
            }
            in.skipBytes(readableBytes);
        }
    }
}
