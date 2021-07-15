package Test.test2018;

import Model.Student;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * 时间戳 1970-01-01 到现在的秒数
 */
public class CalendarTest {

    public void test() {
        try {
            operation();
            formatter();
        } catch (Exception ex) {
            int m = 0;
        }

    }

    private void startEndDay() {
        LocalDateTime today_start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime today_end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        LocalDateTime yesterday = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MAX);
    }

    private void operation() throws ParseException {
        //获取秒数
        Long second = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        //获取毫秒数
        Long milliSecond = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();

        Date date = new Date();
        //格式化输出
        //"yyyy-MM-dd "
        //"yyyyMM"
        //.net 是yyyy-MM-dd HH:mm:ss.fff
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        String dateStr = simpleDateFormat.format(date);

        //转换
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = format1.parse("2018-11-26");

        DateTimeFormatter dateTimeFormatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        //string 转换 LocalDateTime
        LocalDateTime localDateTime4 = LocalDateTime.parse("2018-12-31 13:12:00.000", dateTimeFormatter1);

        DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //string 转换
//        LocalDateTime localDateTime22 = LocalDateTime.parse("2018-12-31", dateTimeFormatter2);
        LocalDate localDate22 = LocalDate.parse("2018-12-31", dateTimeFormatter2);
        Integer year1111 = localDate22.getYear();
        Integer month1111 = localDate22.getMonth().getValue();
        Integer date111 = localDate22.getDayOfMonth();


        //装换成string
        String localDateTimeStr = LocalDateTime.now().format(dateTimeFormatter1);
        //比较
        Integer result = date.compareTo(date1);
        Integer result2 = date.compareTo(date);
        Integer result1 = date.compareTo(simpleDateFormat.parse(dateStr));
        if (date.after(date1)) {
            Integer m = 0;
        }

        //大小比较
        LocalDateTime localDateTime = LocalDateTime.now();
        //小于
        boolean resultCompare = localDateTime.isBefore(localDateTime4);
        //大于
        boolean resultCompare1 = localDateTime.isAfter(localDateTime4);


        long millis = System.currentTimeMillis();

        //采用时间戳比较
        long localDateTimeMillis = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long localDateTimeMillis1 = localDateTime.atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
        if (System.currentTimeMillis() >= localDateTimeMillis) {

        }

        //转换成带时区的时间
        //2019-09-20T09:46:20.203+08:00[Asia/Shanghai]
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime atZone = localDateTime.atZone(ZoneId.of("Asia/Shanghai"));

        //ZonedDateTime 转LocalDateTime
        ZonedDateTime zonedDateTime1 = ZonedDateTime.now();
        zonedDateTime1.toLocalDateTime();

        //获取年月日
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        //获取年
        int year = calendar.get(Calendar.YEAR);
        //获取月份，0表示1月份
        int month = calendar.get(Calendar.MONTH) + 1;
        //获取当前天数
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //获取本月最小天数
        int first = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        //获取本月最大天数
        int last = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        //获取当前小时
        int time = calendar.get(Calendar.HOUR_OF_DAY);
        //获取当前分钟
        int min = calendar.get(Calendar.MINUTE);
        //获取当前秒
        int sec = calendar.get(Calendar.SECOND);


        //加减
        //天加1
        calendar.add(Calendar.DATE, 1);
        dateStr = simpleDateFormat.format(calendar.getTime());


        Date d2 = simpleDateFormat.parse("2018-11-25 11:32:24 123");
        Date d1 = date;
        long diff = d1.getTime() - d2.getTime();

        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        String timeSpanStr = String.format("%d：天，%d：小时，%d：分钟，%d：秒", diffDays, diffHours, diffMinutes, diffSeconds);
        System.out.println("" + diffDays + "天" + diffHours + "小时" + diffMinutes + "分");
        System.out.println(timeSpanStr);


        //region  Period  精度到日期  yyyy-DD-dd
        LocalDate startDate = LocalDate.of(2015, 2, 20);
        LocalDate endDate = LocalDate.of(2017, 1, 15);
        Period period = Period.between(startDate, endDate);

        //endregion


        //region  Duration  精度到时间  HH:mm:ss.sss
        Instant start = Instant.parse("2017-10-03T10:15:30.00Z");
        Instant end = Instant.parse("2017-10-03T10:16:30.00Z");
        Duration duration = Duration.between(start, end);
        //endregion


        //时间戳：默认是UTC时间
        Instant instant = Instant.now().plusMillis(TimeUnit.HOURS.toMillis(8));
        //获取时间戳
        instant.getEpochSecond();


        LocalDateTime startLocalDateTime = LocalDateTime.parse("2019-11-23 13:12:00.000", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        LocalDateTime endLocalDateTime = LocalDateTime.now();
        Duration durationLocalDateTime = Duration.between(startLocalDateTime, endLocalDateTime);
        System.out.println(MessageFormat.format("{0} {1}:{2}:{3}", durationLocalDateTime.toDays(),
                durationLocalDateTime.toHours(),
                durationLocalDateTime.toMinutes(),
                durationLocalDateTime.toMillis()));


    }

    private void formatter() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LocalDateTime localDateTime = LocalDateTime.now();
        String str1 = localDateTime.format(formatter);
        String str2 = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String str3 = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        String str4 = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH"));
        String str5 = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String str6 = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        String str7 = localDateTime.format(DateTimeFormatter.ofPattern("yyyy"));
        Integer m = 0;
    }

    private void convert() {
        //region date-->LocalDateTime
        Instant instant = new Date().toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        LocalDateTime dispatchTime = LocalDateTime.from(localDateTime);
        //endregion

    }


}
