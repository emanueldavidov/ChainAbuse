package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages event subscribers and notifications.
 */
public class EventDispatcher {

    private Map<String, List<EventSubscriber>> eventSubscribers = new HashMap<>();

    public EventDispatcher(String... eventTypes) {
        for (String eventType : eventTypes) {
            eventSubscribers.put(eventType, new ArrayList<>());
        }
    }

    public void subscribe(String eventType, EventSubscriber subscriber) {
        eventSubscribers.get(eventType).add(subscriber);
    }

    public void unsubscribe(String eventType, EventSubscriber subscriber) {
        eventSubscribers.get(eventType).remove(subscriber);
    }

    public void notify(String eventType, Object eventData) {
        for (EventSubscriber subscriber : eventSubscribers.get(eventType)) {
            subscriber.onEvent(eventType, eventData);
        }
    }
}
