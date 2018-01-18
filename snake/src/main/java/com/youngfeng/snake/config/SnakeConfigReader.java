package com.youngfeng.snake.config;

import android.annotation.SuppressLint;
import android.app.Application;
import android.graphics.Color;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Snake config reader.
 *
 * @author Scott Smith 2017-12-19 08:41
 */
public class SnakeConfigReader {
    private Application mApplication;
    @SuppressLint("StaticFieldLeak")
    private static SnakeConfigReader instance;
    private SnakeConfig mSnakeConfig = new SnakeConfig();

    private SnakeConfigReader() {
    }

    public static synchronized SnakeConfigReader get() {
        if(null == instance) {
            instance = new SnakeConfigReader();
        }

        return instance;
    }

    public void init(Application application) {
        mApplication = application;

        try {
            InputStream inputStream = application.getAssets().open("snake.xml");
            if(null != inputStream) {
                readSnakeConfigIn(inputStream);
            }
        } catch (IOException e) {
            // ignore this exception.
        }
    }

    private void readSnakeConfigIn(InputStream inputStream) {
        if(null == inputStream) return;

        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, "utf-8");
            parser.nextTag();
            parser.nextTag();
            parser.require(XmlPullParser.START_TAG, null, "config");

            while(XmlPullParser.END_TAG != parser.next()) {
                if(XmlPullParser.START_TAG != parser.getEventType()) {
                    continue;
                }

                String name = parser.getName();

                parser.require(XmlPullParser.START_TAG, null, name);
                String tagValue = readText(parser);

                switch (name) {
                    case SnakeConfig.TAG_ENABLE_FOR_ROOT_ACTIVITY: {
                        if(!"true".equals(tagValue) && !"false".equals(tagValue)) {
                            throw new SnakeConfigException("The tag " + name + " only allows the use of boolean values. eg: true or false, current value: " + tagValue);
                        }
                        mSnakeConfig.enableForRootActivity = Boolean.parseBoolean(tagValue);
                        break;
                    }
                    case SnakeConfig.TAG_ONLY_LISTEN_TO_FAST_SWIPE: {
                        if(!"true".equals(tagValue) && !"false".equals(tagValue)) {
                            throw new SnakeConfigException("The tag " + name + " only allows the use of boolean values. eg: true or false, current value: " + tagValue);
                        }
                        mSnakeConfig.onlyListenToFastSwipe = Boolean.parseBoolean(tagValue);
                        break;
                    }
                    case SnakeConfig.TAG_MIN_VELOCITY: {
                        try {
                            mSnakeConfig.minVelocity = Integer.parseInt(tagValue);
                        } catch (NumberFormatException e) {
                            throw new SnakeConfigException("The tag " + name + " only allows the use of integer values, current value: " + tagValue);
                        }
                        break;
                    }
                    case SnakeConfig.TAG_HIDE_SHADOW_OF_EDGE: {
                        if(!"true".equals(tagValue) && !"false".equals(tagValue)) {
                            throw new SnakeConfigException("The tag " + name + " only allows the use of boolean values. eg: true or false, current value: " + tagValue);
                        }
                        mSnakeConfig.hideShadowOfEdge = Boolean.parseBoolean(tagValue);
                        break;
                    }
                    case SnakeConfig.TAG_SHADOW_START_COLOR: {
                        try {
                            mSnakeConfig.shadowStartColor = Color.parseColor(tagValue);
                        } catch (IllegalArgumentException e) {
                            throw new SnakeConfigException("The tag " + name + " only allows the use of color string, eg: #ff0000, current value: " + tagValue);
                        }
                        break;
                    }
                    case SnakeConfig.TAG_SHADOW_END_COLOR: {
                        try {
                            mSnakeConfig.shadowEndColor = Color.parseColor(tagValue);
                        } catch (IllegalArgumentException e) {
                            throw new SnakeConfigException("The tag " + name + " only allows the use of color string, eg: #ff0000, current value: " + tagValue);
                        }
                        break;
                    }
                    case SnakeConfig.TAG_ENABLE_SWIPE_UP_TO_HOME: {
                        if(!"true".equals(tagValue) && !"false".equals(tagValue)) {
                            throw new SnakeConfigException("The tag " + name + " only allows the use of boolean values. eg: true or false, current value: " + tagValue);
                        }
                        mSnakeConfig.enableSwipeUpToHome = Boolean.parseBoolean(tagValue);
                        break;
                    }
                    case SnakeConfig.TAG_ALLOW_PAGE_LINKAGE: {
                        if(!"true".equals(tagValue) && !"false".equals(tagValue)) {
                            throw new SnakeConfigException("The tag " + name + " only allows the use of boolean values. eg: true or false, current value: " + tagValue);
                        }
                        mSnakeConfig.allowPageLinkage = Boolean.parseBoolean(tagValue);
                        break;
                    }
                }
                parser.require(XmlPullParser.END_TAG, null, name);
             }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if(parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }

        return result;
    }

    public boolean enableForRootActivity() {
        return mSnakeConfig.enableForRootActivity;
    }

    public boolean onlyListenToFastSwipe() {
        return mSnakeConfig.onlyListenToFastSwipe;
    }

    public int minVelocity() {
        return mSnakeConfig.minVelocity;
    }

    public boolean hideShadowOfEdge() {
        return mSnakeConfig.hideShadowOfEdge;
    }

    public int shadowStartColor() {
        return mSnakeConfig.shadowStartColor;
    }

    public int shadowEndColor() {
        return mSnakeConfig.shadowEndColor;
    }

    public boolean swipeUpToHomeEnabled() { return mSnakeConfig.enableSwipeUpToHome; }

    public boolean allowPageLinkage() { return mSnakeConfig.allowPageLinkage; }
}
