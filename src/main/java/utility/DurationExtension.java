package utility;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.time.Duration;

public class DurationExtension {

    private Duration duration;
    private static final int MillisPerSecond = 1000;
    private static final int MillisPerMinute = 60000;
    private static final int MillisPerHour = 3600000;
    private static final int MillisPerDay = 86400000;

    private int day;
    private int hour;
    private int minute;
    private int second;
    private int milliSecond;
    private static int totalMilliSecond;


    private DecimalFormat decimalFormat2;
    private DecimalFormat decimalFormat3;

    public DurationExtension(Duration duration) {
        this.duration = duration;


        this.totalMilliSecond=(int) duration.toMillis();
        this.day = totalMilliSecond / MillisPerDay;
        this.hour = totalMilliSecond / MillisPerHour % 24;
        this.minute = totalMilliSecond / MillisPerMinute % 60;
        this.second = totalMilliSecond / MillisPerSecond % 60;
        this.milliSecond = totalMilliSecond % 1000;

        decimalFormat2 = new DecimalFormat("00");
        decimalFormat3 = new DecimalFormat("00");
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    public int getMilliSecond() {
        return milliSecond;
    }

    public int getTotalDay() {
        return totalMilliSecond / MillisPerDay;
    }

    public int getTotalHour() {
        return totalMilliSecond / MillisPerHour;
    }

    public int getTotalMinute() {
        return totalMilliSecond / MillisPerMinute;
    }

    public int getTotalSecond() {
        return totalMilliSecond / MillisPerSecond;
    }

    public int getTotalMilliSecond() {
        return totalMilliSecond;
    }

    @Override
    public String toString() {
        if (day > 0) {
            return MessageFormat.format("{0} {1}:{2}:{3}.{4}",
                    decimalFormat2.format(day),
                    decimalFormat2.format(hour),
                    decimalFormat2.format(minute),
                    decimalFormat2.format(second),
                    decimalFormat2.format(milliSecond));
        }
        if (hour > 0) {
            return MessageFormat.format("{0}:{1}:{2}.{3}",
                    decimalFormat2.format(hour),
                    decimalFormat2.format(minute),
                    decimalFormat2.format(second),
                    decimalFormat2.format(milliSecond));
        }
        if (minute > 0) {
            return MessageFormat.format("{0}m{1}s{2}ms",
                    decimalFormat2.format(minute),
                    decimalFormat2.format(second),
                    decimalFormat2.format(milliSecond));
        }
        if (second > 0) {
            return MessageFormat.format("{0}s{1}ms",
                    decimalFormat2.format(second),
                    decimalFormat2.format(milliSecond));
        }
        return MessageFormat.format("{0}ms", decimalFormat2.format(milliSecond));
    }
}
