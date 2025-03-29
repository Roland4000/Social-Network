package org.example.socialnetworkjavafx.Utils.Event;

import org.example.socialnetworkjavafx.Entities.Message;

public class MessageEntityChangeEvent implements Event {
    private ChangeType changeType;
    private Message message, oldMessage;

    public MessageEntityChangeEvent(ChangeType changeType, Message message) {
        this.changeType = changeType;
        this.message = message;
    }

    public MessageEntityChangeEvent(ChangeType changeType, Message message, Message oldData) {
        this.changeType = changeType;
        this.message = message;
        this.oldMessage = oldData;
    }

    public ChangeType getChangeType() {
        return this.changeType;
    }

    public Message getData() {
        return this.message;
    }

    public Message getOldData() {
        return this.oldMessage;
    }
}
