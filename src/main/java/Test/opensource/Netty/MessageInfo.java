package Test.opensource.Netty;

import java.io.Serializable;
import java.text.MessageFormat;


/*
MarshallingDecoder 要求对象要实现Serializable接口
 */
//public class MessageInfo implements Serializable {
public class MessageInfo {
    private MessageType messageType;
    private String body;

    public MessageInfo(){}

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


    @Override
    public String toString() {
        return MessageFormat.format("MessageType:{0},Body:{1}", this.getMessageType(), this.getBody());
    }

}

