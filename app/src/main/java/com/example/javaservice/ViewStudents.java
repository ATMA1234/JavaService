package com.example.javaservice;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.javaservice.Adapter.StudentAdapter;
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
import static com.example.javaservice.Values.Constants.UPDATE_DELETE_FAILURE;
import static com.example.javaservice.Values.Constants.UPDATE_DELETE_SUCCESS;

public class ViewStudents extends AppCompatActivity {
    StudentAdapter studentAdapter;
    RecyclerView recyclerView;
    SendingData sendingData;
    ArrayList<Student> arrayList;
    ProgressDialog progressDialog;
    FunctionCall functionCall;
    Student student;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case DIALOG_SUCCESS:
                    progressDialog.dismiss();
                    functionCall.showtoast(ViewStudents.this, "Success...");
                    break;

                case DIALOG_FAILURE:
                    progressDialog.dismiss();
                    functionCall.showtoast(ViewStudents.this, "Failure...");
                    break;

                case UPDATE_DELETE_SUCCESS:
                    progressDialog.dismiss();
                    startActivity(getIntent());
                    finish();
                    functionCall.showtoast(ViewStudents.this, "Success...");
                    break;

                case UPDATE_DELETE_FAILURE:
                    progressDialog.dismiss();
                    functionCall.showtoast(ViewStudents.this, "Failure...");
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_students);

        student = new Student();
        functionCall = new FunctionCall();
        progressDialog = new ProgressDialog(this);
        sendingData = new SendingData();
        recyclerView = findViewById(R.id.rec_student);
        arrayList = new ArrayList<>();
        studentAdapter = new StudentAdapter(arrayList, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(studentAdapter);

        functionCall.showprogressdialog("Please wait to complete...", progressDialog, "Fetching Records");
        SendingData.Student_Details studentDetails = sendingData.new Student_Details(handler, arrayList, studentAdapter);
        studentDetails.execute();
    }

    public void showDialoag(int id, int pos, ArrayList<Student> arrayList) {
        Dialog dialog;
        switch (id) {
            case DLG_DELETE:
                AlertDialog.Builder dialog_delete = new AlertDialog.Builder(this);
                dialog_delete.setTitle("Delete Record");
                @SuppressLint("InflateParams")
                LinearLayout layout_delete = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_delete_update, null);
                final EditText delete_id = layout_delete.findViewById(R.id.et_id);
                final EditText delete_name = layout_delete.findViewById(R.id.et_name);
                final EditText delete_age = layout_delete.findViewById(R.id.et_age);
                final EditText delete_place = layout_delete.findViewById(R.id.et_place);
                delete_id.setEnabled(false);delete_name.setEnabled(false);delete_age.setEnabled(false);delete_place.setEnabled(false);
                delete_id.setText(arrayList.get(pos).getId());
                delete_name.setText(arrayList.get(pos).getName());
                delete_age.setText(arrayList.get(pos).getAge());
                delete_place.setText(arrayList.get(pos).getPlace());
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
                update_id.setEnabled(false);
                dialog_update.setView(layout_update);
                update_id.setText(arrayList.get(pos).getId());
                update_name.setText(arrayList.get(pos).getName());
                update_age.setText(arrayList.get(pos).getAge());
                update_place.setText(arrayList.get(pos).getPlace());
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
}
