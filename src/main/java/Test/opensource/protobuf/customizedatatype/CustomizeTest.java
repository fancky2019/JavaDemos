package Test.opensource.protobuf.customizedatatype;

import Test.opensource.protobuf.customizedatatype.pojo.Customer;
import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import com.google.type.Date;
import com.google.type.Money;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CustomizeTest {
    /*
    https://blog.danielpadua.dev/posts/understanding-protocol-buffers-protobuf/
       注：自定义类型 除了Well Known Type 不需要生成类型，其他需要生成proto引入，添加到项目
     */

    public  void test()
    {

    }
    private  void  fun()
    {
        Date birthdate = Utils.toGoogleDate(LocalDate.of(1990, 4, 30));
        Money balance = Utils.toGoogleMoney(BigDecimal.valueOf(9000.53));
        Timestamp createdUpdateAt = Utils.toGoogleTimestampUTC(LocalDateTime.now());
        String fullPath = "/Users/danielpadua/protobuf/protobuf-customer";

        try (FileOutputStream fos = new FileOutputStream(fullPath)) {
            Customer daniel = Customer.newBuilder()
                    .setId(1)
                    .setPhoto(ByteString.EMPTY)
                    .setName("Daniel")
                    .setBirthdate(birthdate)
                    .setBalance(balance)
                    .setCreatedAt(createdUpdateAt)
                    .setUpdatedAt(createdUpdateAt)
                    .build();

            daniel.writeTo(fos);
            System.out.println("protobuf-customer created successfully");
        } catch (FileNotFoundException e) {
            System.out.println(MessageFormat.format("could not find file {0}", fullPath));
        } catch (IOException e) {
            System.out.println(MessageFormat.format("error while reading file {0}. exception: {1}", fullPath, e.getMessage()));
        }
    }
}
