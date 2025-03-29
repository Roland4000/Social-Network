package org.example.socialnetworkjavafx.Utils.Event;
import org.example.socialnetworkjavafx.Entities.User;

public class UserEntityChangeEvent implements Event {
    private ChangeType changeType;
    private User data, oldData;

    public UserEntityChangeEvent(ChangeType type, User data) {
        this.changeType = type;
        this.data = data;
    }

    public UserEntityChangeEvent(ChangeType type, User data, User oldData) {
        this.changeType = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ChangeType getType() {
        return changeType;
    }

    public User getData() {
        return data;
    }

    public User getOldData() {
        return oldData;
    }
}
