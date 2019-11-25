package Test.test2018;

import Model.Product;
import Model.Student;
import utility.DurationExtension;
import utility.TimeSpan;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public class ConvertTest {
    public void test() {
        convert();
    }

    private void convert() {
        //包装类：
        //转换过程：string-->int-->包装类
        Integer intNum = Integer.valueOf("5");
        Integer intNum1 = Integer.parseInt("6");

        Double douNum = Double.valueOf("50");
        Double douNum1 = Double.parseDouble("50.56");

        Integer intNum2 = douNum1.intValue();
        Double douBum2 = intNum1.doubleValue();

        String intNumStr = intNum.toString();
        String douNum1Str = String.valueOf(douNum);


        char ch = intNumStr.charAt(0);
        char[] chArray = intNumStr.toCharArray();

        String chStr = String.valueOf(ch);
        String chStr1 = Character.toString(ch);


        Long numLong1 = 2L;

        //日期
        DateTimeFormatter dateTimeFormatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LocalDateTime localDateTime1 = LocalDateTime.parse("2018-12-31 13:12:00.000", dateTimeFormatter1);
        String localDateTimeStr = localDateTime1.format(dateTimeFormatter1);

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


            //格式化字符串
            Date date = simpleDateFormat.parse("2018-11-26 13:25:22");
            String dateStr = simpleDateFormat.format(date);

            //获取时间戳(单位s): Gets the number of seconds from the Java epoch of 1970-01-01T00:00:00Z.
            // Instant
            long timeStamp = date.toInstant().getEpochSecond();
            //参数单位毫秒
            Date date1 = new Date(timeStamp * 1000L);
            String dateStr1 = simpleDateFormat.format(date);
            //转换成秒


            //region  Period  精度到日期  yyyy-DD-dd
            LocalDate startDate = LocalDate.of(2015, 2, 20);
            LocalDate endDate = LocalDate.of(2017, 1, 15);
            Period period = Period.between(startDate, endDate);


            LocalDateTime startLocalDateTime = LocalDateTime.parse("2019-11-23 13:12:30.050", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
            LocalDateTime endLocalDateTime = LocalDateTime.now();
            Duration durationLocalDateTime = Duration.between(startLocalDateTime, endLocalDateTime);
            System.out.println(MessageFormat.format("{0} {1}:{2}:{3}", durationLocalDateTime.toDays(),
                    durationLocalDateTime.toHours(),
                    durationLocalDateTime.toMinutes(),
                    durationLocalDateTime.toMillis()));

            DurationExtension durationExtension=new DurationExtension(durationLocalDateTime) ;
            int day=durationExtension.getDay();
            System.out.println(durationExtension.toString());

            TimeSpan timeSpan = TimeSpan.Minus(startLocalDateTime, endLocalDateTime);
            int day1=timeSpan.getDay();
            System.out.println(timeSpan.toString());
            Integer m = 0;
        } catch (Exception ex) {

        }


        Student student = new Student();
        Object objectStudent = (Object) student;

        //true，java类型转换前最好先判断下，尤其是反射时候
        if (objectStudent instanceof Student) {
            Student studentC = (Student) objectStudent;
        }
        //false
        if (objectStudent instanceof Product) {
            Integer mm = 0;
        }
        //java中如果不能类型转换回抛异常，C#没有异常返回null
        Product pro = (Product) objectStudent;


        Integer m = 0;

    }

    private void localDateTimeTest() {
        LocalDateTime localDateTime = LocalDateTime.now();

        //转换string
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        String dateStr = localDateTime.format(dateTimeFormatter);

//

        DateTimeFormatter dateTimeFormatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        //string 转换
        LocalDateTime localDateTime4 = LocalDateTime.parse("2018-12-31 13:12:00.000", dateTimeFormatter1);
        //小的时间在前 Duration是秒，纳秒
        Duration duration = Duration.between(localDateTime4, localDateTime);

        Long day = duration.toDays();
        Long days = duration.getSeconds() / (24l * 60l * 60l);
        Integer idays = days.intValue();


        //2019-09-20T09:46:20.203
        LocalDateTime localDateTimeNow = LocalDateTime.now();

        //转换成带时区的时间
        //2019-09-20T09:46:20.203+08:00[Asia/Shanghai]
        ZonedDateTime zonedDateTime = localDateTimeNow.atZone(ZoneId.systemDefault());
        //格式化
        //2019-09-20 09:53:30.997
        String nowStr = zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));

        //日期运算，年月日部分运算。
        LocalDateTime addDateTime = localDateTimeNow.plusDays(1);
        LocalDateTime minusDateTime = localDateTimeNow.minusHours(3);

        // 东八区时
        TimeZone timeZone = TimeZone.getTimeZone("GMT-8");


        //和时间戳相互转换
        //  Convert LocalDateTime to milliseconds since January 1, 1970, 00:00:00 GMT
        Long milliseconds = LocalDateTime.now().atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
        // epoch milliseconds to LocalDateTime
        LocalDateTime newNow = LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), ZoneOffset.UTC);

        Integer m = 0;
    }

}
