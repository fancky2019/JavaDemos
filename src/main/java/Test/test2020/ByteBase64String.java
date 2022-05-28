package Test.test2020;

import Test.opensource.Netty.MessageInfo;
import Test.opensource.Netty.MessageType;
import io.netty.buffer.Unpooled;
import org.msgpack.core.MessagePack;

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



        try {

            //序列化对象
            // Serialize
            byte[] bytes = "".getBytes();

            String base64Str = DatatypeConverter.printBase64Binary(bytes);

            byte[] byteArray = DatatypeConverter.parseBase64Binary(base64Str);

//            MessageInfo t = msgpack.read(bytes, MessageInfo.class);
            int m = 0;
        } catch (Exception ex) {
            String msg1 = ex.getMessage();
            int m = 0;
        }


    }
}
