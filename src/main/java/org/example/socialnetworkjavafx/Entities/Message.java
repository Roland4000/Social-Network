package org.example.socialnetworkjavafx.Entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Message extends Entity<Integer> {
    private Integer id;
    private String from, to, content;
    private Integer repplyFor;
    private LocalDateTime time;

    public Message(String from, String to, String content) {
        this.from = from;
        this.to = to;
        this.content = content;
        this.repplyFor = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        this.time = LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter);
        this.id = hashCode();
    }

    public Message(String from, String to, String content, Integer repplyFor) {
        this.from = from;
        this.to = to;
        this.content = content;
        this.repplyFor = repplyFor;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        this.time = LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter);
        this.id = hashCode();
    }

    public Message(Integer id, String from, String to, String content, Integer repplyFor, LocalDateTime time) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.content = content;
        this.repplyFor = repplyFor;
        this.time = time;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public String getContent() {
        return content;
    }

    public String getTo() {
        return to;
    }

    public Integer getRepplyFor() {
        return repplyFor;
    }

    public LocalDateTime getTime() {
        return time;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, time);
    }
}
