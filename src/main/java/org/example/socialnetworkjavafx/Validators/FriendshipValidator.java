package org.example.socialnetworkjavafx.Validators;

import org.example.socialnetworkjavafx.Entities.Friendship;

public class FriendshipValidator implements Validator<Friendship> {

    @Override
    public void validate(Friendship entity) throws ValidationException {
        if ("".equals(entity.getUser1()))
            throw new ValidationException("User1 is empty");
        if ("".equals(entity.getUser2()))
            throw new ValidationException("User2 is empty");
    }
}
