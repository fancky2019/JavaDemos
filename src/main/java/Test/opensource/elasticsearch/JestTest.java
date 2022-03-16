package Test.opensource.elasticsearch;

import Model.Student;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.*;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.mapping.PutMapping;
import io.searchbox.params.Parameters;
//import sun.util.locale.provider.FallbackLocaleProviderAdapter;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/*
mapping；类似table的表结构
ES6之后一个索引只有一个Type,8就没有Type。
 */
/*
github:https://github.com/searchbox-io/Jest
 */

/**
 * Jest客户端操作Elasticsearch
 */
public class JestTest {

    JestClient jestClient;

    //必须全小写
    private static final String INDEX = "test_java";
    private static final String TYPE = "_doc";

    public JestTest() {
        this.jestClient = jestClient();
    }

    public void test() {

        try {
            // createIndex();
//            deleteIndex();
//            createMapping();
//            insert();
            // delete();
//            updateByQuery();
            //  query();

//            pageFromSize(2,1);
//            pageFromSize(2,2);
//            pageScrollWithNoScroll_id();
            pageScrollWithScroll_id("DXF1ZXJ5QW5kRmV0Y2gBAAAAAAAAx-0WektmbDlGUU9RQ0NORHJldFFoZFR1Zw==");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    //region 创建JestClient

    /**
     * 创建JestClient
     *
     * @return
     */
    private JestClient jestClient() {
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(
                new HttpClientConfig.Builder("http://localhost:9200")
                        .multiThreaded(true)
                        .defaultMaxTotalConnectionPerRoute(2)
                        .maxTotalConnection(10)
                        .build());
        return factory.getObject();
    }
    //endregion

    // region 创建索引

    /**
     * 创建索引
     */
    private void createIndex() {
        try {

            //Kibana命令
        /*
        #3个分片，2个副本
        PUT /twittertest
        {
            "settings" : {
                "number_of_shards" : 3,
                "number_of_replicas" : 2
            }
        }
        #或下面简单方式
        PUT /twittertest111
         */
            //创建索引
            JestResult jestResult = jestClient.execute(new CreateIndex.Builder(INDEX).build());
            if (!jestResult.isSucceeded()) {
                //失败
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }


    }
//endregion

    //region  删除索引

    /**
     * 删除索引
     */
    private void deleteIndex() throws IOException {
        //Kibana命令
        /*
        DELETE  /test_java
         */

        //  删除索引
        jestClient.execute(new DeleteIndex.Builder("test_java").build());
    }
    //endregion

    //region 创建修改mapping

    /**
     * 创建修改mapping:ES会在插入数据的时候动态创建mapping.
     */
    private void createMapping() throws IOException {
        //Kibana命令
        /*
        PUT /test_java/_mapping
{
  "properties": {
    "job": {
      "type": "text",
      "fields": {
        "keyword": {
          "type": "keyword",
          "ignore_above": 256
        }
      }
    }
  }
}
         */
        //修改mapping。
        String mappingString = "{\n" +
                "  \"properties\": {\n" +
                "    \"job\": {\n" +
                "      \"type\": \"text\",\n" +
                "      \"fields\": {\n" +
                "        \"keyword\": {\n" +
                "          \"type\": \"keyword\",\n" +
                "          \"ignore_above\": 256\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
        //创建 mapping,ES7不用传type,否则报错，默认type "_doc" :格式：PUT /test_java/_mapping
        PutMapping.Builder builder = new PutMapping.Builder(INDEX, "", mappingString);
        JestResult jestResult = jestClient.execute(builder.build());
    }
    //endregion

    //region  插入数据

    /**
     * 插入数据:会自动创建mapping(类似库表结构)
     *
     * @throws IOException
     */
    private void insert() throws IOException {

        //Kibana命令
        /*
       POST /test_java/_doc/1
        {
          "name": "li yuan ba",
          "age": 237,
          "id": 1
        }
         */

        //插入数据
        Student student = new Student();
        student.setId(3);
        student.setName("li");
        student.setAge(27);
        // 手动创建类型(map一旦定义创建，field只能新增，不能修改)
        //#mapping 数据库的库表结构,#6之后一个索引只有一个Type,ES8就没有Type.#创建索引，并添加文档。
        //在ES可视化工具Kibana中查看执行结果。
        //如果student没有给属性设置值，将不自动创建mapping。

        // 要传 type  不然拼接URL错误（中间少了默认的/_doc/）："Incorrect HTTP method for uri [/test_java/3] and method [PUT], allowed: [POST]"
        //index的id最好用guid
        Index action = new Index.Builder(student).index(INDEX).type(TYPE).id(student.getId().toString()).build();
        JestResult jestResult = jestClient.execute(action);
        if (!jestResult.isSucceeded()) {
            //失败
        }
    }
    //endregion

    //region  删除数据

    /**
     * 删除数据
     */
    private void delete() throws IOException {
        //Kibana命令
        /*
       #删除文档
        DELETE db_test/_doc/1
         */
        Student student = new Student();
        student.setId(3);
        Delete delete = new Delete.Builder(student.getId().toString()).index(INDEX).type(TYPE).build();
        jestClient.execute(delete);
    }
    //endregion

    //region 条件更新数据

    /**
     * 条件更新数据
     *
     * @return
     * @throws IOException
     */
    private Boolean updateByQuery() throws IOException {

        //Kibana命令
        /*
       #条件更新
        POST /test_java/_update_by_query
        {
          "query": {
            "match": {
              "id": 1
            }
          },
          "script": {
            "lang": "painless",
            "source": "ctx._source.name=params.name;ctx._source.age=params.age",
            "params": {
              "name": "李元霸",
              "age": 27
            }
          }
        }

        #批量更新
            POST db_test/_update_by_query
            {
               "query": {
                "match_all": {}
              },
                "script" : "ctx._source.job = 'nongmin'"
            }
         */
        Student student = new Student();
        student.setId(1);
        student.setName("李元霸");
        student.setAge(27);

        //  MessageFormat 占位符转换，会出现问题
        String script = "  {\n" +
                "          \"query\": {\n" +
                "            \"match\": {\n" +
                "              \"id\": " + student.getId() + " \n" +
                "            }\n" +
                "          },\n" +
                "          \"script\": {\n" +
                "            \"lang\": \"painless\",\n" +
                "            \"source\": \"ctx._source.name=params.name;ctx._source.age=params.age\",\n" +
                "            \"params\": {\n" +
                "              \"name\": \" " + student.getName() + " \",\n" +
                "              \"age\": " + student.getAge() + "\n" +
                "            }\n" +
                "          }\n" +
                "        }";


        /*
        json 中有花括号 { 、}，用单引号将花括号包起来 '{','}'
        Ctrl+R 替换,所有内容中替换
         */
        String script2 = MessageFormat.format("  '{'\n" +
                "          \"query\": '{'\n" +
                "            \"match\": '{'\n" +
                "              \"id\": {0}  \n" +
                "            '}'\n" +
                "          '}',\n" +
                "          \"script\": '{'\n" +
                "            \"lang\": \"painless\",\n" +
                "            \"source\": \"ctx._source.name=params.name;ctx._source.age=params.age\",\n" +
                "            \"params\": '{'\n" +
                "              \"name\": \" {1} \",\n" +
                "              \"age\": {2}\n" +
                "            '}'\n" +
                "          '}'\n" +
                "        '}'", student.getId(), student.getName(), student.getAge());

        UpdateByQuery updateByQuery = new UpdateByQuery.Builder(script).addIndex(INDEX).build();
        UpdateByQueryResult result = jestClient.execute(updateByQuery);
        long updatedCount = result.getUpdatedCount();
        if (result.isSucceeded() && updatedCount != 0L) {
            return true;
        }

        return false;
    }
    //endregion

    //region 查询
    private void query() throws IOException {
        //Kibana命令
        /*
        GET /test_java/_search/
        {
         "query":{
              "bool": {
                "must": [
                  {"match":{"name":"李"}},
                  {"match":{"age":27}}
                ]
              }
            }
        }

        #如果不指定要查询的索引，将在所有索引中查询
        GET /_search
        {
         "query":{
              "bool": {
                "must": [
                  {"match":{"name":"李"}},
                  {"match":{"age":27}}
                ]
              }
            }
        }
         */

        Student student = new Student();
        student.setId(1);
        student.setName("李元霸");
        student.setAge(27);

        String query = "     {\n" +
                "         \"query\":{\n" +
                "              \"bool\": {\n" +
                "                \"must\": [\n" +
                "                  {\"match\":{\"name\":\"" + student.getName() + "\"}},\n" +
                "                  {\"match\":{\"age\":" + student.getAge() + "}}\n" +
                "                ]\n" +
                "              }\n" +
                "            }\n" +
                "        }";
        Search search = new Search.Builder(query)
                //以下方法相当于拼接GET后的URI
                .addIndex(INDEX)//在指定的index中查找，不指定查找所有index
                .addType(TYPE)//在指定的文档中
                .build();

        SearchResult result = jestClient.execute(search);

        if (result != null && result.isSucceeded()) {
            //可以通过students.size()获取
            String hitCount = result.getJsonObject().get("hits").getAsJsonObject().get("total").getAsJsonObject().get("value").toString();
            List<SearchResult.Hit<Student, Void>> listHits = result.getHits(Student.class);
            List<Student> students = listHits.stream().map(p -> p.source).collect(Collectors.toList());

            int m = 0;
        }
    }
    //endregion

    //region pageFromSize
    private void pageFromSize(Integer pageSize, Integer pageIndex) throws IOException {
        //Kibana命令
        /*
        GET /test_java/_search
        {
          "query": {
            "bool": {
              "must": [
                {
                  "match": {
                    "name": "李"
                  }
                }
              ]
            }
          },
          "size": 2,
          "from": 0,
          "sort": [
            {
              "age": {
                "order": "asc"
              },
              "id": {
                "order": "desc"
              }
            }
          ]
        }
         */

        Student student = new Student();
        student.setId(1);
        student.setName("李元霸");
        student.setAge(27);

        String query = "{\n" +
                "  \"query\": {\n" +
                "    \"bool\": {\n" +
                "      \"must\": [\n" +
                "                {\"match\":{\"name\":\"" + student.getName() + "\"}}\n" +
                "      ]\n" +
                "    }\n" +
                "  },\n" +
                "  \"size\": " + pageSize + ",\n" +
                "  \"from\": " + pageSize * (pageIndex - 1) + "\n" +
                "}";
        Search search = new Search.Builder(query)
                //以下方法相当于拼接GET后的URI
                .addIndex(INDEX)//在指定的index中查找，不指定查找所有index
                .addType(TYPE)//在指定的文档中
                .build();

        SearchResult result = jestClient.execute(search);

        if (result != null && result.isSucceeded()) {
            //可以通过students.size()获取
            String hitCount = result.getJsonObject().get("hits").getAsJsonObject().get("total").getAsJsonObject().get("value").toString();
            List<SearchResult.Hit<Student, Void>> listHits = result.getHits(Student.class);
            List<Student> students = listHits.stream().map(p -> p.source).collect(Collectors.toList());

            int m = 0;
        }
    }

    //endregion

    //region pageScrollWithNoScroll_id
    private void pageScrollWithNoScroll_id() throws IOException {
        //Kibana命令
        /*
          GET /test_java/_search?scroll=3m
        {
            "query": { "match_all": {}},
            "sort" : ["_doc"],
            "size": 2

        }

        #scroll因为缓存会占用大量内存。数量小可采用size,from
        #scroll_id 为上面查询语句返回的
        #每次执行此语句从scroll_id（类似游标）位置继续往下查，直到没有数据位置
        POST /_search/scroll
        {
          "scroll":"1m",
          "scroll_id": "DXF1ZXJ5QW5kRmV0Y2gBAAAAAAAArv4WektmbDlGUU9RQ0NORHJldFFoZFR1Zw=="
        }
         */

        Student student = new Student();
        student.setId(1);
        student.setName("李元霸");
        student.setAge(27);

        String query = "    {\n" +
                "            \"query\": { \"match_all\": {}},\n" +
                "            \"size\": 2\n" +
                "        \n" +
                "        }";

        Search search = new Search.Builder(query)
                //以下方法相当于拼接GET后的URI
                .addIndex(INDEX)//在指定的index中查找，不指定查找所有index
                .addType(TYPE)//在指定的文档中
                .addSourceIncludePattern("open_id")
                .setParameter(Parameters.SCROLL, "10m")//设置scroll查询
                .build();

        SearchResult result = jestClient.execute(search);

        if (result != null && result.isSucceeded()) {
            //可以通过students.size()获取
            String hitCount = result.getJsonObject().get("hits").getAsJsonObject().get("total").getAsJsonObject().get("value").toString();
            List<SearchResult.Hit<Student, Void>> listHits = result.getHits(Student.class);
            List<Student> students = listHits.stream().map(p -> p.source).collect(Collectors.toList());

            int m = 0;
        }
    }
    //endregion

    //region pageScrollWithScroll_id
    private void pageScrollWithScroll_id(String scroll_id) throws IOException {
        //Kibana命令
        /*
          GET /test_java/_search?scroll=3m
        {
            "query": { "match_all": {}},
            "sort" : ["_doc"],
            "size": 2

        }

        #scroll因为缓存会占用大量内存。数量小可采用size,from
        #scroll_id 为上面查询语句返回的
        #每次执行此语句从scroll_id（类似游标）位置继续往下查，直到没有数据位置
        POST /_search/scroll
        {
          "scroll":"1m",
          "scroll_id": "DXF1ZXJ5QW5kRmV0Y2gBAAAAAAAArv4WektmbDlGUU9RQ0NORHJldFFoZFR1Zw=="
        }
         */

        Student student = new Student();
        student.setId(1);
        student.setName("李元霸");
        student.setAge(27);

        String query = "  {\n" +
                "          \"scroll\":\"10m\",\n" +
                "          \"scroll_id\": \"" + scroll_id + "\"\n" +
                "        }";
        Search search = new Search.Builder(query)
                //以下方法相当于拼接GET后的URI
                .setParameter(Parameters.SCROLL, "3m")//设置scroll查询
                .build();

        SearchResult result = jestClient.execute(search);

        if (result != null && result.isSucceeded()) {
            //可以通过students.size()获取
            String hitCount = result.getJsonObject().get("hits").getAsJsonObject().get("total").getAsJsonObject().get("value").toString();
            List<SearchResult.Hit<Student, Void>> listHits = result.getHits(Student.class);
            List<Student> students = listHits.stream().map(p -> p.source).collect(Collectors.toList());

            int m = 0;
        }
    }
    //endregion
}
