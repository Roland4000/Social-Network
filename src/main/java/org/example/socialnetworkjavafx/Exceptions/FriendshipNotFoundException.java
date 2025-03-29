package org.example.socialnetworkjavafx.Exceptions;

public class FriendshipNotFoundException extends RuntimeException {
    public FriendshipNotFoundException(String user1, String user2) {
        super("Friendship between " + user1 + " and " + user2 + " not found");
    }
}
