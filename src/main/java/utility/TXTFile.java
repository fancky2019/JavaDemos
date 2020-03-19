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
    }

    public static void writeText(String fileName, String content) throws IOException {
        // File file = new File(fileName);
        //Charset.forName("UTF-8");
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(fileName, true), "UTF-8");
        BufferedWriter out = new BufferedWriter(outputStreamWriter);
        out.write(content);
        out.flush(); // 把缓存区内容压入文件
        out.close();
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
