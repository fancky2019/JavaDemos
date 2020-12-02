package Test.test2020;

import utility.ByteConverter;

public class ByteConverterTest {
    public void test() {
        //false:0,true:1
        byte[] bytes = ByteConverter.getByteArray(true);
        boolean b = ByteConverter.getBoolean(bytes);

        int m = 0;
    }

}
