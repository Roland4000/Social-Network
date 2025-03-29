package org.example.socialnetworkjavafx.Infrastructure;

import org.example.socialnetworkjavafx.Entities.Message;
import org.example.socialnetworkjavafx.Repositories.AbstractDbRepository;
import org.example.socialnetworkjavafx.Utils.Paging.Page;
import org.example.socialnetworkjavafx.Utils.Paging.Pageable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MessageDbRepository extends AbstractDbRepository<Integer, Message> {
    public MessageDbRepository(Connection connection) {
        super(connection);
    }

    @Override
    public Optional<Message> findOne(Integer integer) {
        String query = "SELECT * FROM messages WHERE message_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, integer);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
                return Optional.of(new Message(
                        resultSet.getInt("message_id"),
                        resultSet.getString("sender"),
                        resultSet.getString("receiver"),
                        resultSet.getString("content"),
                        resultSet.getInt("reply_for"),
                        resultSet.getTimestamp("date_time").toLocalDateTime()
                ));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Message> findAll() {
        String query = "SELECT * FROM messages";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            List<Message> messages = new ArrayList<>();

            while (resultSet.next()) {
                messages.add(new Message(
                        resultSet.getInt("message_id"),
                        resultSet.getString("sender"),
                        resultSet.getString("receiver"),
                        resultSet.getString("content"),
                        resultSet.getInt("reply_for"),
                        resultSet.getTimestamp("date_time").toLocalDateTime()));
            }
            return messages;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<Message>();
    }

    public List<Message> findAllMessagesOf(String sender, String receiver) {
        List<Message> messages = new ArrayList<>();

        String query = "SELECT * FROM messages WHERE (sender = ? AND receiver = ?) OR (sender = ? AND receiver = ?) ORDER BY date_time";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, sender);
            statement.setString(4, sender);
            statement.setString(2, receiver);
            statement.setString(3, receiver);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                messages.add(new Message(
                        resultSet.getInt("message_id"),
                        resultSet.getString("sender"),
                        resultSet.getString("receiver"),
                        resultSet.getString("content"),
                        resultSet.getInt("reply_for"),
                        resultSet.getTimestamp("date_time").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public Optional<Message> save(Message entity) {
        String query = "INSERT INTO messages (message_id, sender, receiver, content, reply_for, date_time) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, entity.getId());
            statement.setString(2, entity.getFrom());
            statement.setString(3, entity.getTo());
            statement.setString(4, entity.getContent());
            statement.setInt(5, entity.getRepplyFor());
            statement.setTimestamp(6, Timestamp.valueOf(entity.getTime()));

            statement.execute();
            return Optional.of(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Message> delete(Integer integer) {
        Optional<Message> toDelete = findOne(integer);
        if (toDelete.isPresent()) {
            String query = "DELETE FROM messages WHERE message_id = ?";
            try {
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, integer);
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Message> update(Message entity) {
        return super.update(entity);
    }

    @Override
    public Page<Message> findAllOnPage(Pageable pageable, String user) {
        return null;
    }
}
