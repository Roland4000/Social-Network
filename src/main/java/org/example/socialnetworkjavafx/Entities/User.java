package org.example.socialnetworkjavafx.Entities;

import java.util.Objects;

public class User extends Entity<String> {
    private final String username;
    private final String password;
    private final String photoPath;
    private final String bio;

    public User(String username, String password, String photoPath, String bio) {
        this.username = username;
        this.password = password;
        this.photoPath = photoPath;
        this.bio = bio;
    }

    public String getId() {
        return username;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public String getBio() {
        return bio;
    }

    public boolean equals(User user) {
        return Objects.equals(this.username, user.getUsername());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username,password);
    }

    @Override
    public String toString() {
        return username;
    }
}
