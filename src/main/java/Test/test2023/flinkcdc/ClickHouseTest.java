package Test.test2023.flinkcdc;


import com.ververica.cdc.connectors.mysql.source.MySqlSource;
import com.ververica.cdc.connectors.mysql.table.StartupOptions;
import com.ververica.cdc.debezium.JsonDebeziumDeserializationSchema;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;


import java.sql.*;
import java.util.HashMap;
import java.util.Map;


public class ClickHouseTest {

    public void test() throws Exception {
//        testConnect();
//        syncData();
        mysqlCdc();
    }

    public void testConnect() {

        try {

            Class.forName("ru.yandex.clickhouse.ClickHouseDriver");
            // Class.forName("ru.yandex.clickhouse.ClickHouseDriver");// 驱动包
            String url = "jdbc:clickhouse://172.16.112.137:8123/default";// url路径
            String user = "default";// 账号
            String password = "root";// 密码

            user = "";// 账号
            password = "";// 密码
            Connection connection = DriverManager.getConnection(url, user, password);

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from demo_product_ch");
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.println(metaData.getColumnName(i) + ":" + resultSet.getString(i));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 在项目中报错无法捕捉mysql 数据，单独建一个项目正常
     * @throws Exception
     */
    private void mysqlCdc() throws Exception {

        try {
//
//
//            MySqlSource<String> mySqlSource = MySqlSource.<String>builder()
//                    .hostname("127.0.0.1")
//                    .port(3306)
//                    .databaseList("demo") // set captured database
//                    .tableList("demo.demo_product_flink_test") // set captured table
//                    .username("root")
//                    .password("123456")
//                    .deserializer(new JsonDebeziumDeserializationSchema()) // converts SourceRecord to JSON String
////            new MysqlDeserialization() JsonDebeziumDeserializationSchema
////                /**initial初始化快照,即全量导入后增量导入(检测更新数据写入)
////                 * latest:只进行增量导入(不读取历史变化)
////                 * timestamp:指定时间戳进行数据导入(大于等于指定时间错读取数据)
////                 */
////                .startupOptions(StartupOptions.latest())
//
//                    .build();
//
//            StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//            Configuration config = new Configuration();
//            // 设置增量快照开启为 true
//            config.setBoolean("scan.incremental.snapshot.enabled", true);
//            env.configure(config);
//            env.setParallelism(1);
//
//            //        DebeziumSourceFunction<DataChangeInfo> dataChangeInfoMySqlSource = buildDataChangeSource();
//            DataStreamSource<String> streamSource = env.fromSource(mySqlSource, WatermarkStrategy.noWatermarks(), "MySQL Source")
//                    .setParallelism(1);
//            streamSource.addSink(new DataChangeSink());
//            env.execute("mysql-stream-cdc");
//            int m = 0;





                        MySqlSource<String> mySqlSource = MySqlSource.<String>builder()
                    .hostname("127.0.0.1")
                    .port(3306)
                    .databaseList("demo") // set captured database
                    .tableList("demo_product_flink_test") // set captured table
                    .username("root")
                    .password("123456")
                    .deserializer(new JsonDebeziumDeserializationSchema()) // converts SourceRecord to JSON String


                    .build();

            StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

            // enable checkpoint
            env.enableCheckpointing(3000);

            env
                    .fromSource(mySqlSource, WatermarkStrategy.noWatermarks(), "MySQL Source")
                    // set 4 parallel source tasks
                    .setParallelism(4)
//                    .addSink(new DataChangeSink())
                    .print()
                    .setParallelism(1); // use parallelism 1 for sink to keep message ordering

            env.execute("Print MySQL Snapshot + Binlog");

        } catch (Exception ex) {
            int m = 0;
        }
    }

    private void syncData() {
//        TableEnvironment tEnv = TableEnvironment.create(setting);
//
//        Map<String, String> props = new HashMap<>();
//        props.put(ClickHouseConfig.DATABASE_NAME, "default");
//        props.put(ClickHouseConfig.URL, "clickhouse://127.0.0.1:8123");
//        props.put(ClickHouseConfig.USERNAME, "username");
//        props.put(ClickHouseConfig.PASSWORD, "password");
//        props.put(ClickHouseConfig.SINK_FLUSH_INTERVAL, "30s");
//        Catalog cHcatalog = new ClickHouseCatalog("clickhouse", props);
//        tEnv.registerCatalog("clickhouse", cHcatalog);
//        tEnv.useCatalog("clickhouse");
//
//        tEnv.executeSql("insert into `clickhouse`.`default`.`t_table` select...");

    }
}
