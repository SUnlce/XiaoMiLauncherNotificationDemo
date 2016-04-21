package com.sexyuncle.xiaomilaunchernotificationdemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Field;

/**
 * Created by dev-sexyuncle on 16/4/21.
 */
public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    private static final String XIAOMI_MANUFACTURER="Xiaomi";//小米硬件标示


    /**
     *
     * @param context
     * @param count 未读消息数目
     */
    public static void sendMiuiAppUnreadNotificationNumber(Context context,int count) {
        if (Build.MANUFACTURER.equals(XIAOMI_MANUFACTURER)) {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(context);
            builder.setContentTitle("你有" + count + "条未读消息");
            builder.setTicker("你有" + count + "条未读消息");
            builder.setAutoCancel(true);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setDefaults(Notification.DEFAULT_LIGHTS);
            Notification notification  = builder.getNotification();
            try {
                //miui6以上
                Class clazz = Class.forName("android.app.MiuiNotification");
                if (clazz != null) {
                    Object miuiNotification = clazz.newInstance();
                    Field messageCount = clazz.getDeclaredField("messageCount");
                    messageCount.setAccessible(true);
                    messageCount.set(miuiNotification, count);
                    Field extraNotification = notification.getClass().getField("extraNotification");
                    extraNotification.setAccessible(true);
                    extraNotification.set(notification, miuiNotification);

                }

            } catch (Exception e) {
                //miui6以下
                e.printStackTrace();
                Intent localIntent = new Intent(
                        "android.intent.action.APPLICATION_MESSAGE_UPDATE");
                localIntent.putExtra(
                        "android.intent.extra.update_application_component_name",
                        context.getPackageName() + "/" + getLauncherClassName(context));
                localIntent.putExtra(
                        "android.intent.extra.update_application_message_text", String.valueOf(count == 0 ? "" : count));
                context.sendBroadcast(localIntent);
            }finally {
                manager.notify(0, notification);
            }
        }else {
            Toast.makeText(context,"这不是小米的手机",Toast.LENGTH_LONG).show();
        }
    }

    /**
     *
     * @param context 上下文
     * @return 返回类名
     */
    private static String getLauncherClassName(Context context) {
        PackageManager packageManager = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setPackage(context.getPackageName());
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        ResolveInfo info = packageManager
                .resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);

        if (info == null) {
            info = packageManager.resolveActivity(intent, 0);
        }

        return info.activityInfo.name;
    }


}
