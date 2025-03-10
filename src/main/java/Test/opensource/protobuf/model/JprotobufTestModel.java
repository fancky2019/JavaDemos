package Test.opensource.protobuf.model;

import com.baidu.bjf.remoting.protobuf.annotation.EnableZigZap;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Auther fancky
 * @Date 2020-8-19 15:33
 * @Description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor

@ProtobufClass
@EnableZigZap
public class JprotobufTestModel {
    private static final long serialVersionUID = 1L;
    private int age;
    private String name;
}
