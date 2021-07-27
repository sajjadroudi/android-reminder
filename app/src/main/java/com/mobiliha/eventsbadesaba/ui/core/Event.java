package com.mobiliha.eventsbadesaba.ui.core;

public class Event<T> {

    private final T content;
    private boolean hasBeenHandled = false;

    public Event(T content) {
        this.content = content;
    }

    /**
     * Returns the content and prevents its use again.
     */
    public T getContentIfNotHandled() {
        if(hasBeenHandled)
            return null;
        hasBeenHandled = true;
        return content;
    }

    /**
     * If the content is not null then the callback will be called.
     */
    public void handleIfNotNull(Consumer<T> callback) {
        T result = getContentIfNotHandled();
        if(result != null)
            callback.accept(result);
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    public T peekContent() {
        return content;
    }

}