package Test.opensource.Netty;

import org.msgpack.annotation.Message;

import java.io.Serializable;

@Message
public class MessageInfo implements Serializable {
    private MessageType messageType;
    private String body;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public   MessageInfo()
    {}

    @Override
    public String toString() {
        return body;
    }

}

