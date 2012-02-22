package net.incredibles.brtaquiz.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author shaiekh
 * @Created 2/22/12 12:08 PM
 */
public class ForeGroundServiceCompat {
    private static final Class<?>[] setForegroundMethodSignature = {boolean.class};
    private static final Class<?>[] startForegroundMethodSignature = {int.class, Notification.class};
    private static final Class<?>[] stopForegroundMethodSignature = {boolean.class};

    private Object[] setForegroundMethodArgs = new Object[1];
    private Object[] startForegroundMethodArgs = new Object[2];
    private Object[] stopForegroundMethodArgs = new Object[1];

    private Method startForegroundMethod;
    private Method setForegroundMethod;
    private Method stopForegroundMethod;

    private NotificationManager notificationManager;
    private Service service;

    public ForeGroundServiceCompat(Service service) {
        this.service = service;
        notificationManager = (NotificationManager) service.getSystemService(Context.NOTIFICATION_SERVICE);
        setStartStopAndSetForegroundMethods(service);
    }

    private void setStartStopAndSetForegroundMethods(Service service) {
        try {
            startForegroundMethod = service.getClass().getMethod("startForeground", startForegroundMethodSignature);
            stopForegroundMethod = service.getClass().getMethod("stopForeground", stopForegroundMethodSignature);
        } catch (NoSuchMethodException ignore) {
            // Running on an older platform
        }

        try {
            setForegroundMethod = service.getClass().getMethod("setForeground", setForegroundMethodSignature);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("OS doesn't have Service.startForeground OR Service.setForeground!");
        }
    }

    public void startForeground(int id, Notification notification) {
        if (startForegroundMethod != null) {
            startForegroundMethodArgs[0] = id;
            startForegroundMethodArgs[1] = notification;
            invokeMethod(startForegroundMethod, startForegroundMethodArgs);
            return;
        }

        setForegroundMethodArgs[0] = Boolean.TRUE;
        invokeMethod(setForegroundMethod, setForegroundMethodArgs);

        notificationManager.notify(id, notification);
    }

    private void invokeMethod(Method method, Object[] args) {
        try {
            method.invoke(service, args);
        } catch (InvocationTargetException e) {
            Log.w("ApiDemos", "Unable to invoke method", e);
        } catch (IllegalAccessException e) {
            Log.w("ApiDemos", "Unable to invoke method", e);
        }
    }

    public void stopForeground(int id) {
        if (stopForegroundMethod != null) {
            stopForegroundMethodArgs[0] = Boolean.TRUE;
            invokeMethod(stopForegroundMethod, stopForegroundMethodArgs);
            return;
        }

        setForegroundMethodArgs[0] = Boolean.FALSE;
        invokeMethod(setForegroundMethod, setForegroundMethodArgs);

        notificationManager.cancel(id);
    }

}
