package com.zamkovenko.time4parent.Utils;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Yevgeniy Zamkovenko
 * Date: 16.06.2018
 */
public class SimUtils {

    public static final String PARAM_SIM_CARD = "sim_card";

    public static void WriteToSharedPreferences(Context context, int simId) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putInt(PARAM_SIM_CARD, simId).apply();
    }

    public static boolean IsSimChosen(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int simId = prefs.getInt(PARAM_SIM_CARD, -1);
        return simId != -1;
    }

    public static List<String> getNetworkOperator(final Context context) {
        List<String> carrierNames = new ArrayList<>();
        try {
            final String permission = Manifest.permission.READ_PHONE_STATE;
            if ( (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) && (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) ){
                final List<SubscriptionInfo> subscriptionInfos = SubscriptionManager.from(context).getActiveSubscriptionInfoList();
                for (int i = 0; i < subscriptionInfos.size(); i++) {
                    carrierNames.add(subscriptionInfos.get(i).getCarrierName().toString());
                }

            } else {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                // Get carrier name (Network Operator Name)
                carrierNames.add(telephonyManager.getNetworkOperatorName());
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return carrierNames;
    }

}
