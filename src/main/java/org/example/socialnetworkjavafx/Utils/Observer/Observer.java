package org.example.socialnetworkjavafx.Utils.Observer;

import org.example.socialnetworkjavafx.Utils.Event.Event;

public interface Observer <E extends Event> {
    void update(E event);
}
