package com.firebase.test.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.firebase.test.activity.LoginActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by DSK on 2/19/2016.
 */
public class SessionManager {
    public static final String KEY_USER_OBJECT = "UserObj";
    public static final String NOTIFICATION_LIST = "notiList";
    public static final String CATALOGUE_LIST = "catList";
    // Sharedpref file name
    private static final String PREF_NAME = "Pref";
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String USER_MOB_NO = "mobileNumber";
    private static final String NOTI_STATUS = "notiStatus";
    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Constructor
    public SessionManager(Context context) {
        {
            try {
                this._context = context;
                pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
                editor = pref.edit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void storeMobileNumber(String mobNumber) {
        editor.putString(USER_MOB_NO, mobNumber);
        editor.commit();
    }

    public String getMobileNumber() {
        return pref.getString(USER_MOB_NO, "");
    }

    public void storeNotificationStatus(boolean status) {
        editor.putBoolean(NOTI_STATUS, status);
        editor.commit();
    }

    public Boolean getNotificationStatus() {
        return pref.getBoolean(NOTI_STATUS, true);
    }

    public void storeCatList(ArrayList<String> list) {
        String strList = new Gson().toJson(list);
        editor.putString(CATALOGUE_LIST, strList);
        editor.commit();
    }

    public ArrayList<String> getCatList() {
        String strList = pref.getString(CATALOGUE_LIST, "");
        ArrayList<String> pageList = new Gson().fromJson(strList, new TypeToken<List<String>>() {
        }.getType());
        return pageList;
    }

    public void storeList(ArrayList<String> list) {
        String strList = new Gson().toJson(list);
        editor.putString(NOTIFICATION_LIST, strList);
        editor.commit();
    }

    public ArrayList<String> getList() {
        String strList = pref.getString(NOTIFICATION_LIST, "");
        ArrayList<String> pageList = new Gson().fromJson(strList, new TypeToken<List<String>>() {
        }.getType());
        return pageList;
    }

    /**
     * Create login session
     */


    //==========================================
    public void createLoginSession() {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        editor.commit();
    }

//    public void storeUserModel(UserModel userModel) {
//        Gson gson = new Gson();
//        String json = gson.toJson(userModel); // myObject - instance of MyObject
//        editor.putString(KEY_USER_OBJECT, json);
//        editor.commit();
//    }
//
//    public UserModel getUserModel() {
//        Gson gson = new Gson();
//        String json = pref.getString(KEY_USER_OBJECT, "");
//        UserModel userModel = gson.fromJson(json, UserModel.class);
//        return userModel;
//    }


    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        clearData();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);

    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void clearData() {
        editor.clear();
        editor.commit();
    }
}
