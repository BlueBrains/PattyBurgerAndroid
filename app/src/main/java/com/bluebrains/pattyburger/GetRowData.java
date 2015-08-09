package com.bluebrains.pattyburger;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

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
                    urlConnection.setReadTimeout(10000);
                    urlConnection.setConnectTimeout(15000);
                    //conn.setRequestMethod("POST");
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);

                    List<NameValuePair> my_params = new ArrayList<NameValuePair>();
                    for(int i=1;i<=params.length;i+=2)
                    {
                        my_params.add(new BasicNameValuePair(params[i],params[i+1]));
                    }
                    /*
                    my_params.add(new BasicNameValuePair("firstParam", paramValue1));
                    my_params.add(new BasicNameValuePair("secondParam", paramValue2));
                    my_params.add(new BasicNameValuePair("thirdParam", paramValue3));*/

                    OutputStream os = urlConnection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getQuery(my_params));
                    writer.flush();
                    writer.close();
                    os.close();

                    urlConnection.connect();
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
        private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
        {
            StringBuilder result = new StringBuilder();
            boolean first = true;

            for (NameValuePair pair : params)
            {
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
            }

            return result.toString();
        }
    }
}
