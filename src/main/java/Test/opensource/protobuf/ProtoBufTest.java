package Test.opensource.protobuf;


import Test.opensource.protobuf.model.PersonProto;
import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;

import java.time.LocalDateTime;

/**
 * JProtobuf
 *
 *
 *
 *
 * @Auther fancky
 * @Date 2020-8-19 15:49
 * @Description
 */
public class ProtoBufTest {

    /*
      官方文档：https://developers.google.com/protocol-buffers/docs/reference/java-generated
     一、github https://github.com/protocolbuffers/protobuf 下载release的版本 protoc-3.13.0-win64 在页面列表的最下方
     二、解压，在bin下建立Person.txt。重命名为Person。
     三、用nodepad++编辑。
        实例：
            syntax = "proto3";

            // 引入外部的proto对象
            import "google/protobuf/any.proto";


            //option java_multiple_files = true;
            option java_package = "Test.opensource.protobuf.model";//类包名：从工程中名称赋值，
                                                                    //待会还要把生成的Person 拷贝到工程
            option java_outer_classname = "PersonProto";//生成的类名
            message Person {//类成员
                int32 id = 1;
                string name = 2;
                int32 age = 3;
                Gender gender = 5; // Enum值
                repeated string sons = 4; // List列表
                map<string, Job> sonJobs = 6; // 定义Map对象
                repeated Job jobs = 7; // List列表
                repeated google.protobuf.Any any = 8; // Any对象,泛型。可以用作分页数据的data:List<T>
            }
            //成员嵌套类型
            message Job {
                    string name = 1;
                    double salary = 2;
                }
            //成员嵌套类型
            enum Gender {
              MAN = 0;
              WOMAN = 1;
            }

  四、当前bin目录下，cmd执行对应proto
      protoc.exe --java_out=./ Person.proto
      将生成的class复制到项目中。
  五、Maven添加项目依赖 ProtoBuf。注意：版本号要和protoc-3.13.0-win64中的版本号一致。
     Maven依赖见pom.xml
     */


   /*注：BigDecimal BigInteger Timestamp LocalDteTime
   等平台特有类型可以用string代替，或者用float double

   //protobuf只负责数据传输，可以定义一个model只有基本数据类型和string，将不是基本数据类型的处理成 string,
   在接收端再将string类型转换成对应平台的业务model.


   //自定义数据类型虽然满足protobuf进行数据传输，但是还要需要平台业务类型转换成自定义类型，避免不了类型转换。

     参考类型处理：（翻墙）https://blog.danielpadua.dev/posts/understanding-protocol-buffers-protobuf/
    proto拓展类型： https://github.com/googleapis/googleapis/tree/master/google/type


    syntax = "proto3";

package danielpadua.protobufexample.contracts;

option java_multiple_files = true;
option java_outer_classname = "CustomerProto";
option java_package = "dev.danielpadua.protobufexamplejava.contracts";
option csharp_namespace = "DanielPadua.ProtobufExampleDotnet.Contracts";

import "google/protobuf/timestamp.proto";
import "money.proto";
import "date.proto";

message Customer {
    int64 id = 1;
    bytes photo = 2;
    string name = 3;
    google.type.Date birthdate = 4;
    google.type.Money balance = 5;
    google.protobuf.Timestamp createdAt = 6;
    google.protobuf.Timestamp updatedAt = 7;
}
    */


    /*
    传输：
     PO、DTO-->proto
     */

    public void test() {
        /*
        Person:生成的PersonProto
        Person:Person.proto文件的Person类
         */

        PersonProto.Person.Builder builder = PersonProto.Person.newBuilder();
        builder.setId(1)
                .setAge(27)
                .setName("fancky")
                .addJobs(PersonProto.Job.newBuilder().setName("程序员").setSalary(777).build())//List<Job>
                .setGender(PersonProto.Gender.MAN)//枚举
                .addSons("li")//List<String>
                .addSons("zi")//List<String>
                .addAny(Any.pack(PersonProto.Job.newBuilder().setName("程序员any").setSalary(777).build())) //Any 必须是proto 文件里定义的message  类型
                .putSonJobs("li", PersonProto.Job.newBuilder().setName("程序员").setSalary(777).build())//Map<String,Job>
        ;
        PersonProto.Person person = builder.build();

        //二进制序列化

        //序列化
        byte[] personBytes = person.toByteArray();

        try {
            //反序列化
            PersonProto.Person parseFrom = PersonProto.Person.parseFrom(personBytes);
            int m = 0;
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }


        //Json 序列化：已格式化
        //Json序列化一：  --有Any报错用Json序列化二
        //Jackson 序列化Json,参照Jackson的Demo
//        JsonFormat.Printer printer = JsonFormat.printer();
//        String jsonStr = "";
//        try {
//            jsonStr = printer.print(person);
//            int m = 0;
//        } catch (InvalidProtocolBufferException e) {
//            e.printStackTrace();
//        }
//        //Json 反序列化
//        JsonFormat.Parser parser = JsonFormat.parser();
//        try {
//            PersonProto.Person.Builder newBuilder = PersonProto.Person.newBuilder();
//            parser.merge(jsonStr, newBuilder);
//            int m = 0;
//        } catch (InvalidProtocolBufferException e) {
//            e.printStackTrace();
//        }

        //Json序列化二:序列化的时候指定类型usingTypeRegistry
        //定义typeRegistry
        JsonFormat.TypeRegistry typeRegistry = JsonFormat.TypeRegistry.newBuilder().add(PersonProto.Person.getDescriptor()).build();
        String jsonStr2 = "";
        try {
            jsonStr2 = JsonFormat.printer().usingTypeRegistry(typeRegistry).print(person);
            int m = 0;
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }


        //Json 反序列化
        JsonFormat.Parser parser = JsonFormat.parser();
        try {
            PersonProto.Person.Builder newBuilder = PersonProto.Person.newBuilder();
            parser.usingTypeRegistry(typeRegistry).merge(jsonStr2, newBuilder);
            int m = 0;
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

        int n = 0;
    }
}


