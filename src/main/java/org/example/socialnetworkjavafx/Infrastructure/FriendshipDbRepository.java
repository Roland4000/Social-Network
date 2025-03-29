package org.example.socialnetworkjavafx.Infrastructure;
import org.example.socialnetworkjavafx.Entities.Friendship;
import org.example.socialnetworkjavafx.Repositories.AbstractDbRepository;
import org.example.socialnetworkjavafx.Utils.Paging.Page;
import org.example.socialnetworkjavafx.Utils.Paging.Pageable;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FriendshipDbRepository extends AbstractDbRepository<Integer, Friendship> {
    public FriendshipDbRepository(Connection connection) {
        super(connection);
    }

    @Override
    public Optional<Friendship> findOne(Integer integer) {
        return findAll()
                .stream()
                .filter(friendship -> friendship.getId().equals(integer))
                .findFirst();
    }

    @Override
    public List<Friendship> findAll() {
        String query = "SELECT * FROM friendships";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            List<Friendship> friendships = new ArrayList<>();
            while (resultSet.next()) {
                LocalDateTime since = resultSet.getTimestamp("since").toLocalDateTime();
                boolean pending = resultSet.getBoolean("pending");
                friendships.add(new Friendship(resultSet.getString("username1"), resultSet.getString("username2"), resultSet.getString("initiator"), since, pending));
            }
            return friendships;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<Friendship>();
    }

    @Override
    public Optional<Friendship> save(Friendship entity) {
        String query = "INSERT INTO friendships (username1, username2, since, pending, initiator) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);

            int res = entity.getUser1().compareToIgnoreCase(entity.getUser2());

            if (res < 0) {
                statement.setString(1, entity.getUser1());
                statement.setString(2, entity.getUser2());
            } else {
                statement.setString(1, entity.getUser2());
                statement.setString(2, entity.getUser1());
            }

            statement.setTimestamp(3, Timestamp.valueOf(entity.getDateTime()));

            statement.setBoolean(4, entity.isPending());  // Store boolean as true/false
            statement.setString(5, entity.getInitiator());

            statement.execute();

            return Optional.of(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Friendship> delete(Integer integer) {
        Optional<Friendship> friendship = findOne(integer);
        if (friendship.isPresent()) {
            String query = "DELETE FROM friendships WHERE username1 = ? AND username2 = ?";
            try {
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, friendship.get().getUser1());
                statement.setString(2, friendship.get().getUser2());
                statement.execute();
                return friendship;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Friendship> update(Friendship entity) {
        String query = "UPDATE friendships SET since = ?, pending = ? WHERE username1 = ? AND username2 = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setTimestamp(1, Timestamp.valueOf(entity.getDateTime()));  // Store timestamp
            statement.setBoolean(2, entity.isPending());  // Store boolean as true/false

            int res = entity.getUser1().compareToIgnoreCase(entity.getUser2());
            if (res < 0) {
                statement.setString(3, entity.getUser1());
                statement.setString(4, entity.getUser2());
            } else {
                statement.setString(3, entity.getUser2());
                statement.setString(4, entity.getUser1());
            }

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                return Optional.of(entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Page<Friendship> findAllOnPage(Pageable pageable, String user) {
        List<Friendship> friendships = new ArrayList<>();
        String query = "select * from friendships where username1 = ? or username2 = ?";
        query += " LIMIT ? OFFSET ? ";


        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user);
            statement.setString(2, user);
            statement.setInt(3, pageable.getPageSize());
            statement.setInt(4, pageable.getPageSize() * pageable.getPageNumber());

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String username1 = resultSet.getString("username1");
                String username2 = resultSet.getString("username2");
                String initiator = resultSet.getString("initiator");
                LocalDateTime dateTime = resultSet.getTimestamp("since").toLocalDateTime();
                boolean pending = resultSet.getBoolean("pending");
                Friendship friendship = new Friendship(username1, username2, initiator, dateTime, pending);
                friendships.add(friendship);
            }

            return new Page<>(friendships, findAll().size());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}