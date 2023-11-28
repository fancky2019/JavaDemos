package Test.test2023.flinkcdc;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

@Slf4j
public class DataChangeSink implements SinkFunction<String> {

    @Override
    public void invoke(String value, Context context) throws Exception {
        log.info("收到变更原始数据:{}", value);
        //业务代码
    }
}
