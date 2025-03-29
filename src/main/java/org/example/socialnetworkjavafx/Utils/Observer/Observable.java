package org.example.socialnetworkjavafx.Utils.Observer;

import org.example.socialnetworkjavafx.Utils.Event.Event;

public interface Observable <E extends Event> {
    void addObserver(Observer<E> observer);
    void removeObserver(Observer<E> observer);
    void notifyObservers(E event);
}
