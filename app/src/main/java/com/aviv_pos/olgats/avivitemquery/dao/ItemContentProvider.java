package com.aviv_pos.olgats.avivitemquery.dao;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by olgats on 15/12/2015.
 */
public class ItemContentProvider extends ContentProvider {
    private final static String TAG = "ItemContentProvider";
    private DAOHelper dbHelper;

    private static final String BASE_PATH_ITEM = "items";
    private static final String AUTHORITY = "com.aviv_pos.olgats.avivitemquery.dao.daocontentprovider";
    public static final Uri CONTENT_ITEM_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_ITEM);

    private static final int ITEM = 100;
    private static final int ITEMS = 101;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(AUTHORITY, BASE_PATH_ITEM, ITEMS);
        URI_MATCHER.addURI(AUTHORITY, BASE_PATH_ITEM + "/#", ITEM);

    }

    public ItemContentProvider() {
        super();
    }


    private void checkColumns(String[] projection, String[] columns) {
        if (projection != null) {
            HashSet<String> request = new HashSet<>(Arrays.asList(projection));
            HashSet<String> available = new HashSet<>(Arrays.asList(columns));
            if (!available.containsAll(request)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }

    @Override
    public boolean onCreate() {
        Log.d(TAG, "accessed onCreate method");
        dbHelper = new DAOHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(BASE_PATH_ITEM);
        checkColumns(projection, DAOHelper.ITEMS.COLUMNS);

        int type = URI_MATCHER.match(uri);
        switch (type) {
            case ITEMS:
                //there is not to do if the query is for the table
                break;
            case ITEM:
                queryBuilder.appendWhere(DAOHelper.ITEMS.BARCODE + " = '" + uri.getLastPathSegment() + "'");
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //Cursor cursor = db.rawQuery("SELECT * FROM notes",selectionArgs);
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        Context c = getContext();
        if (c != null) {
            cursor.setNotificationUri(c.getContentResolver(), uri);
        }
        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        int type = URI_MATCHER.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Long id;
        switch (type) {
            case ITEMS:
                id = db.insert(DAOHelper.ITEMS.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        notifyContext(uri);
        return Uri.parse(BASE_PATH_ITEM + "/" + id);
    }


    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int type = URI_MATCHER.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int affectedRows;
        switch (type) {
            case ITEMS:
                affectedRows = db.delete(DAOHelper.ITEMS.TABLE_NAME, selection, selectionArgs);
                break;
            case ITEM:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    affectedRows = db.delete(DAOHelper.ITEMS.TABLE_NAME, DAOHelper.ITEMS.BARCODE + "='" + id + "'", null);
                } else {
                    affectedRows = db.delete(DAOHelper.ITEMS.TABLE_NAME, DAOHelper.ITEMS.BARCODE + "='" + id + "' AND " + selection, selectionArgs);
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        notifyContext(uri);
        return affectedRows;
    }


    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int type = URI_MATCHER.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int affectedRows;
        switch (type) {
            case ITEMS:
                affectedRows = db.update(DAOHelper.ITEMS.TABLE_NAME, values, selection, selectionArgs);
                break;

            case ITEM:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    affectedRows = db.update(DAOHelper.ITEMS.TABLE_NAME, values, DAOHelper.ITEMS.BARCODE + "='" + id + "'", null);
                } else {
                    affectedRows = db.update(DAOHelper.ITEMS.TABLE_NAME, values, DAOHelper.ITEMS.BARCODE + "='" + id + "' AND " + selection, selectionArgs);
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        notifyContext(uri);
        return affectedRows;
    }

    private void notifyContext(@NonNull Uri uri) {
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
    }

}
