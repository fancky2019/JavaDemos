package Test.test2019;

import Model.Student;

import java.io.*;

/**
 * 实体类实现Serializable接口（空接口），该实体类可以序列化，否则不能序列化
 * 将实体类序列化成二进制，有点像C#的 [Serializable]特性
 * transient：忽略该字段的序列化。和C# [NonSerialized]特性。
 */
public class SerializableTest {

    /**
     * 忽略该字段的序列化。和C# [NonSerialized]特性。
     */
    private transient String name = "fancky";

    public void test() {
        serializable();
        deserializable();
    }

    private void serializable() {
        Student student = new Student();
        student.setName("fancky");
        student.setAge(27);
        // 把对象写到文件中,生成文件在src同级目录
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("student.txt"))) {
            oos.writeObject(student);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void deserializable() {
        // 从文件中读出对象生成文件在src同级目录
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("student.txt")))) {
            Student student = (Student) ois.readObject();
            System.out.println(student);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
