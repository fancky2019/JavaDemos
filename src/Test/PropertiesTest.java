package Test;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesTest {
    //    public void test() throws Exception {
////将配置文件放到Resources文件夹中，在代码中添加配置文件的虚拟路径。选中文件，右键-Copy Reference，就可以获取到文件的虚拟路径。
////        driver=com.microsoft.sqlserver.jdbc.SQLServerDriver
////        url=jdbc:sqlserver://localhost:1433;DatabaseName=Demo
////        user=sa
////        password=123456
//        Properties properties = new Properties();
//       // InputStream inputStream = new FileInputStream(new File(" resources/jdbc.properties"));
//        InputStream inputStream =   PropertiesTest.class.getClassLoader().getResourceAsStream("jdbc");
//        properties.load(inputStream);
//        String url = properties.getProperty("url");
//        inputStream.close();
//    }
    public void test1() throws Exception {
        Properties properties = new Properties();
        InputStream inputStream = PropertiesTest.class.getClassLoader().getResourceAsStream("resources/jdbc.properties");
        //    InputStream inputStream = new FileInputStream(new File("Test/jdbc.properties"));
        properties.load(inputStream);
        String dburl = properties.getProperty("dburl");

        inputStream.close();
    }

}
