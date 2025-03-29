package org.example.socialnetworkjavafx.Exceptions;

public class AcceptFriendshipException extends RuntimeException {
    public AcceptFriendshipException() {
        super("You cannot accept your own friendship request;");
    }
}
