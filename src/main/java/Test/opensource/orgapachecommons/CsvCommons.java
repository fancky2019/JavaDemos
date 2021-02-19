package Test.opensource.orgapachecommons;

import Model.Student;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvCommons {
    public void test() {
        String fileFullPath = "C:\\Users\\admin\\Desktop\\we\\test.csv";
        write(fileFullPath);
        read(fileFullPath);
    }

    private void write(String fileFullPath) {
        try {

            File file = new File(fileFullPath);
            //获取父目录
            String filePath = file.getParent();
            File fileDic = new File(filePath);
            //判断父目录是否存在,否则创建
            if (!fileDic.exists()) {
                fileDic.mkdirs();
            }

            //类似C#的匿名
            String[] csvHeaders = {"Id","姓名", "年龄"};
            Student student = new Student();
            student.setId(1);
            student.setName("fancky");
            student.setAge(27);

            List<Student> studentList = new ArrayList<>();
            studentList.add(student);
            student = new Student();
            student.setId(2);
            student.setName("fancky2");
            student.setAge(27);
            studentList.add(student);
            Appendable printWriter = new PrintWriter(fileFullPath, "GBK");//指定GBK,解决Microsoft不兼容

            CSVPrinter csvPrinter = CSVFormat.EXCEL.withHeader(csvHeaders).print(printWriter);
            studentList.forEach(p ->
            {
                try {
                    csvPrinter.printRecord(p.toObjects());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            csvPrinter.flush();
            csvPrinter.close();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }

    }

    private void read(String fileFullPath) {
        try {
            //中文乱码
            //   Reader reader = new FileReader(fileFullPath);

            DataInputStream in = new DataInputStream(new FileInputStream(new File(fileFullPath)));
            BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(in,"GBK"));//这里如果csv文件编码格式是utf-8,改成utf-8即可
            Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(bufferedReader);
            for (CSVRecord csvRecord : records) {// 第一行不会被打印出来
                System.out.println(csvRecord.get("Id")+","+csvRecord.get("姓名") + "," +  csvRecord.get("年龄") );
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
