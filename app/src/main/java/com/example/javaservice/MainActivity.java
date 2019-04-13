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

import static com.example.javaservice.Values.Constants.DIALOG_FAILURE;
import static com.example.javaservice.Values.Constants.DIALOG_SUCCESS;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int DLG_DELETE = 1;
    private static final int DLG_UPDATE = 2;
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
                showDialoag(DLG_DELETE);
                break;

            case R.id.btn_update:
                showDialoag(DLG_UPDATE);
                break;

            case R.id.btn_view:
                Intent intent = new Intent(MainActivity.this, ViewStudents.class);
                startActivity(intent);
                break;
        }
    }

    public void showDialoag(int id) {
        Dialog dialog;
        switch (id) {
            case DLG_DELETE:
                AlertDialog.Builder dialog_delete = new AlertDialog.Builder(this);
                dialog_delete.setTitle("Delete Record");
                @SuppressLint("InflateParams")
                LinearLayout layout_delete = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_delete_update, null);
                final EditText delete_id = layout_delete.findViewById(R.id.et_id);
                Button search = layout_delete.findViewById(R.id.btn_search);
                search.setVisibility(View.GONE);
                dialog_delete.setView(layout_delete);
                dialog_delete.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (TextUtils.isEmpty(delete_id.getText())) {
                            delete_id.setError("Please Enter ID");
                            return;
                        }
                        functionCall.showprogressdialog("Please wait to complete", progressDialog, "Deleting Records");
                        SendingData.Delete_Records delete_records = sendingData.new Delete_Records(handler);
                        delete_records.execute(delete_id.getText().toString());
                    }
                });
                dialog = dialog_delete.create();
                dialog.show();
                break;

            case DLG_UPDATE:
                final AlertDialog.Builder dialog_update = new AlertDialog.Builder(this);
                dialog_update.setTitle("Update Record");
                @SuppressLint("InflateParams")
                LinearLayout layout_update = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_delete_update, null);
                final EditText update_id = layout_update.findViewById(R.id.et_id);
                final EditText update_name = layout_update.findViewById(R.id.et_name);
                final EditText update_age = layout_update.findViewById(R.id.et_age);
                final EditText update_place = layout_update.findViewById(R.id.et_place);
                Button search1 = layout_update.findViewById(R.id.btn_search);
                search1.setVisibility(View.GONE);
                update_name.setVisibility(View.VISIBLE);
                update_age.setVisibility(View.VISIBLE);
                update_place.setVisibility(View.VISIBLE);
                dialog_update.setView(layout_update);
                dialog_update.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (TextUtils.isEmpty(update_id.getText())) {
                            update_id.setError("Please Enter ID");
                            return;
                        }
                        student.setId(update_id.getText().toString());
                        student.setName(update_name.getText().toString());
                        student.setAge(update_age.getText().toString());
                        student.setPlace(update_place.getText().toString());
                        Gson gson = new GsonBuilder().create();
                        try {
                            JSONObject jsonObject = new JSONObject(gson.toJson(student));
                            functionCall.showprogressdialog("Please wait to complete", progressDialog, "Updating Records");
                            SendingData.Update_Records update_records = sendingData.new Update_Records(handler, jsonObject);
                            update_records.execute();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                dialog = dialog_update.create();
                dialog.show();
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
}
