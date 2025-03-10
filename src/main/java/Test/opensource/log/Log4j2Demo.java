package Test.opensource.log;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 10W+ 每秒
 */
@Slf4j
public class Log4j2Demo {
//    private static final Logger LOGGER = LogManager.getLogger();
    private static final Logger LOGGER = LogManager.getLogger(Log4j2Demo.class);


    public void test() {
        LocalDateTime localDateTime = LocalDateTime.now();

        //转换string
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        String dateStr = localDateTime.format(dateTimeFormatter);

//        for (int i = 0; i < 1000000; i++) {
//
////            LOGGER.debug(MessageFormat.format("{0} This is debug message", dateStr));
//            LOGGER.info(MessageFormat.format("{0} This is info message{1}", dateStr,i));
////            LOGGER.warn(MessageFormat.format("{0} This is warn message", dateStr));
////            LOGGER.error(MessageFormat.format("{0} This is error message", dateStr));
//        }

        try {
//            LOGGER.debug(MessageFormat.format("{0} This is debug message", dateStr));
//            LOGGER.info(MessageFormat.format("{0} This is info message", dateStr));
//            LOGGER.warn(MessageFormat.format("{0} This is warn message", dateStr));
//            LOGGER.error(MessageFormat.format("{0} This is error message", dateStr));
//            LOGGER.info("logTest {} {}", 1, 2);
//            LOGGER.error("{} ddddd {}", dateStr,1);


            log.debug(MessageFormat.format("{0} This is debug message", dateStr));
            log.info(MessageFormat.format("{0} This is info message", dateStr));
            log.warn(MessageFormat.format("{0} This is warn message", dateStr));
            log.error(MessageFormat.format("{0} This is error message", dateStr));
            log.info("logTest {} {}", 1, 2);
            log.error("{} ddddd {}", dateStr,1);
            int m=0;
        } catch (Exception e) {
//            LOGGER.error("", e);//用此重载，打印异常
        }

    }
}
