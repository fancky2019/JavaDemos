package Test.opensource.sqlite;

import utility.Configs;

import java.sql.Connection;
import java.sql.DriverManager;

public class SQLiteTest {

    public void test() {
        //路径：D:\fancky\Git\Java\JavaDemos
        String relativelyPath = System.getProperty("user.dir");
        //路径：/D:/fancky/Git/Java/JavaDemos/target/classes/
        String path = this.getClass().getClassLoader().getResource("").getPath();
        int m = 0;
    }


    private Connection getConnection() {
        try {

            Class.forName("org.sqlite.JDBC");
            String dbPath = "jdbc:sqlite:test.db";
            Connection con = DriverManager.getConnection(Configs.Instance.getDburl());

            return con;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
