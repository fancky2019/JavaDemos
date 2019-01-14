package Test.rabbitMQ;

public class ExchangeType {
    /**
     * 单播 Exchange type used for AMQP direct exchanges.
     */
    public static final String DIRECT = "direct";
    /**
     * 广播 Exchange type used for AMQP fanout exchanges.
     */
    public static final String FANOUT = "fanout";
    /**
     *   Exchange type used for AMQP headers exchanges.
     */
    public static final String HEADERS = "headers";
    /**
     * 组播 Exchange type used for AMQP topic exchanges.
     */
    public static final String TOPIC = "topic";
}
