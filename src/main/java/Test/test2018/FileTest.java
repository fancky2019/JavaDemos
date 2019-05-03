package Test.test2018;


import java.io.*;

public class FileTest {
    //C#const是隐士静态的
    public static final int A = 5;

    public void createFile() throws IOException {
        String fileName = "log/runoob.txt";
        File file = new File(fileName);
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdir();
        }
        file.createNewFile();

    }

    public void writeTxt(String fileName, String content) throws IOException {
        FileWriter fileWriter = new FileWriter(fileName, true);
        BufferedWriter out = new BufferedWriter(fileWriter);
        out.write(content);
        out.flush(); // 把缓存区内容压入文件
        out.close();
    }

    public void writeText(String fileName, String content) throws IOException {
       // File file = new File(fileName);
        OutputStreamWriter outputStreamWriter=new OutputStreamWriter(new FileOutputStream(fileName));
        BufferedWriter out = new BufferedWriter(outputStreamWriter);
        out.write(content);
        out.flush(); // 把缓存区内容压入文件
        out.close();
    }

    /**
     * 返回读取的文本
     *
     * @param fileNmae
     * @return
     * @throws IOException
     */
    public String readTXT(String fileNmae) throws IOException {
      //  File file = new File(fileNmae);
//        InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
        InputStreamReader reader = new InputStreamReader(new FileInputStream(fileNmae));
        //FileReader
        BufferedReader br = new BufferedReader(reader);
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            content.append(line);
        }
        br.close();
        reader.close();
        return content.toString();
    }

    public  String readText(String fileName) throws Exception
    {
        FileReader fileReader=new FileReader(fileName);
        BufferedReader br = new BufferedReader(fileReader);
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            content.append(line);
        }
        br.close();
        fileReader.close();
        return content.toString();
    }

    public void test() {
        try {
            //D:\JavaProject\Demos
            String relativelyPath = System.getProperty("user.dir");
            //目录：D:\JavaProject\Demo
            //"log\txt.txt",路径下没有log文件夹，要先创建Log文件夹
            //../log/txt.txt:String relativelyPath =D:\JavaProject\Demo的上一级路径下的log/txt.txt
            //即D:\\JavaProject\\log\\txt.txt
            String fileName = "../log/txt.txt";
            String fileName1 = "../log/txt1.txt";
            StringBuilder sb = new StringBuilder("test");
            for (Integer i = 0; i < 10; i++) {
                sb.append("\r\n");
                sb.append(String.format("%s%s", "test", i.toString()));
            }
            String writeContent = sb.toString();
            writeTxt(fileName, writeContent);

            String readContent = readTXT(fileName);

            writeText(fileName1, writeContent);

            String rc=readTXT(fileName1);
            Integer m = 0;

        } catch (Exception ex) {
            String msg = ex.getMessage();
        }


    }
}
