package Test.opensource.Netty;

import org.msgpack.annotation.Message;

@Message
public enum MessageType  {
    HeartBeat,
    Data;
    private MessageType()
    {

    }
}