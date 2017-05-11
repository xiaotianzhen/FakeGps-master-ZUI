package name.caiyao.fakegps.util;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class NetUtils {

    static ConnectivityManager connectivity;
    //Wi-Fi
    public static final int NETTYPE_WIFI = 0;
    //无网络
    public static final int NETTYPE_NONE = 1;
    //2G
    public static final int NETTYPE_2G = 2;
    //3G
    public static final int NETTYPE_3G = 3;
    //4G
    public static final int NETTYPE_4G = 4;
    public static Intent intent;

    public static boolean check(Context context) {
        try {
            connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                // 获取网络连接管理的对象
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    // 判断当前网络是否已经连接
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        //判断网络类型
                        isNetworkAvailable(context);
                        return true;
                    }
                } else {
                    //若网络未连接，则弹出提示进行设置
                    Log.i("520it", "" + "***********  弹出提示进行设置  ***************");
                    setNetWork(context);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void isNetworkAvailable(Context context) {

        NetworkInfo.State gprs = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        NetworkInfo.State wifi = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();

        if (gprs == NetworkInfo.State.CONNECTED || gprs == NetworkInfo.State.CONNECTING) {
            Log.i("520it", "" + "***********  gprs 已开启***************");
        }

        if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
            Log.i("520it", "" + "*************  wifi  *************");
        }

        //判断网络类型

        switch (getNetworkType(context)) {
            case NETTYPE_WIFI:
                //Toast.makeText(context, "wifi", Toast.LENGTH_SHORT).show();
                break;
            case NETTYPE_2G:
               // Toast.makeText(context, "2G", Toast.LENGTH_SHORT).show();
                break;
            case NETTYPE_3G:
               // Toast.makeText(context, "3G", Toast.LENGTH_SHORT).show();
                break;
            case NETTYPE_4G:
                //Toast.makeText(context, "4G", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    private static int getNetworkType(Context context) {

        connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = connectivity.getActiveNetworkInfo();

        if (networkinfo != null && networkinfo.isConnected()) {
            String type = networkinfo.getTypeName();
            if (type.equalsIgnoreCase("wifi")) {
                return NETTYPE_WIFI;
            } else if (type.equalsIgnoreCase("MOBILE")) {
                NetworkInfo mobileInfo = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                if(mobileInfo!=null){
                    switch (mobileInfo.getType()) {
                        case ConnectivityManager.TYPE_MOBILE:// 手机网络
                            switch (mobileInfo.getSubtype()) {
                                //--------------------Added in API level 1---------------------
                                //(3G)联通  ~ 400-7000 kbps
                                case TelephonyManager.NETWORK_TYPE_UMTS:
                                    //(2.5G) 移动和联通  ~ 100 kbps
                                case TelephonyManager.NETWORK_TYPE_GPRS:
                                    //(2.75G) 2.5G 到 3G 的过渡  移动和联通 ~ 50-100 kbps
                                case TelephonyManager.NETWORK_TYPE_EDGE:
                                    //-----------------Added in API level 4---------------------
                                    //( 3G )电信   ~ 400-1000 kbps
                                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                                    //(2G 电信)  ~ 14-64 kbps
                                case TelephonyManager.NETWORK_TYPE_CDMA:
                                    //(3.5G) 属于3G过渡   ~ 600-1400 kbps
                                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                                    //( 2G )  ~ 50-100 kbps
                                case TelephonyManager.NETWORK_TYPE_1xRTT:
                                    //---------------------Added in API level 5--------------------
                                    //(3.5G )  ~ 2-14 Mbps
                                case TelephonyManager.NETWORK_TYPE_HSDPA:
                                    //( 3.5G )  ~ 1-23 Mbps
                                case TelephonyManager.NETWORK_TYPE_HSUPA:
                                    //( 3G ) 联通  ~ 700-1700 kbps
                                case TelephonyManager.NETWORK_TYPE_HSPA:
                                    //---------------------Added in API level 8---------------------
                                    //(2G )  ~25 kbps
                                case TelephonyManager.NETWORK_TYPE_IDEN:
                                    return NETTYPE_2G;
                                //---------------------Added in API level 9---------------------
                                //3G-3.5G  ~ 5 Mbps
                                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                                    //---------------------Added in API level 11--------------------
                                    //(4G) ~ 10+ Mbps
                                case TelephonyManager.NETWORK_TYPE_LTE:
                                    return NETTYPE_4G;
                                //3G(3G到4G的升级产物)  ~ 1-2 Mbps
                                case TelephonyManager.NETWORK_TYPE_EHRPD:
                                    //--------------------Added in API level 13-------------------
                                    //( 3G )  ~ 10-20 Mbps
                                case TelephonyManager.NETWORK_TYPE_HSPAP:
                                    return NETTYPE_3G;
                                //无网络
                                default:
                                    return NETTYPE_NONE;
                            }
                    }
                }
            }


        }
        return NETTYPE_NONE;
    }

    public static void setNetWork(final Context context) {

         new AlertDialog.Builder(context).setTitle("网络连接提示").setMessage("网络不可用，如需继续，请设置网络")
                 .setPositiveButton("确定", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialogInterface, int i) {

                 if(Build.VERSION.SDK_INT>10){
                     intent=new Intent(Settings.ACTION_WIFI_SETTINGS);
                 }else {
                     intent=new Intent();
                     ComponentName componentName=new ComponentName( "com.android.settings",
                             "com.android.settings.WirelessSettings");
                     intent.setComponent(componentName);
                     intent.setAction("android.intent.action.view");
                 }

                 context.startActivity(intent);

             }
         }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialogInterface, int i) {

             }
         }).show();
    }
}
