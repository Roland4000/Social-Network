package org.example.socialnetworkjavafx.Factories;

import org.example.socialnetworkjavafx.Entities.Friendship;

public class FriendshipFactory {
    static final FriendshipFactory SINGLE_INSTANCE = new FriendshipFactory();

    private FriendshipFactory() {}

    public static FriendshipFactory getInstance() {
        return SINGLE_INSTANCE;
    }

    public Friendship createFriendship(String userName1, String userName2) {
        return new Friendship(userName1, userName2);
    }
}
