package org.example.socialnetworkjavafx.Validators;

import org.example.socialnetworkjavafx.Entities.Message;

public class MessageValidator implements Validator<Message> {

    @Override
    public void validate(Message entity) throws ValidationException {
        if ("".equals(entity.getFrom())) {
            throw new ValidationException("Empty sender");
        }
        if ("".equals(entity.getTo())) {
            throw new ValidationException("Empty receiver");
        }
        if ("".equals(entity.getContent())) {
            throw new ValidationException("Empty message");
        }
    }
}
