package com.example.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private View view;
    public static final String DataBase_Name = "Register.db";
    public static final String Table_Name = "Student_register";
    public static final String col_1 = "ID";
    public static final String col_2 = "Name";
    public static final String col_3 = "Email";
    public static final String col_4 = "Phone_No";
    public static final String col_5 = "Date";
    public static final String col_6 = "Marks";

    private ArrayList<student> studentList;

    public static DBHelper dbHelper;

    public static DBHelper getDBHelper(Context context) {
        if (dbHelper == null) {
            dbHelper = new DBHelper(context);
        }
        return dbHelper;
    }

    private DBHelper(Context context) {
        super(context, DataBase_Name, null, 1);
        studentList = new ArrayList<student>();
        readStudents();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Student_register(ID INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT, Email TEXT, Password TEXT, Phone_No TEXT, Date TEXT, Marks INT DEFAULT '0')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + Table_Name);
        onCreate(db);

    }


    public ArrayList<student> getAllStudent() {
        return studentList;
    }

    private void readStudents() {
        String[] columns = {
                col_1, col_2, col_3, col_4, col_5, col_6
        };


        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Table_Name, columns, null, null, null, null, null);
        cursor.moveToFirst();

        if (cursor.moveToFirst()) {
            do {
                student student = new student();
                student.setID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(col_1))));
                student.setName(cursor.getString(cursor.getColumnIndex(col_2)));
                student.setEmail(cursor.getString(cursor.getColumnIndex(col_3)));
                student.setPhone_No(cursor.getString(cursor.getColumnIndex(col_4)));
                student.setDate(cursor.getString(cursor.getColumnIndex(col_5)));
                student.setMarks(cursor.getString(cursor.getColumnIndex(col_6)));
                studentList.add(student);

            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public void addStudent(student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(col_2, student.getName());
        values.put(col_3, student.getEmail());
        values.put(col_4, student.getPhone_No());
        values.put(col_5, student.getDate());
        values.put(col_6, student.getMarks());
        int id = (int) db.insert(Table_Name, null, values);
        student.setID(id);
        studentList.add(student);
        db.close();
    }

    public void deleteUser(student student) {

        SQLiteDatabase db = this.getWritableDatabase();
        int rowAffected = db.delete(Table_Name, col_1 + "=?", new String[]{String.valueOf(student.getID())});
        System.out.println("rowAffected in deleteUser : " + rowAffected);
        studentList.remove(student);
        db.close();
    }

    public void updateUser(student student) {
        System.out.println("student in updateUser : " + student.toString());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(col_2, student.getName());
        values.put(col_3, student.getEmail());
        values.put(col_4, student.getPhone_No());
        values.put(col_5, student.getDate());
        values.put(col_6, student.getMarks());
        int rowAffected = db.update(Table_Name, values, col_1 + "=?", new String[]{String.valueOf(student.getID())});
        System.out.println("rowAffected in updateUser : " + rowAffected);
        db.close();
    }

}
