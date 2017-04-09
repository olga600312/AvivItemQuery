package com.aviv_pos.olgats.avivitemquery.acync;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.aviv_pos.olgats.avivitemquery.WSConstants;
import com.aviv_pos.olgats.avivitemquery.beans.Result;
import com.aviv_pos.olgats.avivitemquery.dao.DatabaseHandler;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by olgats on 30/06/2016.
 */
public class TransactionDataTask extends AsyncTask<String, Void, Result> {
    private static final String TAG = "AATransactionDataTask";
    public static final String RESPONSE = "response";
    private Context mContext;
    private Listener listener;

    public TransactionDataTask(Context context, Listener listener) {
        mContext = context;
        this.listener = listener;

    }


    @Override
    protected Result doInBackground(String... params) {
        String code = params.length > 0 ? params[0] : null;
        String fromDate = params.length > 2 ? params[1] : "0";
        String toDate = params.length > 3 ? params[2] : "" + System.currentTimeMillis();
        String type = params.length > 4 ? params[3] : "" + WSConstants.TR_SALE;
        Result result;
        if (code != null) {
            try {
                DatabaseHandler.Settings settings = new DatabaseHandler.Settings(mContext);
                String str = settings.getValue("account");
                if (str != null) {
                    int accountId = Integer.parseInt(str);
                    String avivId = settings.getValue("avivId");
                    String session = settings.getValue("session");
                    List<NameValuePair> httpParams = new LinkedList<>();
                    httpParams.add(new BasicNameValuePair("avivId", avivId));
                    httpParams.add(new BasicNameValuePair("code", code));
                    httpParams.add(new BasicNameValuePair("type", type));
                    httpParams.add(new BasicNameValuePair("fromDate", fromDate));
                    httpParams.add(new BasicNameValuePair("toDate", toDate));
                    String paramString = URLEncodedUtils.format(httpParams, "UTF-8");
                    String szUrl = WSConstants.URL + "/session/getTransactionData/" + accountId + "/" + session + "?" + paramString;
                    HttpClient client = new DefaultHttpClient();
                    HttpGet request = new HttpGet(szUrl);
                    request.getParams().setParameter("http.socket.timeout", WSConstants.HTTP_TIMEOUT);
                    HttpResponse httpResponse = client.execute(request);
                    StatusLine sl = httpResponse.getStatusLine();
                    int sc = sl.getStatusCode();

                    if (sc == 200) {
                        HttpEntity httpEntity = httpResponse.getEntity();
                        String response = EntityUtils.toString(httpEntity, "UTF-8");
                        result = new Gson().fromJson(response, Result.class);
                    } else {
                        result = new Result(WSConstants.APP_ERROR);
                    }
                } else {
                    result = new Result(WSConstants.ACCOUNT_NOT_EXISTS);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
                result = new Result(WSConstants.APP_ERROR);
            }
        } else {
            result = new Result(WSConstants.APP_ERROR);
        }

        return result;
    }


    @Override
    protected void onPostExecute(Result result) {
        if (listener != null) {
            listener.onResult(result, isCancelled());
        }
    }

    public interface Listener {
        void onResult(Result result, boolean isCanceled);
    }
}
