package com.bluebrains.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
* Created by Molham on 5/20/2015.
*/
public class SessionManager {
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "AndroidHiveLogin";

    private static final String IS_LOGGEDIN = "isResgisterd";

    private static final String KEY_UID = "uid";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(Integer id) {

        editor.putBoolean(IS_LOGGEDIN, true);

        // Storing email in pref
        editor.putInt(KEY_UID, id);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }
    public void logoutSession(){
        editor.putBoolean(IS_LOGGEDIN, false);

        // Storing email in pref
        editor.putString(KEY_UID, "");

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }
    public Integer getId() {
        return pref.getInt(KEY_UID, 0);
    }

    public boolean isResgisterd(){
        return pref.getBoolean(IS_LOGGEDIN, false);
    }
}

