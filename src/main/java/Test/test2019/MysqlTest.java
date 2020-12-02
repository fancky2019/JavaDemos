package Test.test2019;

import Model.Product;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Stopwatch;
import utility.Configs;

import java.math.BigDecimal;
import java.sql.*;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * mysql  数据库操作
 * MSSQL:demo2018 JDBCTest
 */
public class MysqlTest {
    public void test() {
        try {
//              insert();
            insertTakeTime();
//              query();
//            queryDifferentDB();
//            insertDynamic();
//            batchInsert();
            Integer m = 0;
            //  createProduct();
        } catch (Exception ex) {
            String msg = ex.getMessage();
            Integer m = 0;
        }
    }


    private Connection getConnection() {
        try {
            Class.forName(Configs.Instance.getMysqlDriverName());

            Connection con = DriverManager.getConnection(Configs.Instance.getMysqldburl());
//            Connection con =     DriverManager.getConnection(Configs.Instance.getMysqldburl(),Configs.Instance.getMysqlUser(),Configs.Instance.getMysqlPassword());
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
        String productName = "p";
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


    //endregion

    //region 跨库查询
    private void queryDifferentDB() throws Exception {
        String selectCommand = "select *  from  valvulas.`product` WHERE ProductName LIKE ?";
        Connection con = getConnection();
        PreparedStatement preparedStatement = con.prepareStatement(selectCommand);
        String productName = "Br";
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
    //endregion

    //region插入

    private void insertDynamic() {
        Integer i = 1;
        while (i <= 1000000) {
            try {
                Product product = createProduct();
                insert(product);
                i++;

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                Integer m = 0;
            }
        }

    }


    private final static String WORD = "abcdefghijklmnopqrstuvwxyz";

    private Product createProduct() {
        Product product = new Product();
        Random random = new Random();
        //nextInt(11):[0,11)即使[0,10]
        Integer productNameLength = random.nextInt(11) + 5; //随机5--15
        //   Integer productNameLength=length

        StringBuilder productName = new StringBuilder();
        for (int i = 0; i < productNameLength; i++) {
            //随机一个字母
            Integer wordIndex = random.nextInt(26);
            productName.append(WORD.charAt(wordIndex));
        }
        product.setProductname(productName.toString());
        product.setGuid(UUID.randomUUID().toString());
        product.setCount(random.nextInt(100) + 1);
        product.setStockid(random.nextInt(10000) + 1);
        product.setSkuid(random.nextInt(100000) + 1);
        product.setBarcodeid(random.nextInt(100000) + 1);

        product.setPrice(BigDecimal.valueOf(random.nextInt(500) + 1));
        product.setProductstyle(MessageFormat.format("ProductStyle{0}", random.nextInt(10) + 1));
        product.setStatus((short) 1);
        product.setCount(random.nextInt(19999) + 1);


        Integer year = random.nextInt(19) + 2000;//2000--2019
        Integer month = random.nextInt(12) + 1;//1--12
        Integer day = random.nextInt(28) + 1;//0--28
        Integer hour = random.nextInt(24);
        Integer minute = random.nextInt(59);
        Integer second = random.nextInt(59);
        Integer milliSecond = random.nextInt(1000);

        //秒 毫秒 微秒 纳秒
        Long nanos = 1000L * 1000L * Long.valueOf(milliSecond);

        //2000-1-1 0:0:0.000
        LocalDateTime localDateTime = LocalDateTime.of(year, month, day, hour, minute, second);
        localDateTime = localDateTime.plusNanos(nanos);


        product.setCreatetime(localDateTime);
        product.setModifytime(localDateTime);
        return product;
    }


    private void insert(Product product) throws Exception {
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
        preparedStatement.setString(1, product.getGuid());
        preparedStatement.setInt(2, product.getStockid());
//        preparedStatement.setNull(3, Types.INTEGER);
        preparedStatement.setInt(3, product.getBarcodeid());
        preparedStatement.setInt(4, product.getSkuid());
        preparedStatement.setString(5, product.getProductname());
//        preparedStatement.setNull(6, Types.NVARCHAR);
        //如果字段为空不报错
        preparedStatement.setString(6, product.getProductstyle());
        preparedStatement.setBigDecimal(7, product.getPrice());
        Long milliSecond = product.getCreatetime().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        //                 setDate只能得到年月日
        preparedStatement.setTimestamp(8,
//         new  Timestamp(  product.getCreatetime().getYear(),
//                product.getCreatetime().getMonthValue(),
//                product.getCreatetime().getDayOfMonth(),
//                product.getCreatetime().getHour(),
//                product.getCreatetime().getMinute(),
//                product.getCreatetime().getSecond(),
//                 product.getCreatetime().getNano())
                new Timestamp(milliSecond)
        );
        Short status = 1;
        preparedStatement.setShort(9, product.getStatus());
        preparedStatement.setInt(10, product.getCount());
//        preparedStatement.setTimestamp(11, new Timestamp(System.currentTimeMillis()));
        preparedStatement.setTimestamp(11, new Timestamp(milliSecond));
        //执行命令并接受结果
        Integer result = preparedStatement.executeUpdate();
//            ResultSet rs = preparedStatement.getGeneratedKeys();
//            if (rs.next()) {
//                Integer id = rs.getInt(1);
//                Integer m = 0;
//            }

        preparedStatement.close();
        con.close();


        Integer m = 0;
    }


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
        preparedStatement.setString(5, "product");
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
        //不返回自动生成的主键
        //  PreparedStatement preparedStatement = con.prepareStatement(insertCommand);
        //插入语句Statement.RETURN_GENERATED_KEYS返回生成的主键
        PreparedStatement preparedStatement = con.prepareStatement(insertCommand, Statement.RETURN_GENERATED_KEYS);
        //设置参数
        preparedStatement.setString(1, UUID.randomUUID().toString());
        preparedStatement.setInt(2, 1);
        preparedStatement.setNull(3, Types.INTEGER);
        preparedStatement.setInt(4, 1);
        preparedStatement.setString(5, "product");
        preparedStatement.setNull(6, Types.NVARCHAR);
        preparedStatement.setBigDecimal(7, new BigDecimal(123));
        //                 setDate只能得到年月日
        preparedStatement.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
        Short status = 1;
        preparedStatement.setShort(9, status);
        preparedStatement.setInt(10, 12);
        preparedStatement.setTimestamp(11, new Timestamp(System.currentTimeMillis()));
        Stopwatch stopwatch = Stopwatch.createStarted();
        //执行命令并接受结果
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
        //  PreparedStatement preparedStatement = con.prepareStatement(insertCommand);
        // sqlserver批量插入获取自增key报异常,mysql不报错。
        //插入语句Statement.RETURN_GENERATED_KEYS返回生成的主键
        PreparedStatement preparedStatement = con.prepareStatement(insertCommand, Statement.RETURN_GENERATED_KEYS);
        for (int i = 0; i < 3; i++) {
            //设置参数
            preparedStatement.setString(1, UUID.randomUUID().toString());
            preparedStatement.setInt(2, 1);
            preparedStatement.setNull(3, Types.INTEGER);
            preparedStatement.setInt(4, 1);
//            preparedStatement.setString(5, "product");
            if (i != 1) {
                preparedStatement.setString(5, MessageFormat.format("batchInsert{0}", i));
            } else {
                //其中一条异常，其他没有回滚，和数据库里执行节本不同。数据库里当成一个事务。
                //针对其中有可能有异常数据，要开启事务。
                preparedStatement.setString(5, "dddddddddddddddddddddddddddddddddddddddddddddsdsffffffffffdddddddddddddddddddddddddddddddddddddddddddddsdsffffffffffdddddddddddddddddddddddddddddddddddddddddddddsdsffffffffffdddddddddddddddddddddddddddddddddddddddddddddsdsffffffffffdddddddddddddddddddddddddddddddddddddddddddddsdsffffffffff");
            }
            preparedStatement.setNull(6, Types.NVARCHAR);
            preparedStatement.setBigDecimal(7, new BigDecimal(123));
            //                 setDate只能得到年月日
            preparedStatement.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
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
        // sqlserver批量插入获取自增key报异常,mysql不报错。
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
}
