package Test.test2019;

import java.text.MessageFormat;

public class StringTest {
    public void test() {
        format();
    }

    private void format() {
        Integer m = 5;
        String str = "12";
        String format1 = MessageFormat.format("m={0},str={1}", m, str);
        String format2 = String.format("m=%d,str=%s", m, str);
        Integer mr=0;
    }

}
