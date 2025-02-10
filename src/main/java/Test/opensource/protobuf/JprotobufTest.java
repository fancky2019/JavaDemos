package Test.opensource.protobuf;

import Test.opensource.protobuf.model.JprotobufTestModel;
import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.Serializable;


public class JprotobufTest implements Serializable {
    public void test() {
// example code for usage
        Codec<JprotobufTestModel> simpleTypeCodec = ProtobufProxy
                .create(JprotobufTestModel.class);

        JprotobufTestModel stt = new JprotobufTestModel();
        stt.setName("abc");
        stt.setAge(100);
        try {
            // 序列化
            byte[] bb = simpleTypeCodec.encode(stt);
            // 反序列化
            JprotobufTestModel newStt = simpleTypeCodec.decode(bb);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
