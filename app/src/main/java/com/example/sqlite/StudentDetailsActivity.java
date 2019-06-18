package com.example.sqlite;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Pattern;

public class StudentDetailsActivity extends AppCompatActivity {

    private TextInputEditText editTextid;
    private TextInputEditText editTextname;
    private TextInputEditText editTextmail;
    private TextInputEditText editTextphone;
    private TextInputEditText editTextdate;
    private TextInputEditText editTextmarks;
    private Button btnreg;
    private Toolbar toolbar;
    private Notification.MessagingStyle.Message textInputEditText;
    private student student;
    private DBHelper DbHelper;
    boolean isRegCase;
    private String EMAIL_VALIDATION = "[a-zA-Z0-9._-]+@[a-zA-Z]+\\..+[a-zA-Z]+";
    private String PHONE_VALIDATION = "^[+]?[0-9]{10,13}$";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_form);
        toolbar = (Toolbar) findViewById(R.id.tool);
        setSupportActionBar(toolbar);

        editTextid = (TextInputEditText) findViewById(R.id.editid);
        editTextname = (TextInputEditText) findViewById(R.id.editname);
        editTextmail = (TextInputEditText) findViewById(R.id.editmail);
        editTextphone = (TextInputEditText) findViewById(R.id.editphone);
        editTextdate = (TextInputEditText) findViewById(R.id.editdate);
        editTextmarks = findViewById(R.id.editmarks);
        btnreg = (Button) findViewById(R.id.btnregs);

        initialize();
        DatePickerDialog();
    }

    private void initialize() {
        DbHelper = DBHelper.getDBHelper(this);

        isRegCase = getIntent().getExtras().getBoolean("Create");
        if (isRegCase) {
            btnreg.setText("Register");
            editTextid.setEnabled(false);
        } else {
            btnreg.setText("Edit");
            Integer Id = getIntent().getIntExtra("ID", 0);

            ArrayList<student> studentList = DbHelper.getAllStudent();
            for (student record : studentList) {
                if (record.getID() == Id) {
                    student = record;
                    break;
                }

            }

            editTextid.setVisibility(View.VISIBLE);
            editTextid.setText(String.valueOf(student.getID()));
            editTextid.setEnabled(false);
            editTextdate.setText(student.getDate());
            editTextname.setText(student.getName());
            editTextmail.setText(student.getEmail());
            editTextphone.setText(student.getPhone_No());
            editTextmarks.setText(student.getMarks());

        }
        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid().length() <= 0) {
                    if (isRegCase) postDataToSQLitr();
                    else Update();
                }
            }
        });
    }

    private void DatePickerDialog() {
        editTextdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                // date picker dialog
                DatePickerDialog picker = new DatePickerDialog(StudentDetailsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                editTextdate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
    }

    private String isValid() {

        String errorMsg = "";
        EditText errorTxt = null;

        editTextname.setError(null);
        editTextdate.setError(null);
        editTextmail.setError(null);
        editTextphone.setError(null);

        String name = editTextname.getText().toString();
        String date = editTextdate.getText().toString();
        String email = editTextmail.getText().toString();
        String phone = editTextphone.getText().toString();

        if (name == null || name.length() <= 0) {
            errorMsg = "Invalid Name";
            errorTxt = editTextname;
        } else if (date == null || date.length() <= 0) {
            errorMsg = "Invalid Birth Date";
            errorTxt = editTextdate;
        } else if (email == null || email.length() <= 0 || !Pattern.matches(EMAIL_VALIDATION, email)) {
            errorMsg = "Invalid Email";
            errorTxt = editTextmail;
        } else if (phone == null || phone.length() <= 0 || !Pattern.matches(PHONE_VALIDATION, phone)) {
            errorMsg = "Invalid Phone";
            errorTxt = editTextphone;
        }

        if (errorMsg.length() > 0 && errorTxt != null) {
            errorTxt.setError(errorMsg);
            errorTxt.requestFocus();
        }
        return errorMsg;
    }


    private void postDataToSQLitr() {

        student = new student();
        student.setName(editTextname.getText().toString().trim());
        student.setEmail(editTextmail.getText().toString().trim());
        student.setPhone_No(editTextphone.getText().toString().trim());
        student.setDate(editTextdate.getText().toString().trim());
        if (editTextmarks.getText().toString().trim().isEmpty()) {
            student.setMarks("0");
        } else {
            student.setMarks(editTextmarks.getText().toString().trim());
        }

        DbHelper.addStudent(student);
        finish();
        Toast.makeText(this, R.string.RegToast, Toast.LENGTH_SHORT).show();
    }

    private void Update() {
        student.setName(editTextname.getText().toString().trim());
        student.setEmail(editTextmail.getText().toString().trim());
        student.setPhone_No(editTextphone.getText().toString().trim());
        student.setDate(editTextdate.getText().toString().trim());
        student.setMarks(editTextmarks.getText().toString().trim());

        DbHelper.updateUser(student);
        finish();
        Toast.makeText(this, R.string.editToast, Toast.LENGTH_SHORT).show();

    }

}