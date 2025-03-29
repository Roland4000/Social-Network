package org.example.socialnetworkjavafx.Utils.Event;

import org.example.socialnetworkjavafx.Entities.Friendship;

public class FriendshipEntityChangeEvent implements Event {
    private ChangeType changeType;
    private Friendship data, oldData;

    public FriendshipEntityChangeEvent(ChangeType type, Friendship data) {
        this.changeType = type;
        this.data = data;
    }

    public FriendshipEntityChangeEvent(ChangeType type, Friendship data, Friendship oldData) {
        this.changeType = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ChangeType getType() {
        return changeType;
    }

    public Friendship getData() {
        return data;
    }

    public Friendship getOldData() {
        return oldData;
    }
}
