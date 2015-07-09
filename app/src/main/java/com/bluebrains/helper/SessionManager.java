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

    private static final String IS_LOGGEDIN = "isLoggedIn";

    private static final String KEY_EMAIL = "email";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String email) {

        editor.putBoolean(IS_LOGGEDIN, true);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }
    public String getEmail() {
        return pref.getString(KEY_EMAIL, null);
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGGEDIN, false);
    }
}

