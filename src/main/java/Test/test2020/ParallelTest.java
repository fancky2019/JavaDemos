package Test.test2020;

import Model.Student;
import com.google.common.base.Stopwatch;
//import org.omg.CORBA.PRIVATE_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utility.TXTFile;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class ParallelTest {
    public void test() {
//        CompletableFuture.runAsync(()->
//        {
//            parallelFun();
//        });
//
//        CompletableFuture.runAsync(()->
//        {
//            sequentialFun();
//        });
        readFileTest();
    }

    /*
    parallelFun:5,036 milliSeconds
     */
    private void parallelFun() {
        List<Student> studentList = new ArrayList<>();//        new LinkedList<>()

        for (int i = 0; i < 100; i++) {
            Student student = new Student();
            student.setAge(i);
            student.setName(String.valueOf(i));
            studentList.add(student);
        }
        Stopwatch stopwatch = Stopwatch.createStarted();
        //ArrayList 的并行性能优于LinkedList
        studentList.stream().parallel().forEach(p ->
        {
            try {
                Thread.sleep(100);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        });
        //并行转顺序
        //  studentList.stream().parallel().sequential()
        stopwatch.stop();

        long milliSeconds = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        System.out.println(MessageFormat.format("parallelFun:{0} milliSeconds", milliSeconds));
    }

    /*
    sequentialFun:10,053 milliSeconds
     */
    private void sequentialFun() {
        List<Student> studentList = new ArrayList<>();//        new LinkedList<>()

        for (int i = 0; i < 100; i++) {
            Student student = new Student();
            student.setAge(i);
            student.setName(String.valueOf(i));
            studentList.add(student);
        }
        Stopwatch stopwatch = Stopwatch.createStarted();
        //ArrayList 的并行性能优于LinkedList
        studentList.forEach(p ->
        {
            try {
                Thread.sleep(100);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        });
        stopwatch.stop();

        long milliSeconds = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        System.out.println(MessageFormat.format("sequentialFun:{0} milliSeconds", milliSeconds));
    }


    Logger logger = LoggerFactory.getLogger(ParallelTest.class);

    private void readFileTest() {
        String fileName = "C:\\Users\\Administrator\\Desktop\\OCG-log\\ClientIn_20200113.log";
        Stopwatch stopwatch = Stopwatch.createStarted();
        //455ms
//        ConcurrentLinkedQueue<ClientInLog> datas = readDataParallel(fileName);
        //   297ms  19749
        List<ClientInLog> datas = readData(fileName);
        stopwatch.stop();
        //391
        long mills = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        int m = 0;
    }

    private ConcurrentLinkedQueue<ClientInLog> readDataParallel(String fileName) {
        // String fileName = "C:\\Users\\Administrator\\Desktop\\ClientIn_20200115.log";
        ConcurrentLinkedQueue<ClientInLog> orderContent = new ConcurrentLinkedQueue<>();
//        List<ClientInLog> orderContent = new ArrayList<>();
        try {
            List<String> allContent = TXTFile.readTXT(fileName);


//            for (String p : allContent) {

            allContent.stream().parallel().forEach(p -> {
                try {
//                    int nineteenIndex = p.indexOf("len=");
//                    if (nineteenIndex == -1) {
//                        continue;
//                    }
//                    int rightParenthesesIndex = p.indexOf(")");
//                    String lenStr = p.substring(nineteenIndex + 4, rightParenthesesIndex);
//                    Integer len = Integer.parseInt(lenStr);
//                    //不是下单指令，可能是心跳
//                    if (len < 100) {
//                        continue;
//                    }
//                    String logTimeStr = p.substring(0, 21);
//                    LocalDateTime logTime = LocalDateTime.parse(logTimeStr, DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss:SSS"));
//                    int rightParenthesisIndex = p.indexOf(")");
//                    String content = p.substring(rightParenthesisIndex, p.length() - 1);


                    String logContent = p.substring(62);
                    int logContentLength = logContent.length();
                    List<String> listString = new ArrayList<>();
                    //不是下单指令，可能是心跳、登录指令
                    if (logContentLength < 100) {
                        //continue;
                    } else {

                        int firstIndex = logContent.indexOf('}');
                        logContentLength = logContent.length();

                        if (firstIndex == logContentLength - 1) {
                            listString.add(logContent);
                        } else {
                            String currentContent = "";
                            while (firstIndex != logContentLength - 1) {
                                currentContent = logContent.substring(0, firstIndex + 1);
                                listString.add(currentContent);

                                logContent = logContent.substring(firstIndex + 1);
                                logContentLength = logContent.length();
                                firstIndex = logContent.indexOf('}');
                            }
                            listString.add(logContent);
                        }

                        String logTimeStr = p.substring(0, 21);
                        LocalDateTime logTime = LocalDateTime.parse(logTimeStr, DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss:SSS"));

                        listString.forEach(str ->
                        {
                            ClientInLog clientInLog = new ClientInLog();

                            clientInLog.setLogTime(logTime)
                                    .setContent(str);
                            orderContent.add(clientInLog);
                        });

//                        int m = 0;
                    }
                } catch (Exception ex) {
                    logger.error(p);
                    logger.error(ex.getMessage());
                }
            });
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }

        return orderContent;
    }


    private List<ClientInLog> readData(String fileName) {
        // String fileName = "C:\\Users\\Administrator\\Desktop\\ClientIn_20200115.log";

        List<ClientInLog> orderContent = new ArrayList<>();
        try {
            List<String> allContent = TXTFile.readTXT(fileName);
            for (String p : allContent) {


                try {
//                    int nineteenIndex = p.indexOf("len=");
//                    if (nineteenIndex == -1) {
//                        continue;
//                    }
//                    int rightParenthesesIndex = p.indexOf(")");
//                    String lenStr = p.substring(nineteenIndex + 4, rightParenthesesIndex);
//                    Integer len = Integer.parseInt(lenStr);
//                    //不是下单指令，可能是心跳
//                    if (len < 100) {
//                        continue;
//                    }
//                    String logTimeStr = p.substring(0, 21);
//                    LocalDateTime logTime = LocalDateTime.parse(logTimeStr, DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss:SSS"));
//                    int rightParenthesisIndex = p.indexOf(")");
//                    String content = p.substring(rightParenthesisIndex, p.length() - 1);


                    String logContent = p.substring(62);
                    int logContentLength = logContent.length();
                    List<String> listString = new ArrayList<>();
                    //不是下单指令，可能是心跳、登录指令
                    if (logContentLength < 100) {
                        //continue;
                    } else {

                        int firstIndex = logContent.indexOf('}');
                        logContentLength = logContent.length();

                        if (firstIndex == logContentLength - 1) {
                            listString.add(logContent);
                        } else {
                            String currentContent = "";
                            while (firstIndex != logContentLength - 1) {
                                currentContent = logContent.substring(0, firstIndex + 1);
                                listString.add(currentContent);

                                logContent = logContent.substring(firstIndex + 1);
                                logContentLength = logContent.length();
                                firstIndex = logContent.indexOf('}');
                            }
                            listString.add(logContent);
                        }

                        String logTimeStr = p.substring(0, 21);
                        LocalDateTime logTime = LocalDateTime.parse(logTimeStr, DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss:SSS"));

                        listString.forEach(str ->
                        {
                            ClientInLog clientInLog = new ClientInLog();

                            clientInLog.setLogTime(logTime)
                                    .setContent(str);
                            orderContent.add(clientInLog);
                        });

//                        int m = 0;
                    }
                } catch (Exception ex) {
                    logger.error(p);
                    logger.error(ex.getMessage());
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }

        return orderContent;
    }
}

class ClientInLog {
    private LocalDateTime logTime;
    private String content;

    private String sysNo;
    private String customerNo;

    public LocalDateTime getLogTime() {
        return logTime;
    }

    public String getLogTimeSecondStr() {
        return logTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String getLogTimeMinuteStr() {
        return logTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public String getLogTimeHourStr() {
        return logTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH"));
    }

    public ClientInLog setLogTime(LocalDateTime logTime) {
        this.logTime = logTime;
        return this;
    }

    public String getContent() {
        return content;
    }

    public ClientInLog setContent(String content) {
        this.content = content;
        return this;
    }

    public String getSysNo() {
        return sysNo;
    }

    public ClientInLog setSysNo(String sysNo) {
        this.sysNo = sysNo;
        return this;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public ClientInLog setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
        return this;
    }
}
