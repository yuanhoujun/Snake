package me.foji.demo;

import com.squareup.leakcanary.LeakCanary;

/**
 * Application
 *
 * @author Scott Smith 2017-05-03 21:45
 */
public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
