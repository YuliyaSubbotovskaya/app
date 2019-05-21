package com.zamkovenko.time4parent.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zamkovenko.utils.model.Message;
import com.zamkovenko.utils.model.MessageState;

import java.util.ArrayList;
import java.util.Date;

/**
 * User: Yevgeniy Zamkovenko
 * Date: 03.03.2018
 */

public class ParentTaskDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tasks.db";

    public static final String TABLE_TASKS = "tasks";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_VIBRATION = "vibration";
    public static final String COLUMN_BLINKING = "blinking";

    public static final String DATABASE_DROP = "DROP TABLE IF EXISTS " + TABLE_TASKS;

    public ParentTaskDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_TASKS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TIME + " TEXT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_STATUS + " INTEGER, " +
                COLUMN_VIBRATION + " INTEGER, " +
                COLUMN_BLINKING + " INTEGER " +
        ");";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        recreateDb();
    }

    public int addTask(Message message) {
        ContentValues contentValues = GetContentValues(message);
        SQLiteDatabase writableDatabase = getWritableDatabase();
        final long id = writableDatabase.insert(TABLE_TASKS, null, contentValues);
        return (int) id;
    }

    public void updateTask(Message message) {
        ContentValues contentValues = GetContentValues(message);

        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.update(TABLE_TASKS, contentValues, COLUMN_ID+"="+message.getId(), null);
    }

    public ArrayList<Message> getAllData() {
//        recreateDb();

        SQLiteDatabase db = getWritableDatabase();
        ArrayList<Message> mArrayList = new ArrayList<>();

        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + TABLE_TASKS,null);

            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                Message.MessageBuilder messageBuilder = new Message()
                        .getBuilder()
                        .setId((short) cursor.getInt(cursor.getColumnIndex(COLUMN_ID)))
                        .setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)))
                        .setMessageState(cursor.getInt(cursor.getColumnIndex(COLUMN_STATUS)) == 1 ? MessageState.DONE : MessageState.NOT_DONE)
                        .setIsBlinking(cursor.getInt(cursor.getColumnIndex(COLUMN_BLINKING)) == 1)
                        .setIsVibro(cursor.getInt(cursor.getColumnIndex(COLUMN_VIBRATION)) == 1)
                        .setOnDate(new Date(cursor.getLong(cursor.getColumnIndex(COLUMN_TIME))));

                mArrayList.add(messageBuilder.build());
            }
            cursor.close();
        }

        return mArrayList;
    }

    public void deleteTask(final short _id) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.execSQL("DELETE FROM " + TABLE_TASKS + " WHERE " + COLUMN_ID + "=\"" + _id + "\";");
    }

    public void recreateDb() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(DATABASE_DROP);
        onCreate(db);
    }

    private ContentValues GetContentValues(Message message) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TIME, message.getOnDate().getTime());
        contentValues.put(COLUMN_TITLE, message.getTitle());
        contentValues.put(COLUMN_STATUS, message.IsDone());
        contentValues.put(COLUMN_BLINKING, message.isBlinking());
        contentValues.put(COLUMN_VIBRATION, message.isVibro());
        return contentValues;
    }
}
