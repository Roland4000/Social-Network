package org.example.socialnetworkjavafx.Services;

import org.example.socialnetworkjavafx.Entities.Message;
import org.example.socialnetworkjavafx.Factories.MessageFactory;
import org.example.socialnetworkjavafx.Infrastructure.MessageDbRepository;
import org.example.socialnetworkjavafx.Repositories.Repository;
import org.example.socialnetworkjavafx.Utils.Event.MessageEntityChangeEvent;
import org.example.socialnetworkjavafx.Utils.Observer.Observable;
import org.example.socialnetworkjavafx.Utils.Observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class MessageService implements Observable<MessageEntityChangeEvent> {
    MessageFactory messageFactory;
    MessageDbRepository repository;
    List<Observer<MessageEntityChangeEvent>> observers = new ArrayList<>();

    public MessageService(MessageDbRepository messageRepository) {
        this.repository = messageRepository;
        messageFactory = MessageFactory.getInstance();
    }

    public List<Message> getAllMessagesOf(String username1, String username2) {
        return repository.findAllMessagesOf(username1, username2);
    }

    public void addMessage(Message message) {
        System.out.println(message);
        repository.save(message);
    }

    public void removeMessage(Integer message_id) {
        repository.delete(message_id);
    }

    @Override
    public void addObserver(Observer<MessageEntityChangeEvent> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<MessageEntityChangeEvent> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(MessageEntityChangeEvent event) {
        observers.forEach(observer -> observer.update(event));
    }
}
