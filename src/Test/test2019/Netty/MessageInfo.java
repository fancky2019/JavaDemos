package Test.test2019.Netty;

import java.io.Serializable;

public class MessageInfo  implements Serializable {
    private  MessageType messageType;
    private  String body;

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
    public String  toString()
    {
        return  body;
    }

}

