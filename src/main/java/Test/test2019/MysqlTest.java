package Test.test2019;

import Model.Product;
import com.alibaba.fastjson.JSON;
import common.Configs;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

public class MysqlTest {
    public void test() {
        try {
            //  insert();
            query();
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
        String productName = "jdbc";
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
        preparedStatement.close();
        con.close();
        Integer m = 0;
    }
    //endregion
}
