package com.i360ihrd.tasteit.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.i360ihrd.tasteit.Model.Request;
import com.i360ihrd.tasteit.Model.User;

/**
 * Created by Jamshaid on 02-01-2018.
 */

public class Common {
    public static User currentUser;
    public  static Request currentRequest;

    public static final String UPDATE = "Update";
    public static final String DELETE = "Delete";
    public static final String CHANNEL_ID = "OrderUpdate";
    public static final String USER_KEY = "User";
    public static final String PWD_KEY  = "Password";

    public  static boolean isConnectTointernet(Context context){

        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager != null){
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if(info !=null){
                for (int i=0;i<info.length;i++){
                    if(info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }

        return false;

    }

    public static String convertCode(String status) {
        if(status.equals("0"))
            return "Placed";
        else if (status.equals("1"))
            return "On my way";
        else
            return "Shipped";
    }
}
