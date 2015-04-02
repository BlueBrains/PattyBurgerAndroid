package com.bluebrains.pattyburger;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Molham on 3/30/2015.
 */

enum DownloadStatus { IDLE, PROCESSING, NOT_INITIALISED, FAILED_OR_EMPTY, OK}
public class GetRowData {
    private String LOG_TAG = GetRowData.class.getName();
    private String mRowUrl;
    private String mData;
    public static String GET = "GET";
    public static String POST = "POST";
    private String mMethod;
    private DownloadStatus mDownloadStatus;

    public GetRowData (String mRowUrl, String method){
        this.mRowUrl = mRowUrl;
        this.mMethod = method;
    }

    public void reset (){
        this.mDownloadStatus = DownloadStatus.IDLE;
        this.mRowUrl = null;
        this.mData = null;
    }
    // get fun
    public String getmRowUrl() {
        return mRowUrl;
    }

    public String getmData() {
        return mData;
    }

    public DownloadStatus getmDownloadStatus() {
        return mDownloadStatus;
    }
    //*/

    // set fun
    public void setmRowUrl(String mRowUrl) {
        this.mRowUrl = mRowUrl;
    }

    public void setmData(String mData) {
        this.mData = mData;
    }
    //*/

    public void execute (){
        this.mDownloadStatus = DownloadStatus.PROCESSING;
        DownloadRowData  downloadRowData = new DownloadRowData();
        downloadRowData.execute(mRowUrl);
    }
    public class DownloadRowData extends AsyncTask<String, Void, String> {

        protected void onPostExecute(String webData){
            mData = webData;
            Log.v(LOG_TAG, "Data returned was: "+mData);
            if(mData == null){
                if(mRowUrl == null){
                    mDownloadStatus = DownloadStatus.NOT_INITIALISED;
                }else{
                    mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
                }
            }else{
                //Success
                mDownloadStatus = DownloadStatus.OK;
            }
        }

        protected String doInBackground(String... params){
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            if(params == null){
                return null;
            }
            try{
                URL url = new URL(params[0]);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod(mMethod);
                urlConnection.connect();
                if(mMethod == GET){
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();

                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;

                    while ((line=reader.readLine())!= null){
                        buffer.append(line + "\n");
                    }

                    return buffer.toString();
                }else {
                    return null;
                }
            }catch(IOException e){
                Log.e(LOG_TAG, "Error", e);
                return null;
            }finally {
                if(urlConnection!=null){
                    urlConnection.disconnect();
                }
                if(reader != null){
                    try{
                        reader.close();
                    }catch (final IOException e){
                        Log.e(LOG_TAG, "Error Closing Stream",e);
                    }
                }
            }
        }
    }
}
