package com.example.avinash.listview;

/**
 * Created by rshinde on 10/21/15.
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.SQLException;




import java.util.ArrayList;

public class DbHandler extends SQLiteOpenHelper{

    public  SQLiteDatabase SQLdbObj;
    public static final String DATABASE_NAME = "myStock.db";
    private static final int DATABASE_VERSION=1;
    public static final String TABLE_NAME = "LiveQuote";

    //Table Columns names
    private static final String LiveQuote_Comp_Nm = "Comp_Nm ";

    public DbHandler(Context context) {
        super(context, DATABASE_NAME, null,DATABASE_VERSION);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        System.out.println("DROP TABLE IF EXISTS executed---------------------->>>>>" + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        String CREATE_LiveQuote_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + LiveQuote_Comp_Nm + " TEXT PRIMARY KEY" + ")";

        db.execSQL(CREATE_LiveQuote_TABLE);

        System.out.println("Table created" + TABLE_NAME);

        // db.execSQL(
        // "CREATE TABLE LiveQuote" + "(Comp_Nm TEXT PRIMARY KEY NOT NULL," + "Comp_Fnm TEXT NOT NULL, " + "Quote_time TEXT NOT NULL, " + "Main_valTxtV1 TEXT NOT NULL, " + "Change_valTxtV2 TEXT NOT NULL, " + "PerChange_valTxtV3 TEXT NOT NULL");
    }

    public boolean dbf_initRows() {
        if (dbf_numberOfRows() == 0) {

            System.out.println("Inside dbf_initRows");



            SQLiteDatabase db = this.getWritableDatabase();

            db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('AAPL');");
            db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('AMD');");
            db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('AMZN');");
            db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('FB');");
            db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('GOOG');");
            db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('INTC');");
            db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('MSFT');");
            db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('QCOM');");

            System.out.println("Inside dbf_initRows executed successfully");

            db.close();
            return true;

        } else {
            return false;
        }
    }
    //Getting total Count
    public int dbf_numberOfRows() {

        /*String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(countQuery, null);

        cursor.close();
        return res.getCount();*/

        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        db.close();
        return numRows;
    }

    public ArrayList<String> dbf_getAllRecords() {
        ArrayList<String> lv_list = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                lv_list.add(cursor.getString(cursor.getColumnIndex("Comp_Nm")));
            } while (cursor.moveToNext());
        }
        db.close();

        return lv_list;
    }


    public void dbf_deletePart(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE Comp_Nm = '" + name + "'");
        db.close();
    }
    public void dbf_appendPart(String NewCompNm) {
        //int last = dbf_getLastRowValue();

        System.out.println("\n\nInside DbHandler dbf_appendPart --------------->>>> " + NewCompNm);

        System.out.println("INSERT INTO "+ TABLE_NAME + "(Comp_nm) VALUES('"+NewCompNm+"');");

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO "+ TABLE_NAME + "(Comp_nm) VALUES('"+NewCompNm+"');");



                //  db.execSQL("insert into your table name (order_id,doc_id,doc_name)" + "values("ordid+","+doci+","+ "\"" + docnam + "\"" + ") ;");
                //String sql1 = "insert into " +tablename+ " (" +CONTENT1+ ", " +CONTENT2+ ") values(" +uid+  ",'" +pwd+ "')";
        //db.execSQL(sql1);
                db.close();
    }

    public int dbf_getLastRowValue () {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from " + TABLE_NAME, null);
        res.moveToLast();

        String parts = res.getString(res.getColumnIndex("Comp_Nm"));
        db.close();
        return Integer.parseInt(parts.substring(5));
    }



}
