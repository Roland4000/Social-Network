package org.example.socialnetworkjavafx.Entities;

import java.util.ArrayList;
import java.util.List;

public class Network {
    private List<Friendship> friendships;

    public Network() {
        friendships = new ArrayList<>();
    }

    public void addFriend(Friendship friendship) {
        friendships.add(friendship);
    }

    public void removeFriend(Friendship friendship) {
        friendships.remove(friendship);
    }

    public int getNumberOfCommunities() {
        return 0;
    }
}
