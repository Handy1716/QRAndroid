package com.example.beadando1;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Adatbazis extends SQLiteOpenHelper {

    public Adatbazis(Context context) {
        super(context, "qr", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE scanned_codes ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "content TEXT,"
                + "ip_address TEXT,"
                + "timestamp TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS scanned_codes");
        onCreate(db);
    }

    public void ujQRcodeFelvitel(String content, String ip_address)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("content", content);
        values.put("ip_address", ip_address);
        values.put("timestamp", sdf.format(new Date()));
        db.insert("scanned_codes", null, values);
        db.close();
    }

}
