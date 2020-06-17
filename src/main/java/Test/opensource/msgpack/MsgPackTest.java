package Test.opensource.msgpack;

import org.msgpack.MessagePack;
import org.msgpack.annotation.Message;
import org.msgpack.packer.BufferPacker;
import org.msgpack.packer.MessagePackBufferPacker;
import org.msgpack.packer.Packer;
import org.msgpack.template.Template;
import org.msgpack.template.TemplateReference;
import org.msgpack.template.Templates;
import org.msgpack.unpacker.Unpacker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MsgPackTest {
    public void test() {
        serialize();
    }

    private void serialize() {
        MsgPackPojo pojo = new MsgPackPojo();
        pojo.setName("fancky");
        pojo.setAge(27);

        MessagePack msgpack = new MessagePack();

        try {
            //在实体类加@Message 注解或者下面语句注册实体类，否则不能序列化
            //注册：生成实体类模板
            msgpack.register(MsgPackPojo.class);
            //序列化对象
            // Serialize
            byte[] raw = msgpack.write(pojo);
            // Deserialize
            MsgPackPojo pojo1 = msgpack.read(raw, pojo.getClass());
            msgpack.unregister(MsgPackPojo.class);

           //string 类型及基础数据类型集合序列化，反序列化。
            List<String> listStr = new ArrayList<>();
            listStr.add("fancky");
            byte[] rawlistStr = msgpack.write(listStr, Templates.tList(Templates.TString));
            List<String> listStr1 = msgpack.read(rawlistStr, Templates.tList(Templates.TString));

            //序列化pojo集合
            List<MsgPackPojo> list = new ArrayList<>();
            list.add(pojo);
            //注册：生成实体类模板
            msgpack.register(MsgPackPojo.class);
            Template<MsgPackPojo> template = msgpack.lookup(MsgPackPojo.class);
            //Serialize
            byte[] rawList = msgpack.write(list, Templates.tList(template));
            //Deserialize
            List<MsgPackPojo> list1 = msgpack.read(rawList, Templates.tList(template));
            msgpack.unregister(MsgPackPojo.class);
            int m = 0;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }
}

//@Message
class MsgPackPojo {
    private String name;
    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
