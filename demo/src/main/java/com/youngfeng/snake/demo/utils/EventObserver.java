package com.youngfeng.snake.demo.utils;

import androidx.lifecycle.Observer;

/**
 * Event observer.
 *
 * @author Scott Smith 2019-11-23 11:29
 */
public class EventObserver<T> implements Observer<Event<T>> {
    private OnEventHandler<T> onEventHandler;

    public EventObserver(OnEventHandler<T> onEventHandler) {
        this.onEventHandler = onEventHandler;
    }

    @Override
    public void onChanged(Event<T> event) {
        T content = event.getContentIfNotHandled();
        if (null != content) {
            if (null != this.onEventHandler) {
                this.onEventHandler.onEventUnhandledContent(content);
            }
        }
    }
}
