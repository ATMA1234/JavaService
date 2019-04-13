package com.example.javaservice;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.javaservice.Invoke.SendingData;
import com.example.javaservice.Model.Student;
import com.example.javaservice.Values.FunctionCall;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.javaservice.Values.Constants.DIALOG_FAILURE;
import static com.example.javaservice.Values.Constants.DIALOG_SUCCESS;
import static com.example.javaservice.Values.Constants.DLG_DELETE;
import static com.example.javaservice.Values.Constants.DLG_UPDATE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button insert, delete, update, view;
    EditText name, age, location;
    SendingData sendingData;
    Student student;
    ProgressDialog progressDialog;
    FunctionCall functionCall;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case DIALOG_SUCCESS:
                    progressDialog.dismiss();
                    functionCall.showtoast(MainActivity.this, "Success...");
                    name.setText("");
                    age.setText("");
                    location.setText("");
                    break;

                case DIALOG_FAILURE:
                    progressDialog.dismiss();
                    functionCall.showtoast(MainActivity.this, "Failure...");
                    break;
            }
            return false;
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    public void initialize() {
        functionCall = new FunctionCall();
        progressDialog = new ProgressDialog(this);
        student = new Student();
        sendingData = new SendingData();
        name = findViewById(R.id.et_name);
        age = findViewById(R.id.et_age);
        location = findViewById(R.id.et_location);
        insert = findViewById(R.id.btn_insert);
        insert.setOnClickListener(this);
        delete = findViewById(R.id.btn_delete);
        delete.setOnClickListener(this);
        update = findViewById(R.id.btn_update);
        update.setOnClickListener(this);
        view = findViewById(R.id.btn_view);
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_insert:
                insertStudent();
                break;

            case R.id.btn_delete:
                moveNext();
                break;

            case R.id.btn_update:
                moveNext();
                break;

            case R.id.btn_view:
                moveNext();
                break;
        }
    }

    public void insertStudent() {
        if (TextUtils.isEmpty(name.getText().toString())) {
            name.setError("Enter name");
            return;
        }
        if (TextUtils.isEmpty(age.getText().toString())) {
            age.setError("Enter age");
            return;
        }
        if (TextUtils.isEmpty(location.getText().toString())) {
            location.setError("Enter place");
            return;
        }
        student.setName(name.getText().toString());
        student.setAge(age.getText().toString());
        student.setPlace(location.getText().toString());
        Gson gson = new GsonBuilder().create();
        try {
            JSONObject jsonObject = new JSONObject(gson.toJson(student));
            functionCall.showprogressdialog("Please wait to complete", progressDialog, "Inserting Records");
            SendingData.Insert_Records insertRecords = sendingData.new Insert_Records(handler, jsonObject);
            insertRecords.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void moveNext() {
        Intent intent = new Intent(MainActivity.this, ViewStudents.class);
        startActivity(intent);
    }
}
