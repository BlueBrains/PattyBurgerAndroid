package com.bluebrains.receiver;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.bluebrains.activity.MainActivity;
import com.bluebrains.helper.NotificationUtils;
import com.pusher.client.Pusher;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Molham on 7/24/2015.
 */
public class PusherReciver extends IntentService{
    private final String TAG = CustomPushReceiver.class.getSimpleName();
    public static final String  APP_KEY = "83192374ad73924c1bd2";
    public static final String  CHANNAL = "test-push";
    public static final String  EVENT = "test-event";
    private static Context mContext;
    private NotificationUtils notificationUtils;

    public PusherReciver() {
        super("TEST");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        init();
    }

    @Override
    public void setIntentRedelivery(boolean enabled) {
        super.setIntentRedelivery(enabled);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        String dataString = workIntent.getDataString();

        // Do work here, based on the contents of dataString

    }
    void init(){
        Pusher pusher = new Pusher(APP_KEY);

        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                System.out.println("State changed to " + change.getCurrentState() +
                        " from " + change.getPreviousState());
            }

            @Override
            public void onError(String message, String code, Exception e) {
                System.out.println("There was a problem connecting!");
            }
        }, ConnectionState.ALL);

        // Subscribe to a channel
        Channel channel = pusher.subscribe(CHANNAL);

        // Bind to listen for events called "my-event" sent to "my-channel"
        channel.bind(EVENT, new SubscriptionEventListener() {
            @Override
            public void onEvent(String channel, String event, String data) {
                System.out.println("Received event with data: " + data);
                try {
                    JSONObject json = new JSONObject(data);
                    PusherPushJson(mContext,new JSONObject(json.getString("message")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    void PusherPushJson(Context context, JSONObject json){
        try {
            boolean isBackground = json.getBoolean("is_background");
            String type = json.getString("type");
            if(type=="confirmation")
            {

            }else if(type=="order_state"){

            }else if(type=="advertisement"){

            }
            JSONObject data = json.getJSONObject("data");
            String title = data.getString("title");
            String message = data.getString("message");

            if (!isBackground) {
                Intent resultIntent = new Intent(context, MainActivity.class);
                showNotificationMessage(context, title, message, resultIntent);
            }

        } catch (JSONException e) {
            Log.e(TAG, "Push message json exception: " + e.getMessage());
        }
    }
    private void showNotificationMessage(Context context, String title, String message, Intent intent) {

        notificationUtils = new NotificationUtils(context);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        notificationUtils.showNotificationMessage(title, message, intent);
    }

}
