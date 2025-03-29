package org.example.socialnetworkjavafx.Infrastructure;

import org.example.socialnetworkjavafx.Entities.User;
import org.example.socialnetworkjavafx.Factories.UserFactory;
import org.example.socialnetworkjavafx.Repositories.AbstractFileRepository;
import org.example.socialnetworkjavafx.Validators.Validator;

import java.io.*;
import java.util.Optional;

public class UserRepository extends AbstractFileRepository<String, User> {

    UserFactory userFactory;

    public UserRepository(String filePath, Validator validator) {
        super(filePath, validator);
        userFactory = UserFactory.getInstance();
        try {
            readFromFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void readFromFile() throws FileNotFoundException {
        String line;
        String delimiter = ",";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(delimiter);

                String uname = values[0];
                String pass = values[1];
                String photoPath = values[2];
                String bio = values[3];
                User user = userFactory.createUser(uname, pass,photoPath,bio);

                super.save(user);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void writeToFile() {
        try (FileWriter fw = new FileWriter(filePath)) {
            Iterable<User> users = findAll();
            users.forEach(user -> {
                try {
                    fw.write(user.getUsername() + ',' + user.getPassword() + '\n');
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<User> update(User entity) {
        super.update(entity);
        writeToFile();
        return Optional.of(entity);
    }

    @Override
    public Optional<User> delete(String o) {
        Optional<User> oDu = super.delete(o);
        if (oDu.isPresent()) {
            writeToFile();
            return Optional.of(oDu.get());
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> save(User entity) {
        super.save(entity);
        writeToFile();
        return Optional.of(entity);
    }

    @Override
    public Iterable<User> findAll() {
        return super.findAll();
    }

    @Override
    public Optional<User> findOne(String o) {
        return super.findOne(o);
    }

}
