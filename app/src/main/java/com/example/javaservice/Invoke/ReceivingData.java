package com.example.javaservice.Invoke;

import android.os.Handler;
import android.text.TextUtils;

import com.example.javaservice.Adapter.StudentAdapter;
import com.example.javaservice.Model.Student;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.javaservice.Values.Constants.DIALOG_FAILURE;
import static com.example.javaservice.Values.Constants.DIALOG_SUCCESS;
import static com.example.javaservice.Values.Constants.UPDATE_DELETE_FAILURE;
import static com.example.javaservice.Values.Constants.UPDATE_DELETE_SUCCESS;

public class ReceivingData {

    //------------------------------------------------------------------------------------------------------------------------
    void getInsertResult(String result, Handler handler) {
        if (!TextUtils.isEmpty(result)) {
            handler.sendEmptyMessage(DIALOG_SUCCESS);
        } else {
            handler.sendEmptyMessage(DIALOG_FAILURE);
        }
    }

    //------------------------------------------------------------------------------------------------------------------------
    void getDeleteResult(String result, Handler handler) {
        if (!TextUtils.isEmpty(result)) {
            handler.sendEmptyMessage(UPDATE_DELETE_SUCCESS);
        } else {
            handler.sendEmptyMessage(UPDATE_DELETE_FAILURE);
        }
    }

    //------------------------------------------------------------------------------------------------------------------------
    void getUpdateResult(String result, Handler handler) {
        if (!TextUtils.isEmpty(result)) {
            handler.sendEmptyMessage(UPDATE_DELETE_SUCCESS);
        } else {
            handler.sendEmptyMessage(UPDATE_DELETE_FAILURE);
        }
    }

    //---------------------------------------------------------------------------------------------------------------------------
    void getStudentDetails(String result, Handler handler, ArrayList<Student> studentArrayList, StudentAdapter studentAdapter) {
        JSONArray jsonArray;
        studentArrayList.clear();
        try {
            jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Student student = new Gson().fromJson(jsonObject.toString(), Student.class);
                studentArrayList.add(student);
                studentAdapter.notifyDataSetChanged();
            }
            if (studentArrayList.size() > 0)
                handler.sendEmptyMessage(DIALOG_SUCCESS);
            else handler.sendEmptyMessage(DIALOG_FAILURE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
