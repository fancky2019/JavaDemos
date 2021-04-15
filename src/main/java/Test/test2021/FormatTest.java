package Test.test2021;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class FormatTest {
    public void test() {
        foramt();
    }

    private void foramt() {
        try {


            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse("2021-03-2");

            DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate22 = LocalDate.parse("2018-12-31", dateTimeFormatter2);

            DateTimeFormatter dateTimeFormatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
            //string 转换
            LocalDateTime localDateTime4 = LocalDateTime.parse("2018-12-31 13:12:00.000", dateTimeFormatter1);

            //获取月有多少天
            Integer dayss = getDaysByYearMonth(2021, 2);
            //04
            String strNum = String.format("%02d", 4);

            Integer year = LocalDateTime.now().getYear();
            Integer month = LocalDateTime.now().getMonth().getValue();
            //202104
            String yearMonthStr = MessageFormat.format("{0}{1}", year.toString(), String.format("%02d", month));
            //202104
            String yearMonthStr1 = MessageFormat.format("{0}{1}", String.valueOf(LocalDateTime.now().getYear()), String.format("%02d", month));
            int m33 = 0;
        } catch (Exception ex) {

        }
    }


    /**
     * 根据年 月 获取对应的月份 天数
     */
    public int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }
}
