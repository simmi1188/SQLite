package com.example.sqlite;

public class student {
    private int ID;
    private String Name;
    private String Email;
    private String Phone_No;
    private String Date;
    private String Marks;

    public int getID() {
        return ID;
    }

    public void setID(int id) {
        ID = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone_No() {
        return Phone_No;
    }

    public void setPhone_No(String phone_no) {
        Phone_No = phone_no;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getMarks() {
        return Marks;
    }

    public void setMarks(String marks) {
        Marks = marks;
    }

    @Override
    public String toString() {
        return ID + " : " + Name + " : " + Date + " : " + Email + " : " + Phone_No + " : " + Marks;
    }



}
