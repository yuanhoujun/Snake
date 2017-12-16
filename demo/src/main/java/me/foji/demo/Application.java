package me.foji.demo;

import android.support.multidex.MultiDexApplication;

import com.squareup.leakcanary.LeakCanary;
import com.youngfeng.annotations.EnableDragToClose;

/**
 * Application
 *
 * @author Scott Smith 2017-05-03 21:45
 */
public class Application extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
