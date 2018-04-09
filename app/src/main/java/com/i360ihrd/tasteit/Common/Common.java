package com.i360ihrd.tasteit.Common;

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


    public static String convertCode(String status) {
        if(status.equals("0"))
            return "Placed";
        else if (status.equals("1"))
            return "On my way";
        else
            return "Shipped";
    }
}
