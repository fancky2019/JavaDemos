package Test.test2020;

import Test.opensource.Netty.MessageInfo;
import Test.opensource.Netty.MessageType;
import io.netty.buffer.Unpooled;
import org.msgpack.MessagePack;

import javax.xml.bind.DatatypeConverter;
//JDK 9
//import javax.xml.bind.DatatypeConverter;

/**
 * @Auther fancky
 * @Date 2020-9-14 17:55
 * @Description
 */
public class ByteBase64String {

    public void test() {
        fun();
    }

    private void fun() {

        MessagePack msgpack = new MessagePack();

        try {
            //在实体类加@Message 注解或者下面语句注册实体类，否则不能序列化
            //注册：生成实体类模板
            //注意：先注册子类的模板，有点类似js引人文件顺序

            MessageInfo msg = new MessageInfo();
            msg.setMessageType(MessageType.HeartBeat);
            msg.setBody("dsdssd");


            msgpack.register(MessageType.class);
            msgpack.register(MessageInfo.class);

            //序列化对象
            // Serialize
            byte[] bytes = msgpack.write(msg);

            String base64Str = DatatypeConverter.printBase64Binary(bytes);

            byte[] byteArray = DatatypeConverter.parseBase64Binary(base64Str);

            MessageInfo t = msgpack.read(bytes, MessageInfo.class);
            int m = 0;
        } catch (Exception ex) {
            String msg1 = ex.getMessage();
            int m = 0;
        }


    }
}
