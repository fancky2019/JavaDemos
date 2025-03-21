package Test.opensource.sqlite;

import Model.JacksonPojo;
import Model.Product;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import utility.Configs;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

public class SQLiteTest {

    public void test() {
//        //路径：D:\fancky\Git\Java\JavaDemos
//        String relativelyPath = System.getProperty("user.dir");
//        //路径：/D:/fancky/Git/Java/JavaDemos/target/classes/
//        String path = this.getClass().getClassLoader().getResource("").getPath();
//        int m = 0;

        try {
//            insert();

            query();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private Connection getConnection() {
        try {

            Class.forName("org.sqlite.JDBC");
            String relativelyPath = System.getProperty("user.dir");
            String dbPath = "jdbc:sqlite:" + relativelyPath + "\\SQLite\\JavaTest.db";


            Connection con = DriverManager.getConnection(dbPath);

            return con;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

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
    //endregion

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

        //fastJson 序列化
//        String jsonString = JSON.toJSONString(list);
//        List<T> resultList = JSON.parseArray(jsonString, t);

        //jackson 反序列化
        ObjectMapper mapper = new ObjectMapper();
        //序列化
        String jsonStr = mapper.writeValueAsString(list);
        //反序列化
        List<T> resultList = mapper.readValue(jsonStr, new TypeReference<List<T>>() {
        });

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

            String msg = ex.getMessage();
            Integer m = 0;
        } finally {
            preparedStatement.close();
            con.close();
        }
    }
    //endregion
}
