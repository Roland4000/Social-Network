package org.example.socialnetworkjavafx.Entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Friendship extends Entity<Integer> {
    String initiator;
    String userName1, userName2;
    LocalDateTime dateTime;
    boolean pending;

    public Friendship(String user1, String user2){
        int rez = user1.compareToIgnoreCase(user2);

        if (rez < 0) {
            this.userName1 = user1;
            this.userName2 = user2;
        } else if(rez > 0) {
            this.userName1 = user2;
            this.userName2 = user1;
        } else {
            System.out.println(user1 + " and " + user2 + " are equal");
        }
        pending = true;
        initiator = user1;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        this.dateTime = LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter);
    }

    public Friendship(String user1, String user2, String initiator, LocalDateTime dateTime, boolean pending) {
        int rez = user1.compareToIgnoreCase(user2);
        this.initiator = initiator;
        if (rez <= 0) {
            this.userName1 = user1;
            this.userName2 = user2;
        } else {
            this.userName1 = user2;
            this.userName2 = user1;
        }
        this.dateTime = dateTime;
        this.pending = pending;
    }

    public Integer getId() {
        return hashCode();
    }

    public String getUser1() {
        return userName1;
    }

    public String getUser2() {
        return userName2;
    }

    public String getInitiator() {
        return initiator;
    }

    public boolean isPending() {
        return pending;
    }

    public void Accept() {
        pending = false;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        return Objects.equals(userName1, that.userName1) && Objects.equals(userName2, that.userName2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName1, userName2);
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "userName1='" + userName1 + '\'' +
                ", userName2='" + userName2 + '\'' +
                '}' + " since " + dateTime.format(DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss.SSS")) + " pending: " + pending;
    }
}
