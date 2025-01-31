package server;

/**
 * Handles event updates.
 */
public interface EventSubscriber {
    void onEvent(String eventType, Object eventData);
}
