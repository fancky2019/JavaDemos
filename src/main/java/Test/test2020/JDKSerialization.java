package Test.test2020;

import Model.JacksonPojo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @Auther fancky
 * @Date 2020-9-14 10:59
 * @Description
 */
public class JDKSerialization {
    public static byte[] serialization(Object object) {
        byte[] bytes = null;

        //  Serialization/Deserialization


        //jdk序列化
        ByteArrayOutputStream byteArrayOutputStream = null;
        ObjectOutputStream out = null;

        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            out = new ObjectOutputStream(byteArrayOutputStream);
            out.writeObject(object);
            bytes = byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
                byteArrayOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return bytes;
    }


    public static <T> T deserialization(byte[] bytes) {

        //  Serialization/Deserialization


        T t = null;

        //jdk7 Java-try-with-resource
        //jdk 反序列化
        try (ByteArrayInputStream fileIn = new ByteArrayInputStream(bytes);
             ObjectInputStream in = new ObjectInputStream(fileIn);) {
            t = (T) in.readObject();

        } catch (Exception ex) {

        }
        int m = 0;
        return t;
    }
}
