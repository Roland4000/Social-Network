package org.example.socialnetworkjavafx.Factories;

import org.example.socialnetworkjavafx.Entities.User;

public class UserFactory {
    static final UserFactory SINGLE_INSTANCE = new UserFactory();

    private UserFactory() {
    }

    public static UserFactory getInstance() {
        return SINGLE_INSTANCE;
    }

    public User createUser(String name, String password, String photoPath, String bio) {
        return new User(name, password, photoPath, bio);
    }
}
