package com.bluebrains.helper;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.bluebrains.app.AppConfig;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.SaveCallback;

import java.util.List;

/**
 * Created by Molham on 7/7/2015.
 */
public class ParseUtils {
    private static String TAG = ParseUtils.class.getSimpleName();

    public static void verifyParseConfiguration(Context context) {
        if (TextUtils.isEmpty(AppConfig.PARSE_APPLICATION_ID) || TextUtils.isEmpty(AppConfig.PARSE_CLIENT_KEY)) {
            Toast.makeText(context, "Please configure your Parse Application ID and Client Key in AppConfig.java", Toast.LENGTH_LONG).show();
            ((Activity) context).finish();
        }
    }

    public static void registerParse(Context context) {
        // initializing parse library
        Parse.initialize(context, AppConfig.PARSE_APPLICATION_ID, AppConfig.PARSE_CLIENT_KEY);
        ParseInstallation.getCurrentInstallation().saveInBackground();

        ParsePush.subscribeInBackground(AppConfig.PARSE_CHANNEL, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.e(TAG, "Successfully subscribed to Parse!");
            }
        });
    }

    public static void subscribeWithUniqueId(Integer unique_id) {
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("unique_id", unique_id+"");
        installation.saveInBackground();
    }
    public static boolean IsSubscribedTo(String res_name) {
        List<Object> channels = getSubscribedList();
        for(int i = 0; i<channels.size(); i++){
            if(channels.get(i).toString().equals(res_name))
                return true;
        }
        return false;
    }
    public static List<Object> getSubscribedList() {
        return ParseInstallation.getCurrentInstallation().getList("channels");
    }
    public static void subscribeWithRes(final String res_name) {
        ParsePush.subscribeInBackground(res_name, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.e(TAG, "Successfully subscribed to " + res_name);
            }
        });
    }
    public static void unSubscribeWithRes(final String res_name) {
        ParsePush.unsubscribeInBackground(res_name, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.e(TAG, "Successfully subscribed to " + res_name);
            }
        });
    }
}
