package com.aviv_pos.olgats.avivitemquery.acync;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.aviv_pos.olgats.avivitemquery.WSConstants;
import com.aviv_pos.olgats.avivitemquery.beans.Item;
import com.aviv_pos.olgats.avivitemquery.dao.DatabaseHandler;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by olgats on 29/06/2016.
 */
public class ItemSaveTask extends AsyncTask<Item, Void, Map<String, Object>> {
    private static final String TAG = "AARestTask";
    public static final String RESPONSE = "response";
    public static final String RESPONSE_FIRST = "responseFirst";

    private Context mContext;
    private String mActionTag;


    public ItemSaveTask(Context context, String action) {
        mContext = context;
        mActionTag = action;
    }


    @Override
    protected Map<String, Object> doInBackground(Item... arr) {
        Item item = arr.length > 0 ? arr[0] : null;
        Map<String, Object> map = new TreeMap<>();
        if (item != null) {
            DatabaseHandler.Settings settings = new DatabaseHandler.Settings(mContext);
            String str = settings.getValue("account");
            int account = str != null ? Integer.parseInt(str) : -1;
            str = settings.getValue("session");
            int session = str != null ? Integer.parseInt(str) : -1;
            String avivId = settings.getValue("avivId");
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(WSConstants.URL + "/session/updateItemInfo/" + account + "/" + session + "?avivId=" + avivId + "&updateType=" + WSConstants.UPDATE_TYPE_REPLACE);
                httpPost.getParams().setParameter("http.socket.timeout", WSConstants.HTTP_TIMEOUT);
                httpPost.setEntity(new StringEntity(new Gson().toJson(item), "UTF-8"));
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                HttpResponse httpResponse = client.execute(httpPost);
                StatusLine sl = httpResponse.getStatusLine();
                int sc = sl.getStatusCode();
                switch (sc) {
                    case 200:
                    case 201:
                    case 202:
                        if (!isCancelled()) {
                            map.put(RESPONSE, WSConstants.SUCCESS);
                            HttpEntity httpEntity = httpResponse.getEntity();
                            String response = EntityUtils.toString(httpEntity, "UTF-8");
                            Item updatedItem = new Gson().fromJson(response, Item.class);
                            DatabaseHandler.Items items = new DatabaseHandler.Items(mContext);
                            updatedItem = items.replace(updatedItem) ? updatedItem : null;
                            Log.i(TAG, updatedItem != null ? updatedItem.toString() : "item update error " + item.getCode());
                            if (updatedItem != null) {
                                map.put(RESPONSE_FIRST, updatedItem.getCode());
                            }
                        }
                        break;
                    case 500:
                        Integer res;
                        try {
                            HttpEntity httpEntity = httpResponse.getEntity();
                            String response = EntityUtils.toString(httpEntity, "UTF-8");
                            res = new Gson().fromJson(response, Integer.class);
                        } catch (Exception ex) {
                            res = WSConstants.APP_ERROR;
                        }
                        map.put(RESPONSE, res);
                        break;
                    default:
                        map.put(RESPONSE, WSConstants.APP_ERROR);
                }


            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
        return map;
    }


    @Override
    protected void onPostExecute(Map<String, Object> result) {
        if (!isCancelled()) {
            Intent intent = new Intent(mActionTag);
            if (result != null) {
                intent.putExtra(RESPONSE, (Integer) result.get(RESPONSE));
                intent.putExtra(RESPONSE_FIRST, (String) result.get(RESPONSE_FIRST));
            } else
                intent.putExtra(RESPONSE, -1);
            // broadcast the completion
            mContext.sendBroadcast(intent);
        }
    }

}

