package org.example.socialnetworkjavafx.Infrastructure;

import org.example.socialnetworkjavafx.Entities.User;
import org.example.socialnetworkjavafx.Repositories.AbstractDbRepository;
import org.example.socialnetworkjavafx.Repositories.PagingRepository;
import org.example.socialnetworkjavafx.Utils.Paging.Page;
import org.example.socialnetworkjavafx.Utils.Paging.Pageable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDbRepository extends AbstractDbRepository<String, User> {

     int SHIFT_VALUE=3;

    public UserDbRepository(Connection connection) {
        super(connection);
    }

    @Override
    public Optional<User> findOne(String s) {

        String query = "SELECT * FROM users WHERE username = ?";
        try {

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, s);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
                return Optional.of(new User(resultSet.getString("username"), decrypt(resultSet.getString("password"),3), resultSet.getString("photoPath"), resultSet.getString("bio")));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {

        String query = "SELECT * FROM users";
        try {

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(new User(resultSet.getString("username"), decrypt(resultSet.getString("password"),SHIFT_VALUE), resultSet.getString("photoPath"), resultSet.getString("bio")));
            }
            return users;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<User>();
    }

    @Override
    public Optional<User> save(User entity) {
        String query = "INSERT INTO users (username, password, photoPath, bio) VALUES (?, ?, ?, ?)";
        try {

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, entity.getUsername());
            statement.setString(2, encrypt(entity.getPassword(),SHIFT_VALUE));
            statement.setString(3, entity.getPhotoPath());
            statement.setString(4, entity.getBio());
            statement.execute();

            return Optional.of(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> delete(String s) {
        String query = "DELETE FROM users WHERE username = ?";

        Optional<User> user = findOne(s);
        if (user.isPresent()) {
            try {

                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, s);
                statement.executeUpdate();
                return user;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> update(User entity) {
        String query = "UPDATE users SET password = ? WHERE username = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, entity.getPassword());
            statement.setString(2, entity.getUsername());
            int affectedRows = statement.executeUpdate();


            if (affectedRows > 0) {
                return Optional.of(entity);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Page<User> findAllOnPage(Pageable pageable,String user) {
        return null;
    }

    public static String encrypt(String text, int shift) {
        StringBuilder encrypted = new StringBuilder();
        for (char ch : text.toCharArray()) {
            if (Character.isLetter(ch)) {
                char base = Character.isUpperCase(ch) ? 'A' : 'a';
                encrypted.append((char) ((ch - base + shift) % 26 + base));
            } else {
                encrypted.append(ch);
            }
        }
        return encrypted.toString();
    }

    public static String decrypt(String text, int shift) {
        StringBuilder decrypted = new StringBuilder();
        for (char ch : text.toCharArray()) {
            if (Character.isLetter(ch)) {
                char base = Character.isUpperCase(ch) ? 'A' : 'a';
                decrypted.append((char) ((ch - base - shift + 26) % 26 + base));
            } else {
                decrypted.append(ch);
            }
        }
        return decrypted.toString();
    }
}
