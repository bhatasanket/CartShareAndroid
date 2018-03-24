package com.example.it354f701.CartShare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by it354F701 on 10/22/2017.
 */

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "SQLiteDatabase.db";
    private static final String TABLE_NAME = "LoginInfo";
    private static final String COLUMN_USERID = "USERID";
    private static final String COLUMN_FIRST_NAME = "FIRST_NAME";
    private static final String COLUMN_LAST_NAME = "LAST_NAME";
    private static final String COLUMN_PASSWORD = "PASSWORD";
    private static final String COLUMN_EMAIL = "EMAIL";
    private static final String COLUMN_SEC_QS = "SEC_QS";
    private static final String COLUMN_SEC_ANS = "SEC_ANS";
    private static final String[] allColumns = {
            COLUMN_USERID,
            COLUMN_FIRST_NAME,
            COLUMN_LAST_NAME,
            COLUMN_PASSWORD,
            COLUMN_EMAIL,
            COLUMN_SEC_QS,
            COLUMN_SEC_ANS
    };

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " ( " +
                COLUMN_USERID + " VARCHAR PRIMARY KEY," +
                COLUMN_FIRST_NAME + " VARCHAR, " +
                COLUMN_LAST_NAME + " VARCHAR, " +
                COLUMN_PASSWORD + " VARCHAR, " +
                COLUMN_EMAIL + " VARCHAR, " +
                COLUMN_SEC_QS + " VARCHAR, " +
                COLUMN_SEC_ANS + " VARCHAR" +
                ");");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    private SQLiteDatabase database;

    public void insertRecord(AccountBean accountBean) {
//        SQLiteDatabase database1 = this.getReadableDatabase();
//        database1.delete(TABLE_NAME,null,null);
//        database1.close();
        database = this.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_FIRST_NAME, accountBean.getFirstName());
        contentValues.put(COLUMN_LAST_NAME, accountBean.getLastName());
        contentValues.put(COLUMN_PASSWORD, accountBean.getPassword());
        contentValues.put(COLUMN_EMAIL, accountBean.getEmail());
        System.out.println(accountBean);
        database.insert(TABLE_NAME, null, contentValues);
        database.close();
    }

    public AccountBean getUserDetails(String userID) {
        database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, allColumns, COLUMN_USERID + " = '" + userID + "'", null, null, null, null);
        ArrayList<AccountBean> beanArrayList = new ArrayList<>();

        AccountBean accountBean=null;
        if (cursor.getCount() > 0) {
            accountBean = new AccountBean();
            cursor.moveToNext();
            accountBean.setFirstName(cursor.getString(1));
            accountBean.setLastName(cursor.getString(2));
            accountBean.setPassword(cursor.getString(3));
            accountBean.setEmail(cursor.getString(4));
        }

        cursor.close();

        return accountBean;
    }


//
//    public List<AccountBean> getAllComments() {
//        List<AccountBean> comments = new ArrayList<AccountBean>();
//        database = this.getReadableDatabase();
//
//        Cursor cursor = database.query(TABLE_NAME,
//                allColumns, null, null, null, null, null);
//
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            Comment comment = cursorToComment(cursor);
//            comments.add(comment);
//            cursor.moveToNext();
//        }
//        // Make sure to close the cursor
//        cursor.close();
//        return comments;
//
//    }
}
