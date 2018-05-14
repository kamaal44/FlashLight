package com.flash.light;

import java.util.List;
import java.util.ArrayList;
 
import android.content.Context;
import android.content.ContentValues;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
 
  private static final int DATABASE_VERSION = 1;
 
  private static final String KEY_ID        = "id";
  private static final String KEY_HIDE      = "hide";

  private static final String TABLE_OPTIONS = "options";
  private static final String DATABASE_NAME = "FlashLight";
 
  public DatabaseHandler(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }
 
  @Override
  public void onCreate(SQLiteDatabase db) {
    String CREATE_OPTIONS_TABLE = "CREATE TABLE " + TABLE_OPTIONS + "("
      + KEY_ID + " INTEGER PRIMARY KEY," 
      + KEY_HIDE + " TEXT" + ")";
    db.execSQL(CREATE_OPTIONS_TABLE);
  }
 
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_OPTIONS);
    onCreate(db);
  }

  void addFlashLightDatabase(FlashLightDatabase flashLightDatabase) {
    SQLiteDatabase db = this.getWritableDatabase();
 
    ContentValues values = new ContentValues();
    values.put(KEY_HIDE, flashLightDatabase.getHide()); 
 
    db.insert(TABLE_OPTIONS, null, values);
    db.close(); 
  }
 
  FlashLightDatabase getFlashLightDatabase(int id) {
    SQLiteDatabase db = this.getReadableDatabase();
 
    Cursor cursor = db.query(TABLE_OPTIONS, 
      new String[] { KEY_ID, KEY_HIDE }, KEY_ID + "=?",
      new String[] { String.valueOf(id) }, null, null, null, null);
      if (cursor != null) {
        cursor.moveToFirst();
      }
 
      FlashLightDatabase flashLightDatabase = new FlashLightDatabase(Integer.parseInt(cursor.getString(0)),
        cursor.getString(1));

      return flashLightDatabase;
  }
     
  public List<FlashLightDatabase> getAllFlashLightDatabase() {

    List<FlashLightDatabase> flashLightDatabaseList = new ArrayList<FlashLightDatabase>();

    String selectQuery = "SELECT  * FROM " + TABLE_OPTIONS;
 
    SQLiteDatabase db  = this.getWritableDatabase();
    Cursor cursor      = db.rawQuery(selectQuery, null);
 
    if (cursor.moveToFirst()) {
      do {
        FlashLightDatabase flashLightDatabase = new FlashLightDatabase();
        flashLightDatabase.setID(Integer.parseInt(cursor.getString(0)));
        flashLightDatabase.setHide(cursor.getString(1));
        flashLightDatabaseList.add(flashLightDatabase);
      } while (cursor.moveToNext());
    }
 
    return flashLightDatabaseList;
  }
 
  public int updateFlashLightDatabase(FlashLightDatabase flashLightDatabase) {
    SQLiteDatabase db    = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(KEY_HIDE, flashLightDatabase.getHide());
 
    return db.update(TABLE_OPTIONS, values, KEY_ID + " = ?",
      new String[] { String.valueOf(flashLightDatabase.getID()) });
  }
 
  public void deleteFlashLightDatabase(FlashLightDatabase flashLightDatabase) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete(TABLE_OPTIONS, KEY_ID + " = ?",
      new String[] { String.valueOf(flashLightDatabase.getID()) });
    db.close();
  }

  public int getFlashLightDatabaseCount() {
    String countQuery = "SELECT  * FROM " + TABLE_OPTIONS;
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(countQuery, null);
    cursor.close();
 
    return cursor.getCount();
  }
 
}