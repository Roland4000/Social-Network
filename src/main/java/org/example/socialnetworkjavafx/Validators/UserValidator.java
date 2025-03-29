package org.example.socialnetworkjavafx.Validators;

import org.example.socialnetworkjavafx.Entities.User;

public class UserValidator implements Validator<User> {

    @Override
    public void validate(User entity) throws ValidationException {
        if ("".equals(entity.getUsername()))
            throw new ValidationException("Username cannot be empty");
    }
}
