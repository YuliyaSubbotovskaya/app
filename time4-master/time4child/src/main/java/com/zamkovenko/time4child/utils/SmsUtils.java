package com.zamkovenko.time4child.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * User: Yevgeniy Zamkovenko
 * Date: 15.05.2018
 */
public class SmsUtils {
    private static final String PARAM_SIM_CARD = "sim_card";

    public static void sendSMS(String phoneNo, String msg, Context context) {
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            int simId = prefs.getInt(SmsUtils.PARAM_SIM_CARD, -1);

            if (simId != -1) {
                sendSMS(context, simId, phoneNo, null, msg, null, null);
            } else {
                Log.d("SmsUtils", "simId != -1");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static boolean sendSMS(final Context ctx, int simID, String toNum, String centerNum, String smsText, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        Log.d("sendSMS", "try to send sms from simID " + simID);
        try {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                SubscriptionManager subscriptionManager = SubscriptionManager.from(ctx);

                List<SubscriptionInfo> subscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();
                for (SubscriptionInfo subscriptionInfo : subscriptionInfoList) {
                    int subscriptionId = subscriptionInfo.getSubscriptionId();
                    int simSlotIndex = subscriptionInfo.getSimSlotIndex();
                    Log.d("apipas", "subscriptionId:" + subscriptionId + ", simSlotIndex: " + simSlotIndex);

                    if (simID == simSlotIndex) {
                        SmsManager.getSmsManagerForSubscriptionId(subscriptionId).sendTextMessage(toNum, centerNum, smsText,sentIntent, deliveryIntent);
                        System.out.println("sms is OK");
                    }
                }
            } else {
                String name;
                if (simID == 0) {
                    name = "isms";
                    // for model : "Philips T939" name = "isms0"
                } else if (simID == 1) {
                    name = "isms2";
                } else {
                    throw new Exception("can not get service which for sim '" + simID + "', only 0,1 accepted as values");
                }

                Log.d("sendSMS", "name: " + name);

                Method method = Class.forName("android.os.ServiceManager").getDeclaredMethod("getService", String.class);
                method.setAccessible(true);
                Object param = method.invoke(null, name);

                method = Class.forName("com.android.internal.telephony.ISms$Stub").getDeclaredMethod("asInterface", IBinder.class);
                method.setAccessible(true);
                Object stubObj = method.invoke(null, param);
                if (Build.VERSION.SDK_INT < 18) {
                    method = stubObj.getClass().getMethod("sendText", String.class, String.class, String.class, PendingIntent.class, PendingIntent.class);
                    method.invoke(stubObj, toNum, centerNum, smsText, sentIntent, deliveryIntent);
                } else {
                    method = stubObj.getClass().getMethod("sendText", String.class, String.class, String.class, String.class, PendingIntent.class, PendingIntent.class);
                    method.invoke(stubObj, ctx.getPackageName(), toNum, centerNum, smsText, sentIntent, deliveryIntent);
                }

                System.out.println("sms is OK");

                return true;
            }

        } catch (ClassNotFoundException e) {
            Log.e("apipas", "ClassNotFoundException:" + e.getMessage());
        } catch (NoSuchMethodException e) {
            Log.e("apipas", "NoSuchMethodException:" + e.getMessage());
        } catch (InvocationTargetException e) {
            Log.e("apipas", "InvocationTargetException:" + e.getMessage());
        } catch (IllegalAccessException e) {
            Log.e("apipas", "IllegalAccessException:" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return false;
    }
}
