package utility;

import java.io.InputStream;
import java.util.Properties;

/*
properties的文件路径要看生成后的文件路径，和Main类同级别不加文件夹，直接文件名加后缀（jdbc.properties）
 */
public class Configs {
    private String driverName;
    private String dburl;
    private String user;
    private String password;

    private String mysqlDriverName;
    private String mysqldburl;
    private String mysqlUser;
    private String mysqlPassword;

    private String redisIP;
    private String redisPort;
    private String redisPassword;
    private Integer dbIndex;
    private Integer timeout;

    public Integer getTimeout() {
        return timeout;
    }

    public String getRedisPort() {
        return redisPort;
    }

    public Integer getDbIndex() {
        return dbIndex;
    }

    public String getRedisPassword() {
        return redisPassword;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getDburl() {
        return dburl;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getMysqlDriverName() {
        return mysqlDriverName;
    }

    public void setMysqlDriverName(String mysqlDriverName) {
        this.mysqlDriverName = mysqlDriverName;
    }

    public String getMysqldburl() {
        return mysqldburl;
    }

    public void setMysqldburl(String mysqldburl) {
        this.mysqldburl = mysqldburl;
    }

    public String getMysqlUser() {
        return mysqlUser;
    }

    public void setMysqlUser(String mysqlUser) {
        this.mysqlUser = mysqlUser;
    }

    public String getMysqlPassword() {
        return mysqlPassword;
    }

    public void setMysqlPassword(String mysqlPassword) {
        this.mysqlPassword = mysqlPassword;
    }


    private static Object lockObj = new Object();
    public static Configs Instance;

    static {
        if (Instance == null) {
            synchronized (lockObj) {
                if (Instance == null) {
                    Instance = new Configs();
                }
            }
        }
    }

    Configs() {
        try {
            loadProperties();
        } catch (Exception ex) {

        }
    }

    private void loadProperties() {
        try {
            Properties properties = new Properties();
            InputStream inputStream = Configs.class.getClassLoader().getResourceAsStream("jdbc.properties");


            //   InputStream inputStream = new FileInputStream(new File("resources/jdbc.properties"));
            properties.load(inputStream);

            driverName = properties.getProperty("driverName");
            dburl = properties.getProperty("dburl");
            user = properties.getProperty("user");
            password = properties.getProperty("password");


            mysqlDriverName = properties.getProperty("mysqlDriverName");
            mysqldburl = properties.getProperty("mysqldburl");
            mysqlUser = properties.getProperty("mysqlUser");
            mysqlPassword = properties.getProperty("mysqlPassword");

            redisIP = properties.getProperty("redisIP");
            redisPort = properties.getProperty("redisPort");
            redisPassword = properties.getProperty("redisPassword");
            dbIndex = Integer.parseInt(properties.getProperty("dbIndex"));
            timeout = Integer.parseInt(properties.getProperty("timeout"));
            inputStream.close();
        } catch (Exception ex) {
            String msg = ex.getMessage();
            Integer m = 0;
        }
    }

    public String getRedisIP() {
        return redisIP;
    }

    public void test() {
        String redis = getRedisIP();
        String dr = getDriverName();
        Integer m = 0;
    }

}
