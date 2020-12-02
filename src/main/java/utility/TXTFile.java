package utility;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TXTFile {
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
        fileWriter.close();
    }

    /*
    生成文件目录在根路径。
     */
    public static void writeText(String fileName, String content) throws IOException {
        // File file = new File(fileName);
        //Charset.forName("UTF-8");
        FileOutputStream fileOutputStream = new FileOutputStream(fileName, true);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
        BufferedWriter out = new BufferedWriter(outputStreamWriter);
        //换行
        out.write(content + "\r\n");
        out.flush(); // 把缓存区内容压入文件

        //注意关闭的先后顺序，先打开的后关闭，后打开的先关闭
        out.close();
        outputStreamWriter.close();
        fileOutputStream.close();
    }

    /**
     * 返回读取的文本
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public static List<String> readTXT(String fileName) throws IOException {
        //  File file = new File(fileName);
//        InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
        InputStreamReader reader = new InputStreamReader(new FileInputStream(fileName));
        //FileReader
        BufferedReader br = new BufferedReader(reader);
        List<String> content = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            content.add(line);
        }
        br.close();
        reader.close();
        return content;
    }

    public String readText(String fileName) throws Exception {
        FileReader fileReader = new FileReader(fileName);
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


}
