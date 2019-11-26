package com.youngfeng.snake.demo.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.youngfeng.snake.demo.utils.Event;

/**
 * Main ViewModel class.
 *
 * @author Scott Smith 2019-11-23 11:05
 */
public class MainViewModel extends ViewModel {
    private final MutableLiveData<Event<Boolean>> _openUseCaseInActivityEvent = new MutableLiveData<>();
    public final LiveData<Event<Boolean>> openUseCaseInActivityEvent = _openUseCaseInActivityEvent;

    private final MutableLiveData<Event<Boolean>> _openUseCaseInAndroidXFrgEvent = new MutableLiveData<>();
    public final LiveData<Event<Boolean>> openUseCaseInAndroidXFrgEvent = _openUseCaseInAndroidXFrgEvent;

    private final MutableLiveData<Event<Boolean>> _openUseInheritEvent = new MutableLiveData<>();
    public final LiveData<Event<Boolean>> openUseInheritEvent = _openUseInheritEvent;

    private final MutableLiveData<Event<Boolean>> _copyQQNumberEvent = new MutableLiveData<>();
    public final LiveData<Event<Boolean>> copyQQNumberEvent = _copyQQNumberEvent;

    private final MutableLiveData<Event<Boolean>> _startGitRepoEvent = new MutableLiveData<>();
    public final LiveData<Event<Boolean>> startGitRepoEvent = _startGitRepoEvent;

    public void openUseCaseInActivity() {
        _openUseCaseInActivityEvent.setValue(new Event<>(true));
    }

    public void openUseCaseInAndroidXFrgEvent() {
        _openUseCaseInAndroidXFrgEvent.setValue(new Event<>(true));
    }

    public void openUseInheritEvent() {
        _openUseInheritEvent.setValue(new Event<>(true));
    }

    public void startGitRepo() {
        _startGitRepoEvent.setValue(new Event<>(true));
    }

    public void copyQQNumber() {
        _copyQQNumberEvent.setValue(new Event<>(true));
    }
}
