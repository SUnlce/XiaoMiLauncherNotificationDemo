package com.sexyuncle.xiaomilaunchernotificationdemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import java.lang.reflect.Field;

/**
 * Created by dev-sexyuncle on 16/4/21.
 */
public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    public static void getFiledOfClass(Context context,int count){
        try {
            //miui6以上
            Notification notification = null;
            Notification.Builder builder = new Notification.Builder(context);
            builder.setContentTitle("你有"+count+"条未读消息");
            builder.setTicker("你有"+count+"条未读消息");
            builder.setAutoCancel(true);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setDefaults(Notification.DEFAULT_LIGHTS);
            notification = builder.build();
            Class clazz = Class.forName("android.app.MiuiNotification");
            if (clazz!=null){
                Object miuiNotification = clazz.newInstance();
                Field messageCount = clazz.getDeclaredField("messageCount");
                messageCount.setAccessible(true);
                messageCount.set(miuiNotification,count);
                Field extraNotification= notification.getClass().getField("extraNotification");
                extraNotification.setAccessible(true);
                extraNotification.set(notification, miuiNotification);
                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(0,notification);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
