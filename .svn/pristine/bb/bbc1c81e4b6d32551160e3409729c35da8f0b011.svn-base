package com.aviv_pos.olgats.avivitemquery.acync;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.aviv_pos.olgats.avivitemquery.LoginActivity;
import com.aviv_pos.olgats.avivitemquery.R;
import com.aviv_pos.olgats.avivitemquery.WSConstants;
import com.aviv_pos.olgats.avivitemquery.beans.Account;
import com.aviv_pos.olgats.avivitemquery.beans.KeyValueSet;
import com.aviv_pos.olgats.avivitemquery.beans.Session;
import com.aviv_pos.olgats.avivitemquery.beans.Store;
import com.aviv_pos.olgats.avivitemquery.beans.StoreSet;
import com.aviv_pos.olgats.avivitemquery.dao.DatabaseHandler;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;


/**
 * Created by olgats on 11/04/2016.
 */
public class InitTask  extends AsyncTask<String, String, Map<String, Object>> {
    private static final String TAG = "AAInitTask";
    public static final String RESPONSE = "response";
    public static final String RESPONSE_MSG = "response_msg";

    private LoginActivity mContext;
    private String mActionTag;
    private ProgressDialog progress;

    public InitTask(LoginActivity context, String action) {
        mContext = context;
        mActionTag = action;
    }


    @Override
    protected Map<String, Object> doInBackground(String... params) {
        String user = params.length > 0 ? params[0] : null;
        String pwd = params.length > 1 ? params[1] : null;
        Map<String, Object> map = new TreeMap<>();
        if (user != null && pwd != null) {
            try {
                HttpClient client = new DefaultHttpClient();

                HttpGet request = new HttpGet(WSConstants.URL + "/inventory/verify/" + user + "/" + pwd);
                publishProgress("Verifying...");
                HttpResponse httpResponse = client.execute(request);
                StatusLine sl = httpResponse.getStatusLine();
                int sc = sl.getStatusCode();
                if (sc == 200) {
                    HttpEntity httpEntity = httpResponse.getEntity();
                    String response = EntityUtils.toString(httpEntity, "UTF-8");
                    Account account = new Gson().fromJson(response, Account.class);
                    int status = account.getStatus();
                    if (status == WSConstants.SUCCESS) {

                        publishProgress("Get chain info ...");
                        request = new HttpGet(WSConstants.URL + "/account/getStores/" + account.getId());
                        request.getParams().setParameter("http.socket.timeout", WSConstants.HTTP_TIMEOUT);
                        httpResponse = client.execute(request);
                        sl = httpResponse.getStatusLine();
                        sc = sl.getStatusCode();
                        if (sc == 200 && !isCancelled()) {
                            httpEntity = httpResponse.getEntity();
                            response = EntityUtils.toString(httpEntity, "UTF-8");
                            StoreSet set = new Gson().fromJson(response, StoreSet.class);
                            status = set.getStatus();
                            if (status == WSConstants.SUCCESS) {
                                Store[] stores= set.getSet();
                                DatabaseHandler.Stores storesDAO=new DatabaseHandler.Stores(mContext);
                                storesDAO.init(stores);
                                DatabaseHandler.Settings settings = new DatabaseHandler.Settings(mContext);
                                settings.replaceValue("storesLoaded", true);
                            }
                        }
                        status = initMasterTable(account, client, "getGroupsSet", new DatabaseHandler.Groups(mContext));
                        if (status == WSConstants.SUCCESS) {
                            status = initMasterTable(account, client, "getDepartmentsSet", new DatabaseHandler.Departments(mContext));
                        }
                        if (status == WSConstants.SUCCESS) {
                            status = initMasterTable(account, client, "getSuppliersSet", new DatabaseHandler.Suppliers(mContext));
                        }
                        if (status == WSConstants.SUCCESS) {
                            status = initMasterTable(account, client, "getUnitSet", new DatabaseHandler.Units(mContext));
                        }
                    }
                    String msg = "";
                    switch (status) {
                        case WSConstants.SUCCESS:
                            publishProgress("Open session...");
                            request = new HttpGet(WSConstants.URL + "/account/openItemSession/" + account.getId() + "?avivId=" + account.getAvivId() + "&counterPwd=" + "1");
                            request.getParams().setParameter("http.socket.timeout", WSConstants.HTTP_TIMEOUT);
                            httpResponse = client.execute(request);
                            sl = httpResponse.getStatusLine();
                            sc = sl.getStatusCode();
                            if (sc == 200 && !isCancelled()) {
                                httpEntity = httpResponse.getEntity();
                                response = EntityUtils.toString(httpEntity, "UTF-8");
                                Session session = new Gson().fromJson(response, Session.class);
                                DatabaseHandler.Settings settings = new DatabaseHandler.Settings(mContext);
                                settings.replaceValue("avivId", account.getAvivId());
                                settings.replaceValue("account", account.getId());
                                settings.replaceValue("session", session.getId());
                            } else {
                                status = WSConstants.APP_ERROR;
                                msg = mContext.getString(R.string.get_info_error);
                            }
                            break;
                        case WSConstants.ACCESS_DENIED:
                            msg = mContext.getString(R.string.access_denied);
                            break;
                        case WSConstants.ACCOUNT_NOT_EXISTS:
                            msg = mContext.getString(R.string.account_not_exists);
                            break;
                        case WSConstants.CLIENT_CONNECTION_ERROR:
                            msg = mContext.getString(R.string.client_db_connection_error);
                            break;
                        default:
                            msg = mContext.getString(R.string.get_info_error);
                            break;
                    }
                    map.put(RESPONSE, status);
                    map.put(RESPONSE_MSG, msg);


                } else {
                    map.put(RESPONSE, WSConstants.APP_ERROR);
                    map.put(RESPONSE_MSG, mContext.getString(R.string.get_info_error));
                }

            } catch (java.net.SocketTimeoutException e) {
                Log.e(TAG, e.getMessage(), e);
                map.put(RESPONSE, WSConstants.APP_ERROR);
                map.put(RESPONSE_MSG, mContext.getString(R.string.socket_timeout));
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
                map.put(RESPONSE, WSConstants.APP_ERROR);
                map.put(RESPONSE_MSG, mContext.getString(R.string.get_info_error));
            }
        }
        return map;
    }

    private int initMasterTable(Account account, HttpClient client, String method, DatabaseHandler.Master master) throws IOException {
        publishProgress("Init "+master.getName()+"...");
        HttpGet request = new HttpGet(WSConstants.URL + "/account/" + method + "/" + account.getId() + "?avivId=" + account.getAvivId());
        request.getParams().setParameter("http.socket.timeout", WSConstants.HTTP_TIMEOUT);
        HttpResponse httpResponse = client.execute(request);
        StatusLine sl = httpResponse.getStatusLine();
        int sc = sl.getStatusCode();
        int status = WSConstants.APP_ERROR;
        if (sc == 200 && !isCancelled()) {
            HttpEntity httpEntity = httpResponse.getEntity();
            String response = EntityUtils.toString(httpEntity, "UTF-8");
            KeyValueSet set = new Gson().fromJson(response, KeyValueSet.class);
            status = set.getStatus();
            if (status == WSConstants.SUCCESS && master.setValue(set)) {
                status = WSConstants.SUCCESS;

            }
        }
        return status;
    }

    private int initRules(Account account, HttpClient client, String method, DatabaseHandler.Rules rules) throws IOException {
        publishProgress("Init rules...");
        HttpGet request = new HttpGet(WSConstants.URL + "/account/rules/" + account.getId() + "?avivId=" + account.getAvivId());
        request.getParams().setParameter("http.socket.timeout", WSConstants.HTTP_TIMEOUT);
        HttpResponse httpResponse = client.execute(request);
        StatusLine sl = httpResponse.getStatusLine();
        int sc = sl.getStatusCode();
        int status = WSConstants.APP_ERROR;
        if (sc == 200 && !isCancelled()) {
            HttpEntity httpEntity = httpResponse.getEntity();
            String response = EntityUtils.toString(httpEntity, "UTF-8");
            KeyValueSet set = new Gson().fromJson(response, KeyValueSet.class);
            status = set.getStatus();
         /*   if (status == WSConstants.SUCCESS && rules.init(set.getSet())) {
                status = WSConstants.SUCCESS;

            }*/
        }
        return status;
    }


    @Override
    protected void onPostExecute(Map<String, Object> result) {
        if (!isCancelled()) {
            Intent intent = new Intent(mActionTag);
            if (result != null) {
                intent.putExtra(RESPONSE, (Integer) result.get(RESPONSE));
                intent.putExtra(RESPONSE_MSG, (String) result.get(RESPONSE_MSG));
            } else
                intent.putExtra(RESPONSE, -1);
            // broadcast the completion
            mContext.sendBroadcast(intent);
        }
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        if (mContext != null && values != null && values.length > 0) {
            mContext.setProgressMessage(values[0]);
        }
    }
}

