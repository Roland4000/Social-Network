package org.example.socialnetworkjavafx.Factories;

import org.example.socialnetworkjavafx.Entities.Message;

import java.util.List;

public class MessageFactory {
    static final MessageFactory SINGLE_INSTANCE = new MessageFactory();

    private MessageFactory() {
    }

    public static MessageFactory getInstance() {
        return SINGLE_INSTANCE;
    }

    public Message createMessage(String sender, String receiver, String content) {
        return new Message(sender, receiver, content);
    }

    public Message createMessage(String sender, String receiver, String content, Integer reply_id) {
        return new Message(sender, receiver, content, reply_id);
    }
}
