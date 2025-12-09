package org.example.eventpublisher.listener;

public abstract class EventHandler<T> {
    public void handleEvent(T event) {
        // Common pre-processing logic can go here
        System.out.println("Handling event: " + event.toString());
        process(event);
        // Common post-processing logic can go here
        System.out.println("Event handled successfully.");
    }

    protected abstract void process(T event);
}
