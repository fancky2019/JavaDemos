package Test;

import Model.Student;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class CalendarTest {

    public void test() {
        try {
            operation();
        } catch (Exception ex) {

        }

    }

    private void operation() throws ParseException {

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

        //比较
        Integer result = date.compareTo(date1);
        Integer result2 = date.compareTo(date);
        Integer result1 = date.compareTo(simpleDateFormat.parse(dateStr));
        if (date.after(date1)) {
            Integer m = 0;
        }

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
    }
}
