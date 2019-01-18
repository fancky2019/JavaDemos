package common;

import Test.test2018.PropertiesTest;

import java.io.InputStream;
import java.util.Properties;

public class Configs {
    private String driverName;
    private String dburl;
    private String user;
    private String password;

    private String redisIP;
    private String redisPort;
    private String redisPassword;
    private  Integer dbIndex;
    private  Integer timeout;

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

    public Configs() {
        try {
            loadProperties();
        }
        catch (Exception ex)
        {

        }
    }

    private void loadProperties() throws Exception {
        Properties properties = new Properties();
        InputStream inputStream = PropertiesTest.class.getClassLoader().getResourceAsStream("resources/jdbc.properties");
        //  InputStream inputStream = new FileInputStream(new File("resources/jdbc.properties"));
        properties.load(inputStream);

        driverName = properties.getProperty("driverName");
        dburl = properties.getProperty("dburl");
        user = properties.getProperty("user");
        password = properties.getProperty("password");

        redisIP = properties.getProperty("redisIP");
        redisPort = properties.getProperty("redisPort");
        redisPassword = properties.getProperty("redisPassword");
        dbIndex= Integer.parseInt( properties.getProperty("dbIndex"));
        timeout= Integer.parseInt( properties.getProperty("timeout"));
        inputStream.close();
    }

    public String getRedisIP() {
        return redisIP;
    }

    public  void  test()
    {
        String redis=getRedisIP();
        String dr=getDriverName();
        Integer m=0;
    }

}
