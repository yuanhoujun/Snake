package com.youngfeng.snake.demo.androidx;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.youngfeng.snake.demo.utils.Event;

/**
 * Fragment ViewModel implementation.
 *
 * @author Scott Smith 2019-11-23 12:53
 */
public class FragmentViewModel extends ViewModel {
    private MutableLiveData<Event<Boolean>> _goToSecondFrgEvent = new MutableLiveData<>();
    LiveData<Event<Boolean>> goToSecondFrgEvent = _goToSecondFrgEvent;

    private MutableLiveData<Event<Boolean>> _goToThirdFrgEvent = new MutableLiveData<>();
    LiveData<Event<Boolean>> goToThirdFrgEvent = _goToThirdFrgEvent;

    private MutableLiveData<Boolean> _dragToCloseEnable = new MutableLiveData<>(true);
    public LiveData<Boolean> dragToCloseEnable = _dragToCloseEnable;

    private MutableLiveData<String> _dragToCloseEnableStatusText = new MutableLiveData<>("");
    public LiveData<String> dragToCloseEnableStatusText = _dragToCloseEnableStatusText;

    private MutableLiveData<String> _titleText = new MutableLiveData<>();
    public LiveData<String> titleText = _titleText;

    public void goToSecondFrag() {
        _goToSecondFrgEvent.setValue(new Event<>(true));
    }

    public void goToThirdFrag() {
        _goToThirdFrgEvent.setValue(new Event<>(true));
    }

    public void enableDragToClose(boolean enable) {
        _dragToCloseEnable.setValue(enable);
    }

    public void setDragToCloseStatusText(String text) {
        _dragToCloseEnableStatusText.setValue(text);
    }

    public void setTitle(String title) {
        _titleText.setValue(title);
    }
}
