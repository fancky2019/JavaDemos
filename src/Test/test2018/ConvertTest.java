package Test.test2018;

import Model.Product;
import Model.Student;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ConvertTest {
    public void test() {
        convert();
    }

    private void convert() {
        Integer intNum = Integer.valueOf("5");
        Integer intNum1 = Integer.parseInt("6");
        Double douNum = Double.valueOf("50");
        Double douNum1 = Double.parseDouble("50.56");
        Integer intNum2 = douNum1.intValue();
        Double douBum2 = intNum1.doubleValue();

        String intNumStr = intNum.toString();
        String douNum1Str = String.valueOf(douNum);

        Long numLong1 = 2L;

        //日期
        DateTimeFormatter dateTimeFormatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LocalDateTime localDateTime1 = LocalDateTime.parse("2018-12-31 13:12:00.000", dateTimeFormatter1);
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = simpleDateFormat.parse("2018-11-26 13:25:22");
            Integer m = 0;
        } catch (Exception ex) {

        }


        Student student = new Student();
        Object objectStudent = (Object) student;

        //true，java类型转换前最好先判断下，尤其是反射时候
        if (objectStudent instanceof Student) {
            Student studentC =(Student) objectStudent;
        }
        //false
        if (objectStudent instanceof Product) {
            Integer mm = 0;
        }
        //java中如果不能类型转换回抛异常，C#没有异常返回null
        Product pro = (Product) objectStudent;


        Integer m = 0;

    }

}
