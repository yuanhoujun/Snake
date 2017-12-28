package com.youngfeng.snake.util;

import android.util.Log;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Use to resolve listening to convert translucent completed
 *
 * @author Scott Smith 2017-12-18 13:49
 */
public class ConversionInvocationHandler implements InvocationHandler {
    private WeakReference<TranslucentConversionListener> mConversionListenerRef;

    public ConversionInvocationHandler(WeakReference<TranslucentConversionListener> conversionListenerRef) {
        mConversionListenerRef = conversionListenerRef;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            Log.e("Conversion >>>>>>", method.getName() + "<===>" + args[0]);
            boolean drawComplete = (boolean) args[0];

            if(null != mConversionListenerRef.get()) {
                mConversionListenerRef.get().onTranslucentConversionComplete(drawComplete);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
}
