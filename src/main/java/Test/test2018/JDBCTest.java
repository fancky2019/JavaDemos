package Test.test2018;

import Model.Product;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Stopwatch;
import io.netty.resolver.RoundRobinInetAddressResolver;
import org.apache.commons.lang3.RandomStringUtils;
import scala.collection.mutable.ReusableBuilder;
import utility.Configs;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.TimeUnit;

/*
github:https://github.com/Microsoft/mssql-jdbc
不用配置本地mssql-jdbc驱动，动过maven从仓库里下载。具体在pom.xml配置文件。
 */

/**
 * MSSQL 数据库操作
 * mysql:demo2019 MysqlTest
 */
public class JDBCTest {


    public void test() {
        try {
//              insert();
//            insertTakeTime();
            batchInsert();
            // delete();
            // update();
//            query();
            //  queryMultipleResult();
            //procedure();
//              procedureParamOutPut();
//               transaction();
//            concurrentTransaction();
//            pageData();
            Integer m = 0;
        } catch (Exception ex) {
            String msg = ex.getMessage();
            Integer m = 0;
        }
    }


    private Connection getConnection() {
        try {

//           Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//
//            String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=WMS;user=sa;password=123456;";
            Class.forName(Configs.Instance.getMysqlDriverName());
            Connection con = DriverManager.getConnection(Configs.Instance.getMysqldburl());
            //或者下面的创建连接重载
//            Connection con =     DriverManager.getConnection(Configs.Instance.getDburl(),Configs.Instance.getUser(),Configs.Instance.getPassword());


            //加载SQLJDBC驱动SQLServerDriver
            //    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            // Create a variable for the connection string.
            //      String connectionUrl = MessageFormat.format("{0};username={1};password={2}", Configs.Instance.getDburl(), Configs.Instance.getUser(), Configs.Instance.getPassword());

            //       Connection dbConn = DriverManager.getConnection(connectionUrl);
            // Class.forName(Configs.Instance.getDriverName());
            //  com.microsoft.sqlserver.jdbc.SQLServerDriver
            //加载SQLJDBC驱动SQLServerDriver
            //  Class.forName("com.microsoft.sqlserver.mssql-jdbc.SQLServerDriver");

//            Connection dbConn = DriverManager.getConnection(Configs.Instance.getDburl(),
//                    Configs.Instance.getUser(),
//                    Configs.Instance.getPassword());
            return con;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //region 查询
    private void query() throws Exception {
        String selectCommand = "select *  from  Product WHERE ProductName LIKE ?";
        Connection con = getConnection();
        PreparedStatement preparedStatement = con.prepareStatement(selectCommand);
        String productName = "fancky";
        preparedStatement.setString(1, "%" + productName + "%");
        // 执行数据库查询语句
        ResultSet rs = preparedStatement.executeQuery();
        //  String name = rs.getString("Name");
        // List<Product> list = resultSetToList(rs, Product.class);
        List<Product> list = convertToList(rs, Product.class);
        rs.close();
        preparedStatement.close();
        con.close();

        Integer m = 0;

    }

    /**
     * 获取泛型集合
     *
     * @param resultSet
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> resultSetToList(ResultSet resultSet, Class<T> clazz) {

        //创建一个 T 类型的数组
        List<T> list = new ArrayList<>();
        try {
            //通过反射获取对象的实例
            T t = clazz.getConstructor().newInstance();
            //获取resultSet 的列的信息
            ResultSetMetaData metaData = resultSet.getMetaData();
            //遍历resultSet
            while (resultSet.next()) {
                //遍历每一列
                for (int i = 0; i < metaData.getColumnCount(); i++) {
                    //获取列的名字
                    String fName = metaData.getColumnLabel(i + 1);
                    //因为列的名字和我们EMP中的属性名是一样的，所以通过列的名字获得其EMP中属性
                    Field field = clazz.getDeclaredField(fName.toLowerCase());
                    //因为属性是私有的，所有获得其对应的set 方法。set+属性名首字母大写+其他小写
                    String setName = "set" + fName.toUpperCase().substring(0, 1) + fName.substring(1).toLowerCase();
                    //因为属性的类型和set方法的参数类型一致，所以可以获得set方法
                    Method setMethod = clazz.getMethod(setName, field.getType());
                    //执行set方法，把resultSet中的值传入emp中，  再继续循环传值
                    setMethod.invoke(t, resultSet.getObject(fName));
                }
                //把赋值后的对象 加入到list集合中
                list.add(t);
            }

        } catch (Exception e) {
            String str = e.getMessage();
            e.printStackTrace();
        }
        // 返回list
        return list;
    }

    public static <T> List<T> convertToList(ResultSet rs, Class<T> t) throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResultSetMetaData md = (ResultSetMetaData) rs.getMetaData();
        int columnCount = md.getColumnCount();
        while (rs.next()) {
            Map<String, Object> rowData = new HashMap<String, Object>();
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnName(i), rs.getObject(i));
            }
            list.add(rowData);
        }
        String jsonString = JSON.toJSONString(list);
        List<T> resultList = JSON.parseArray(jsonString, t);
        return resultList;
    }

    private void queryMultipleResult() throws Exception {
        String insertCommand = "  insert into Product ( GUID, StockID," +
                "      BarCodeID, SkuID, ProductName," +
                "      ProductStyle, Price, CreateTime, " +
                "      Status, COUNT , ModifyTime " +
                "      )" +
                "      values (?,?,?,?,?,?,?,?,?,?,?)";
        String selectCommand = insertCommand + ";select *  from  Product ;select count(ID) [Count] from Product;";
        Connection con = getConnection();

        //设置事务
        con.setAutoCommit(false);
//        PreparedStatement preparedStatement = con.prepareStatement(selectCommand);
        PreparedStatement preparedStatement = con.prepareStatement(selectCommand);
        try {

            //设置参数
            preparedStatement.setString(1, UUID.randomUUID().toString());
            preparedStatement.setInt(2, 1);
            preparedStatement.setNull(3, Types.INTEGER);
            preparedStatement.setInt(4, 1);
            preparedStatement.setString(5, "pfanckyJDBC");
            preparedStatement.setNull(6, Types.NVARCHAR);
            preparedStatement.setBigDecimal(7, new BigDecimal(123));
            //                 setDate只能得到年月日
            preparedStatement.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
            Short status = 1;
            preparedStatement.setShort(9, status);
            preparedStatement.setInt(10, 12);
            preparedStatement.setTimestamp(11, new Timestamp(System.currentTimeMillis()));

            // 执行数据库查询语句
            boolean isResult = preparedStatement.execute();

            //获取第一个返回受影响的行数
            if (isResult) {
                Integer m = 0;

            } else {
                Integer count1 = preparedStatement.getUpdateCount();
                Integer num = count1;
            }

            //获取第二个个结果集
            boolean isResult2 = preparedStatement.getMoreResults();
            ResultSet resultSet2 = preparedStatement.getResultSet();
            List<Product> productList = convertToList(resultSet2, Product.class);

            //获取第三个个结果集
            boolean isResult3 = preparedStatement.getMoreResults();
            ResultSet resultSet3 = preparedStatement.getResultSet();
            resultSet3.next();
            Integer count = resultSet3.getInt("Count");
            // Integer m=Integer.valueOf("m");
            con.commit();
        } catch (Exception ex) {
            //已经插入了数据库
            //回滚了再从数据库中删除
            con.rollback();
            String msg = ex.getMessage();
            Integer m = 0;
        } finally {
            preparedStatement.close();
            con.close();
        }
    }
    //endregion

    //region更新
    private void update() throws Exception {
        String updateCommand = "update Product set ModifyTime=?,ProductName=? WHERE id=?";
        Connection con = getConnection();
        PreparedStatement preparedStatement = con.prepareStatement(updateCommand);
        preparedStatement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
        preparedStatement.setString(2, "testJDBC");
        preparedStatement.setInt(3, 59);
        Integer result = preparedStatement.executeUpdate();
        preparedStatement.close();
        con.close();
        Integer m = 0;
    }
//endregion

    //region  删除
    private void delete() throws Exception {
        String deleteCommand = "delete  from Product where ID=?";
        Connection con = getConnection();
        PreparedStatement preparedStatement = con.prepareStatement(deleteCommand);
        preparedStatement.setInt(1, 58);
        Integer result = preparedStatement.executeUpdate();
        preparedStatement.close();
        con.close();
        Integer m = 0;
    }
    //endregion

    //region插入
    private void insert() throws Exception {
        String insertCommand = "  insert into Product ( GUID, StockID," +
                "      BarCodeID, SkuID, ProductName," +
                "      ProductStyle, Price, CreateTime, " +
                "      Status, COUNT , ModifyTime " +
                "      )" +
                "      values (?,?,?,?,?,?,?,?,?,?,?)";
        Connection con = getConnection();
        //不返回自动生成的主键
        //  PreparedStatement preparedStatement = con.prepareStatement(insertCommand);
        //插入语句Statement.RETURN_GENERATED_KEYS返回生成的主键
        PreparedStatement preparedStatement = con.prepareStatement(insertCommand, Statement.RETURN_GENERATED_KEYS);
        //设置参数
        preparedStatement.setString(1, UUID.randomUUID().toString());
        preparedStatement.setInt(2, 1);
        preparedStatement.setNull(3, Types.INTEGER);
        preparedStatement.setInt(4, 1);
        preparedStatement.setString(5, "fanckyJDBC");
        preparedStatement.setNull(6, Types.NVARCHAR);
        preparedStatement.setBigDecimal(7, new BigDecimal(123));
        //                 setDate只能得到年月日
        preparedStatement.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
        Short status = 1;
        preparedStatement.setShort(9, status);
        preparedStatement.setInt(10, 12);
        preparedStatement.setTimestamp(11, new Timestamp(System.currentTimeMillis()));

        //执行命令并接受结果
        Integer result = preparedStatement.executeUpdate();
        ResultSet rs = preparedStatement.getGeneratedKeys();
        if (rs.next()) {
            Integer id = rs.getInt(1);
            Integer m = 0;
        }
        rs.close();
        preparedStatement.close();
        con.close();
        Integer m = 0;
    }

    private void insertTakeTime() throws Exception {
        String insertCommand = "  insert into Product ( GUID, StockID," +
                "      BarCodeID, SkuID, ProductName," +
                "      ProductStyle, Price, CreateTime, " +
                "      Status, COUNT , ModifyTime " +
                "      )" +
                "      values (?,?,?,?,?,?,?,?,?,?,?)";
        Connection con = getConnection();
        PreparedStatement preparedStatement = con.prepareStatement(insertCommand, Statement.RETURN_GENERATED_KEYS);
        Stopwatch stopwatch = Stopwatch.createStarted();


        //不返回自动生成的主键
        //  PreparedStatement preparedStatement = con.prepareStatement(insertCommand);
        //插入语句Statement.RETURN_GENERATED_KEYS返回生成的主键
        //设置参数
        preparedStatement.setString(1, UUID.randomUUID().toString());
        preparedStatement.setInt(2, 1);
        preparedStatement.setNull(3, Types.INTEGER);
        preparedStatement.setInt(4, 1);
        preparedStatement.setString(5, "fanckyJDBC");
        preparedStatement.setNull(6, Types.NVARCHAR);
        preparedStatement.setBigDecimal(7, new BigDecimal(123));
        //                 setDate只能得到年月日
        preparedStatement.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
        Short status = 1;
        preparedStatement.setShort(9, status);
        preparedStatement.setInt(10, 12);
        preparedStatement.setTimestamp(11, new Timestamp(System.currentTimeMillis()));
        //有时候0ms-->比C#块。
        //插入mysql慢。
        for (int i = 0; i < 50; i++) {
            //执行命令并接受结果
            Integer result = preparedStatement.executeUpdate();
            stopwatch.stop();
            System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
            stopwatch.reset();
            stopwatch.start();
        }
        ResultSet rs = preparedStatement.getGeneratedKeys();
        if (rs.next()) {
            Integer id = rs.getInt(1);
            Integer m = 0;
        }
        rs.close();
        preparedStatement.close();
        con.close();
        Integer m = 0;
    }
    //endregion

    //region 批量插入

    /**
     * 利用SQL Insert() values(),()
     *  jdbc 的batch  操作update 和insert 都是   preparedStatement.addBatch();  preparedStatement.executeBatch();
     *
     * @throws Exception
     */
    private void batchInsert() throws Exception {
        String insertCommand = "  insert into Product ( GUID, StockID," +
                "      BarCodeID, SkuID, ProductName," +
                "      ProductStyle, Price, CreateTime, " +
                "      Status, COUNT , ModifyTime " +
                "      )" +
                "      values (?,?,?,?,?,?,?,?,?,?,?)";
        Connection con = getConnection();
        con.setAutoCommit(false);
        //不返回自动生成的主键
        PreparedStatement preparedStatement = con.prepareStatement(insertCommand);
        // sqlserver批量插入获取自增key报异常,mysql不报错。
        //插入语句Statement.RETURN_GENERATED_KEYS返回生成的主键
//        PreparedStatement preparedStatement = con.prepareStatement(insertCommand, PreparedStatement.RETURN_GENERATED_KEYS);

        try {
            for (int i = 0; i < 200000; i++) {
                //设置参数
                //每个参数生成一个Parameter对象加入Parameter[]中。
                preparedStatement.setString(1, UUID.randomUUID().toString());
                preparedStatement.setInt(2, 1);
                preparedStatement.setNull(3, Types.INTEGER);
                preparedStatement.setInt(4, 1);

                //java17
//                int nameLength = new Random().nextInt(8, 15);
                //java8
                int nameLength = new Random().nextInt(15)+8;
                String nameStr = RandomStringUtils.random(nameLength, true, false);
//                preparedStatement.setString(5, MessageFormat.format("batchInsert{0}", i));
                preparedStatement.setString(5, nameStr);
//                if (i != 1) {
//                    preparedStatement.setString(5, MessageFormat.format("batchInsert{0}", i));
//                } else {
//                    //其中一条异常，其他没有回滚，和数据库里执行节本不同。数据库里当成一个事务。
//                    //针对其中有可能有异常数据，要开启事务。
//                    preparedStatement.setString(5, "dddddddddddddddddddddddddddddddddddddddddddddsdsffffffffffdddddddddddddddddddddddddddddddddddddddddddddsdsffffffffffdddddddddddddddddddddddddddddddddddddddddddddsdsffffffffffdddddddddddddddddddddddddddddddddddddddddddddsdsffffffffffdddddddddddddddddddddddddddddddddddddddddddddsdsffffffffff");
//                }
                preparedStatement.setNull(6, Types.NVARCHAR);
                preparedStatement.setBigDecimal(7, getRandomPrice());
                //                 setDate只能得到年月日
                preparedStatement.setTimestamp(8, new Timestamp(getRandomDate()));
                Short status = 1;
                preparedStatement.setShort(9, status);
                preparedStatement.setInt(10, 12);
                preparedStatement.setTimestamp(11, new Timestamp(System.currentTimeMillis()));
                //将每条sql的Parameter[]加入    ArrayList<Parameter[]> batchParamValues;中
                preparedStatement.addBatch();
            }
            //执行命令并接受结果
            int[] result = preparedStatement.executeBatch();
            con.commit();
            //sqlserver批量插入获取自增key报异常
//            ResultSet rs = preparedStatement.getGeneratedKeys();
//            if (rs.next()) {
//                Integer id = rs.getInt(1);
//                Integer m = 0;
//            }
//            rs.close();
        } catch (Exception ex) {
            String msg = ex.getMessage();
            System.out.println(msg);
        } finally {
            preparedStatement.close();
            con.close();
        }
        Integer m = 0;
    }

    //endregion


    private BigDecimal getRandomPrice() {
        Random randomPrice = new Random();
//        BigDecimal price = BigDecimal.valueOf(randomPrice.nextDouble(10000)).setScale(2, RoundingMode.HALF_UP);;

        BigDecimal price = BigDecimal.valueOf(randomPrice.nextDouble()).setScale(2, RoundingMode.HALF_UP);;


        return price;
    }

    private long getRandomDate() {
        Random random = new Random();//如果设置参数1每次都一样的值
        int year = random.nextInt(52);
        int month = random.nextInt(11)+1;
        int day = random.nextInt(27)+1;
        LocalDateTime startDate = LocalDateTime.of(1970, 1, day, 0, 0, 0);
        startDate = startDate.plusYears(year);
        startDate = startDate.plusMonths(month);
        startDate = startDate.plusDays(day);
        //atZone(ZoneOffset.UTC).toInstant().toEpochMilli()
//        imestamp.from(Instant.now());

        long ep = startDate.atZone((OffsetDateTime.now().getOffset())).toInstant().toEpochMilli();
        return ep;
    }

    //region 存储过程

    //region 存储过程返回集合
    private void procedure() throws Exception {
        Connection con = getConnection();
        CallableStatement callableStatement = con.prepareCall("{call GetProductProc(?,?)}");

        callableStatement.setBigDecimal(1, new BigDecimal(13));
        callableStatement.setString(2, "jdbc");
        //3.执行存储过程
        callableStatement.execute();
        ResultSet rs = callableStatement.getResultSet();
        List<Product> list = convertToList(rs, Product.class);
        Integer m = 0;
    }
    //endregion

    //region 存储过程输出参数
    private void procedureParamOutPut() throws Exception {
        Connection con = getConnection();
        CallableStatement callableStatement = con.prepareCall("{call UpdateProductOutParamProc(?,?,?)}");
        //注意参数没有@符号。
        callableStatement.setString("productName", "jdbc");
        callableStatement.setInt("id", 49);
        callableStatement.registerOutParameter("result", Types.INTEGER);
        //3.执行存储过程
        callableStatement.execute();
        Integer success = callableStatement.getInt("result");

        Integer m = 0;
    }
    //endregion

    //endregion

    //region 事务
    private void transaction() throws Exception {
        Connection con = getConnection();
        try {
            String updateCommand = "update Product set ModifyTime=?,ProductName=? WHERE id=?";
            con.setAutoCommit(false);//开启事务
            PreparedStatement preparedStatement = con.prepareStatement(updateCommand);
            preparedStatement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setString(2, "testJDBC");
            preparedStatement.setInt(3, 59);
            Integer result = preparedStatement.executeUpdate();
            con.commit();//try的最后提交事务
            preparedStatement.close();
            con.close();
            Integer m = 0;


        } catch (Exception ex) {
            con.rollback();//回滚事务
        }
    }


    private void concurrentTransaction() throws Exception {
        Connection con = getConnection();
        try {
            String updateCommand = "update Product set ModifyTime=?,ProductName=? WHERE id=?";
            con.setAutoCommit(false);//开启事务
            PreparedStatement preparedStatement = con.prepareStatement(updateCommand);
            preparedStatement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setString(2, "testJDBC");
            preparedStatement.setInt(3, 59);
            Integer result = preparedStatement.executeUpdate();
            con.commit();//try的最后提交事务
            preparedStatement.close();
            con.close();
            Integer m = 0;


        } catch (Exception ex) {
            con.rollback();//回滚事务
        }
    }
    //endregion

    //region 分页存储过程
    private void pageData() throws Exception {
        Connection con = getConnection();
        CallableStatement callableStatement = con.prepareCall("{call pageData(?,?,?)}");
        //注意参数没有@符号。
        callableStatement.setInt("pageIndex", 2);
        callableStatement.setInt("pageSize", 15);
        callableStatement.registerOutParameter("totalCount", Types.INTEGER);
        //3.执行存储过程
        callableStatement.execute();
        ResultSet rs = callableStatement.getResultSet();
        List<Product> list = convertToList(rs, Product.class);
        Integer totalCount = callableStatement.getInt("totalCount");

        Integer m = 0;
    }
    //endregion
}
