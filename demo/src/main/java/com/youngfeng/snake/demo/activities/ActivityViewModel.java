package com.youngfeng.snake.demo.activities;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.youngfeng.snake.demo.utils.Event;

/**
 * Activity ViewModel implementation.
 *
 * @author Scott Smith 2019-11-23 12:01
 */
public class ActivityViewModel extends ViewModel {
    private MutableLiveData<Boolean> _dragToCloseEnable = new MutableLiveData<>(true);
    public LiveData<Boolean> dragToCloseEnable = _dragToCloseEnable;

    private MutableLiveData<String> _dragToCloseEnableStatusText = new MutableLiveData<>("");
    public LiveData<String> dragToCloseEnableStatusText = _dragToCloseEnableStatusText;

    private MutableLiveData<Event<Boolean>> _goToNextEvent = new MutableLiveData<>();
    public LiveData<Event<Boolean>> goToNextEvent = _goToNextEvent;

    public void enableDragToClose(boolean enable) {
        _dragToCloseEnable.setValue(enable);
    }

    public void setDragToCloseStatusText(String text) {
        _dragToCloseEnableStatusText.setValue(text);
    }

    public void goToNextActivity() {
        _goToNextEvent.setValue(new Event<>(true));
    }
}
