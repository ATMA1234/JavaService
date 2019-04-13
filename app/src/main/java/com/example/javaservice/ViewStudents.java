package com.example.javaservice;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.javaservice.Adapter.StudentAdapter;
import com.example.javaservice.Invoke.SendingData;
import com.example.javaservice.Model.Student;
import com.example.javaservice.Values.FunctionCall;

import java.util.ArrayList;

import static com.example.javaservice.Values.Constants.DIALOG_FAILURE;
import static com.example.javaservice.Values.Constants.DIALOG_SUCCESS;

public class ViewStudents extends AppCompatActivity {
    StudentAdapter studentAdapter;
    RecyclerView recyclerView;
    SendingData sendingData;
    ArrayList<Student> arrayList;
    ProgressDialog progressDialog;
    FunctionCall functionCall;

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
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_students);

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
}
